package org.sysuboys.diaryu.web.websocket;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;

public class MatchHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);

		JSONObject receivedObj = new JSONObject(message.getPayload());
		Integer position = (Integer) receivedObj.get("position");

		String error = null;
		ExchangeModel exchangeModel = map.get(username);
		if (position == null)
			error = "parameter \"position\" not found or was wrong in type";
		else if (exchangeModel == null)
			error = "you didn't invite and are not invited";
		else if (!exchangeModel.isReady())
			error = exchangeModel.getInviter().equals(username) ? "your friend are not ready" : "you didn't get ready";

		JSONObject rtn = new JSONObject();
		if (error != null) {
			rtn.put("success", false);
			rtn.put("error", error);

			logger.debug("return error: " + error);
		} else {
			boolean result = exchangeModel.match(username, position);
			rtn.put("success", result);

			logger.debug("match result: " + result);
		}

		TextMessage informMessage = new TextMessage(rtn.toString());
		synchronized (session) {
			session.sendMessage(informMessage);
		}

		if ((Boolean) rtn.get("success")) { // 成功后也立即通知另一个人
			String friend = exchangeModel.getAnother(username);
			WebSocketSession friendSession = webSocketSessionMap.get(friend).get(SessionType.match);
			if (friendSession == null) {
				logger.warn(username + "'s friend " + friend + "have no \"match\" session, exchange abort");
				map.remove(username);
				map.remove(friend);
			} else
				synchronized (friendSession) {
					friendSession.sendMessage(informMessage);
				}
		}
	}

	@Override
	public SessionType getSessionType() {
		return SessionType.match;
	}

}