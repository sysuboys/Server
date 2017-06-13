package org.sysuboys.diaryu.web.websocket;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;
import org.sysuboys.diaryu.business.service.IExchangeModelMap;
import org.sysuboys.diaryu.business.service.IWebSocketSessionMap;

public abstract class AbstractBaseHandler extends TextWebSocketHandler {

	Logger logger;

	@Autowired
	IWebSocketSessionMap webSocketSessionMap;
	@Autowired
	IExchangeModelMap exchangeModelMap;

	String username;
	String handlerName;
	Map<String, ExchangeModel> map;

	public abstract SessionType getSessionType();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger = Logger.getLogger(this.getClass().getName());
		String[] array = this.getClass().getName().split("\\.");
		handlerName = array[array.length - 1];

		Subject subject = SecurityUtils.getSubject();
		username = (String) subject.getPrincipal();

		logger.debug(handlerName + ": " + username + " connected");
		webSocketSessionMap.get(username).put(getSessionType(), session);
		map = exchangeModelMap.get();
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		logger.debug(handlerName + ": " + username + " receive: " + message);
	}

	@Override
	public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
		if (wss.isOpen()) {
			wss.close();
		}
		logger.info(handlerName + ": " + username + " TransportError");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
		logger.debug(handlerName + ": " + username + " closed");
		webSocketSessionMap.get(username).remove(SessionType.invite);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}