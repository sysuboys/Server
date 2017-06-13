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

		JSONObject rtn = new JSONObject();
		ExchangeModel exchangeModel = map.get(username);
		if (exchangeModel == null) {
			rtn.put("invited", false);
		} else {
			rtn.put("invited", false);
			rtn.put("inviter", exchangeModel.getAnother(username));
			rtn.put("title", exchangeModel.getFriendTitle(username));
		}

		TextMessage returnMessage = new TextMessage(rtn.toString());
		synchronized (session) {
			session.sendMessage(returnMessage);
		}
	}

	@Override
	public SessionType getSessionType() {
		return SessionType.isInvited;
	}

}