package org.sysuboys.diaryu.web.websocket;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;

public class ReadyHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);

		JSONObject receivedObj = new JSONObject(message.getPayload());
		String title2 = (String) receivedObj.get("title");

		String error = null;
		ExchangeModel exchangeModel = map.get(username);
		if (exchangeModel == null)
			error = "you are not invited";
		else if (exchangeModel.getInviter().equals(username))
			error = "inviter doesn't have to get ready";
		else if (title2 == null)
			error = "parameter \"title\" not found or was wrong in type";
		else if (userService.findDiaryByUsernameAndTitle(username, title2) == null)
			error = "you have no diary titled \"" + title2 + "\"";

		JSONObject rtn = new JSONObject();
		if (error != null) {
			rtn.put("success", false);
			rtn.put("error", error);

			logger.debug("return error: " + error);
		} else {
			rtn.put("success", true);
			exchangeModel.ready(title2);

			logger.debug(username + " get ready");
		}

		TextMessage returnMessage = new TextMessage(rtn.toString());
		synchronized (session) {
			session.sendMessage(returnMessage);
		}
	}

	@Override
	public SessionType getSessionType() {
		return SessionType.ready;
	}

}