package org.sysuboys.diaryu.web.websocket;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;

public class InviteHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);

		JSONObject object = new JSONObject(message.getPayload());
		String friend = (String) object.get("friend");
		String title = (String) object.get("title");

		// TODO 好友在线和日记存在检测

		// 用户同一时间只允许一个关系，不能同时跟2人交换
		String error = null;
		synchronized (map) { // 交换关系创建锁
			if (map.containsKey(username))
				error = "you have already an exchange";
			else if (map.containsKey(friend))
				error = "your friend " + friend + " has already an exchange";
			else {
				ExchangeModel exchangeModel = new ExchangeModel(username, friend, title);
				map.put(username, exchangeModel);
				map.put(friend, exchangeModel);
			}
		}

		JSONObject inform = new JSONObject();
		if (error != null) {
			inform.put("success", false);
			inform.put("message", message);

			TextMessage informMessage = new TextMessage(inform.toString());
			synchronized (session) {
				session.sendMessage(informMessage);
			}
		} else {
			inform.put("invited", true);
			inform.put("friend", username);

			TextMessage informMessage = new TextMessage(inform.toString());
			WebSocketSession friendSession = webSocketSessionMap.get(friend).get(SessionType.isInvited);
			synchronized (friendSession) {
				friendSession.sendMessage(informMessage);
			}
		}

	}

	@Override
	public SessionType getSessionType() {
		return SessionType.invite;
	}

}