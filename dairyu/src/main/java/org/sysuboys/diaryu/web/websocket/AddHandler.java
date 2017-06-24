package org.sysuboys.diaryu.web.websocket;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.SessionType;
import org.sysuboys.diaryu.exception.ClientError;
import org.sysuboys.diaryu.exception.NoSuchUser;
import org.sysuboys.diaryu.exception.ServerError;

public class AddHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		super.handleTextMessage(session, message);
		String username = getUsername(session);

		try {
			String invitee = null;
			try {
				JSONObject rcvObj = new JSONObject(message.getPayload());
				invitee = (String) rcvObj.get("invitee");
			} catch (JSONException e) {
				throw new ClientError("JSON format error");
			}

			if (invitee == null)
				throw new ClientError("parameter \"invitee\" is not String");
			try {
				if (!userService.exist(invitee))
					throw new ClientError("user \"" + invitee + "\" doesn't exist");
				if (friendshipService.findFriends(username).contains(invitee) == true)
					throw new ClientError("you are already friends");
			} catch (NoSuchUser e) {
				throw new ServerError("can not find user [" + username + "] while connecting");
			}

			WebSocketSession inviteeIsAdded = webSocketSessionService.get(invitee).get(SessionType.isAdded);
			if (inviteeIsAdded == null)
				throw new ClientError("he(she) is not online");

			boolean success = friendshipService.registerRequest(username, invitee);
			if (success)
				throw new ClientError("you have already requested");

			logger.info("[" + username + "] want to make friends with [" + invitee + "]");

			JSONObject informObj = new JSONObject();
			informObj.put("invited", true);
			informObj.put("inviter", username);

			sendJSON(inviteeIsAdded, informObj);
			logger.debug("[" + username + "] send to [" + invitee + "][isAdded]: " + informObj.toString());

		} catch (ClientError e) {
			logger.warn("[" + username + "] " + e.getMessage());
			sendJSONErrorMessage(session, e.getMessage());
		} catch (ServerError e) {
			logger.error("[" + username + "] " + e.getMessage());
			sendJSONErrorMessage(session, "server error");
		}

	}

	@Override
	public SessionType getSessionType() {
		return SessionType.add;
	}

}