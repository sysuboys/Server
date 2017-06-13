package org.sysuboys.diaryu.web.websocket;

import org.sysuboys.diaryu.business.model.SessionType;

public class IsInvitedHandler extends AbstractBaseHandler {

	@Override
	public SessionType getSessionType() {
		return SessionType.isInvited;
	}

}