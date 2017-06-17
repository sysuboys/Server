package org.sysuboys.diaryu.web.websocket;

import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.sysuboys.diaryu.business.model.Constant;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;
import org.sysuboys.diaryu.business.service.IExchangeModelMap;
import org.sysuboys.diaryu.business.service.ILoginService;
import org.sysuboys.diaryu.business.service.IUserService;
import org.sysuboys.diaryu.business.service.IWebSocketSessionMap;

public abstract class AbstractBaseHandler extends TextWebSocketHandler {

	Logger logger;

	@Autowired
	IWebSocketSessionMap webSocketSessionMap;
	@Autowired
	IExchangeModelMap exchangeModelMap;
	@Autowired
	ILoginService loginService;
	@Autowired
	IUserService userService;

	String username;
	Map<String, ExchangeModel> map;

	public AbstractBaseHandler() {
		logger = Logger.getLogger(this.getClass());
	}

	public abstract SessionType getSessionType();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.debug("connected");

		// TODO throw
		String sessionid = (String) session.getAttributes().get(Constant.sessionid);
		if (sessionid == null) {
			logger.debug("can't get sessionid from Attributes after connection established");
			session.close(CloseStatus.SERVER_ERROR);
			return;
		}
		username = loginService.getUsername(sessionid);
		if (username == null) {
			logger.debug("can't get username with sessionid=" + sessionid);
			session.close(CloseStatus.SERVER_ERROR);
			return;
		}

		logger.debug("username=" + username);
		webSocketSessionMap.get(username).put(getSessionType(), session);
		map = exchangeModelMap.get();
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		logger.debug("username=" + username + ", receive: " + message.getPayload());
	}

	@Override
	public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
		if (wss.isOpen()) {
			wss.close();
		}
		logger.info("username=" + username + ", TransportError");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
		logger.debug("username=" + username + ", closed");
		if (username != null)
			webSocketSessionMap.get(username).remove(SessionType.invite);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}