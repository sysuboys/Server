package org.sysuboys.diaryu.web.websocket;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.sysuboys.diaryu.business.model.Constant;
import org.sysuboys.diaryu.business.service.LoginService;

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	static Logger logger = Logger.getLogger(HandshakeInterceptor.class);

	@Autowired
	LoginService loginService;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		logger.debug("Before Handshake");

		super.beforeHandshake(request, response, wsHandler, attributes);

		for (Entry<String, Object> entry : attributes.entrySet()) {
			logger.debug("attr - " + entry.getKey() + ": " + entry.getValue().toString());
		}
		String sessionid = (String) attributes.get(Constant.sessionid);
		if (sessionid == null) {
			logger.warn("can't get sessionid from attributes named \"" + Constant.sessionid + "\"");
			return false;
		}
		if (loginService.getUsername(sessionid) == null) {
			logger.warn("can't get username with sessionid=" + sessionid);
			return false;
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		logger.debug("After Handshake");
		super.afterHandshake(request, response, wsHandler, ex);
	}

}
