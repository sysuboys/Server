package org.sysuboys.diaryu.web.websocket;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;
import org.sysuboys.diaryu.exception.ClientError;
import org.sysuboys.diaryu.exception.NoSessionError;

public class MatchHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		super.handleTextMessage(session, message);
		String username = getUsername(session);

		try {

			Integer position = null;
			try {
				JSONObject rcvObj = new JSONObject(message.getPayload());
				position = (Integer) rcvObj.get("position");
			} catch (JSONException e) {
				throw new ClientError("JSON format error");
			}

			ExchangeModel model = exchangeMap.get(username);
			if (position == null)
				throw new ClientError("parameter \"position\" is not Integer");
			if (model == null)
				throw new ClientError("you didn't invite and are not invited");
			if (!model.isReady())
				throw new ClientError(
						model.getInviter().equals(username) ? "your friend are not ready" : "you didn't get ready");

			boolean success = model.match(username, position);
			logger.debug("[" + username + "] is at position " + position);

			JSONObject informObj = new JSONObject();
			informObj.put("success", success);
			logger.debug("match result: " + success);

			sendJSON(session, informObj);

			if (success) { // 成功后也立即通知另一个人
				logger.info("[" + username + "] and [" + model.getAnother(username) + "] matched");
				String friend = model.getAnother(username);
				WebSocketSession friendSession = webSocketSessionService.get(friend).get(SessionType.match);
				if (friendSession == null)
					throw new NoSessionError("[" + friend + "][match] not exist", friend);
				sendJSON(friendSession, informObj);
			}

		} catch (ClientError e) {
			logger.warn("[" + username + "] " + e.getMessage());
			sendJSONErrorMessage(session, e.getMessage());
		} catch (NoSessionError e) { // TODO 通知客户端。更改消息形式
			logger.warn("[" + username + "] " + e.getMessage());
			logger.warn("exchange abort");
			String friend = e.getUsername();
			exchangeMap.remove(friend);
			exchangeMap.remove(username);
		}

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus cs) throws Exception {
		String username = getUsername(session);
		ExchangeModel model = exchangeMap.get(username);
		if (model != null && model.isReady() && !model.isMatched()) {
			cancelExchange(username);
			logger.warn("[" + username + "] match session closed before matched, exchange abort");
		}
		super.afterConnectionClosed(session, cs);
	}

	@Override
	public SessionType getSessionType() {
		return SessionType.match;
	}

}