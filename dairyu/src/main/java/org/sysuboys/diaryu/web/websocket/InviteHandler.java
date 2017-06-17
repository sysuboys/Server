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

		JSONObject receivedObj = new JSONObject(message.getPayload());
		String invitee = (String) receivedObj.get("invitee");
		String title = (String) receivedObj.get("title");

		String error = null;
		if (invitee == null) {
			error = "parameter \"invitee\" not found or was wrong in type";
		} else if (title == null) {
			error = "parameter \"title\" not found or was wrong in type";
		} else if (userService.findDiaryByUsernameAndTitle(username, title) == null) {
			error = "you have no diary titled \"" + title + "\"";
		} else {
			// 用户同一时间只允许一个关系，不能同时跟2人交换
			synchronized (map) { // 交换关系创建锁
				if (map.containsKey(username))
					error = "you have already an exchange";
				else if (map.containsKey(invitee))
					error = "your friend " + invitee + " has already an exchange";
				else {
					ExchangeModel exchangeModel = new ExchangeModel(username, invitee, title);
					map.put(username, exchangeModel);
					map.put(invitee, exchangeModel);
				}
			}
		}

		WebSocketSession friendSession = null;
		if (error == null) {
			friendSession = webSocketSessionMap.get(invitee).get(SessionType.isInvited);
			if (friendSession == null)
				error = "your friend " + invitee + " has no \"isInvited\" session";
		}

		JSONObject object = new JSONObject();
		if (error != null) {
			object.put("success", false);
			object.put("error", error);

			logger.debug("return error: " + error);

			TextMessage informMessage = new TextMessage(object.toString());
			synchronized (session) {
				session.sendMessage(informMessage);
			}
		} else {
			object.put("invited", true);
			object.put("inviter", username);
			object.put("title", title);

			logger.debug("send to isInvited: " + object.toString());

			TextMessage informMessage = new TextMessage(object.toString());
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