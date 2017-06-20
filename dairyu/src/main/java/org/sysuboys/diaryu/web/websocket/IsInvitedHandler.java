package org.sysuboys.diaryu.web.websocket;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;

public class IsInvitedHandler extends AbstractBaseHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		super.handleTextMessage(session, message);

		JSONObject rtnObj = new JSONObject();
		ExchangeModel model = exchangeMap.get(username);
		if (model == null) {
			rtnObj.put("invited", false);
		} else {
			rtnObj.put("invited", true);
			rtnObj.put("inviter", model.getAnother(username));
			rtnObj.put("title", model.getFriendTitle(username));
		}

		TextMessage rtnMsg = new TextMessage(rtnObj.toString());
		synchronized (session) {
			session.sendMessage(rtnMsg);
		}
		logger.info("send back: " + rtnObj.toString());

	}

	@Override
	public SessionType getSessionType() {
		return SessionType.isInvited;
	}

}