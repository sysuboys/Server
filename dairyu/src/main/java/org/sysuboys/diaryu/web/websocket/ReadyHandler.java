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

public class ReadyHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		super.handleTextMessage(session, message);

		try {

			String title2 = null;
			try {
				JSONObject rcvObj = new JSONObject(message.getPayload());
				title2 = (String) rcvObj.get("title");
			} catch (JSONException e) {
				throw new ClientError("JSON format error");
			}

			ExchangeModel exchangeModel = exchangeMap.get(username);
			if (title2 == null)
				throw new ClientError("parameter \"title\" is not String");
			if (exchangeModel == null)
				throw new ClientError("you are not invited");
			if (exchangeModel.getInviter().equals(username))
				throw new ClientError("inviter doesn't have to get ready");
			try {
				if (diaryService.findByUsernameAndTitle(username, title2) == null)
					throw new ClientError("you have no diary titled \"" + title2 + "\"");
			} catch (NoSuchUser e) {
				throw new ServerError("can not find user [" + username + "] while connecting");
			}

			exchangeModel.ready(title2);
			logger.info(username + " get ready");

			JSONObject rtnObj = new JSONObject();
			rtnObj.put("success", true);

			sendJSON(session, rtnObj);
			logger.info("send back: " + rtnObj.toString());

			String inviter = exchangeModel.getInviter();
			WebSocketSession inviterInvite = webSocketSessionService.get(inviter).get(SessionType.invite);
			if (inviterInvite == null) {
				cancelExchange(username);
				throw new ClientError("inviter [" + inviter + "] is not online, exchange abort");
			}
			sendJSON(inviterInvite, rtnObj);
			logger.info("send to inviter [" + inviter + "]: " + rtnObj.toString());

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
		return SessionType.ready;
	}

}