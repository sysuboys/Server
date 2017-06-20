package org.sysuboys.diaryu.web.websocket;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;
import org.sysuboys.diaryu.exception.ClientError;
import org.sysuboys.diaryu.exception.NoSuchUser;
import org.sysuboys.diaryu.exception.ServerError;

public class InviteHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		super.handleTextMessage(session, message);

		try {
			String invitee = null;
			String title = null;
			try {
				JSONObject rcvObj = new JSONObject(message.getPayload());
				invitee = (String) rcvObj.get("invitee");
				title = (String) rcvObj.get("title");
			} catch (JSONException e) {
				throw new ClientError("JSON format error");
			}

			if (invitee == null)
				throw new ClientError("parameter \"invitee\" is not String");
			if (title == null)
				throw new ClientError("parameter \"title\" is not String");
			try {
				if (friendshipService.findFriends(username).contains(invitee))
					throw new ClientError("you have no friend \"" + invitee + "\"");
				if (userService.findDiaryByUsernameAndTitle(username, title) == null)
					throw new ClientError("you have no diary titled \"" + title + "\"");
			} catch (NoSuchUser e) {
				throw new ServerError("can not find username \"" + username + "\" while connecting");
			}

			WebSocketSession friendSession = webSocketSessionService.get(invitee).get(SessionType.isInvited);
			if (friendSession == null) // TODO 可能是意外断开？
				throw new ClientError("your friend is not online");

			// 用户同一时间只允许一个关系，不能同时跟2人交换
			synchronized (exchangeMap) { // 交换关系创建锁。防止不可重复读？
				if (exchangeMap.containsKey(username))
					throw new ClientError("you have already an exchange");
				if (exchangeMap.containsKey(invitee))
					throw new ClientError("your friend \"" + invitee + "\" has already an exchange");
				ExchangeModel model = new ExchangeModel(username, invitee, title);
				exchangeMap.put(username, model);
				exchangeMap.put(invitee, model);
			}

			logger.info(username + " invite " + invitee + " with " + title);

			JSONObject informObj = new JSONObject();
			informObj.put("invited", true);
			informObj.put("inviter", username);
			informObj.put("title", title);

			TextMessage informMsg = new TextMessage(informObj.toString());
			synchronized (friendSession) {
				friendSession.sendMessage(informMsg);
			}
			logger.info("send to [isInvited]: " + informObj.toString());

		} catch (ClientError e) {
			logger.warn(e.getMessage());
			sendJSONErrorMessage(session, e.getMessage());
		} catch (ServerError e) {
			logger.error(e.getMessage());
			sendJSONErrorMessage(session, "server error");
		}

	}

	@Override
	public SessionType getSessionType() {
		return SessionType.invite;
	}

}