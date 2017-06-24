package org.sysuboys.diaryu.web.websocket;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.SessionType;
import org.sysuboys.diaryu.exception.ClientError;

public class IsAddedHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		super.handleTextMessage(session, message);
		String username = getUsername(session);

		try {
			String inviter = null;
			boolean accept = false;
			try {
				JSONObject rcvObj = new JSONObject(message.getPayload());
				inviter = (String) rcvObj.get("inviter");
				accept = (Boolean) rcvObj.get("accept");
			} catch (JSONException e) {
				throw new ClientError("JSON format error");
			}

			boolean success = friendshipService.removeRequest(inviter, username);
			if (!success)
				throw new ClientError("you are not requested by \"" + inviter + "\"");

			logger.info("[" + username + "] " + (accept ? "" : "don't") + " accept [" + inviter + "] to be friends");

			if (accept)
				friendshipService.makeFriend(username, inviter);

			JSONObject informObj = new JSONObject();
			informObj.put("success", accept);
			informObj.put("accept", accept);
			informObj.put("invitee", username);

			WebSocketSession inviterAdd = webSocketSessionService.get(inviter).get(SessionType.add);
			if (inviterAdd == null) {
				logger.warn("[" + inviter + "][add] not exists after [" + username
						+ "] accepted, successful message will not send");
			} else {
				sendJSON(inviterAdd, informObj);
				logger.debug("[" + username + "] send to [" + inviter + "][add]: " + informObj.toString());
			}

		} catch (ClientError e) {
			logger.warn("[" + username + "] " + e.getMessage());
			sendJSONErrorMessage(session, e.getMessage());
		}

	}

	@Override
	public SessionType getSessionType() {
		return SessionType.isAdded;
	}

}