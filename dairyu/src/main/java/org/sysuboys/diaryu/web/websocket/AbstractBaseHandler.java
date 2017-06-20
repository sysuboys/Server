package org.sysuboys.diaryu.web.websocket;

import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.sysuboys.diaryu.business.model.Constant;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.model.SessionType;
import org.sysuboys.diaryu.business.service.IExchangeModelService;
import org.sysuboys.diaryu.business.service.IFriendshipService;
import org.sysuboys.diaryu.business.service.ILoginService;
import org.sysuboys.diaryu.business.service.IUserService;
import org.sysuboys.diaryu.business.service.IWebSocketSessionService;

public abstract class AbstractBaseHandler extends TextWebSocketHandler {

	Logger logger;

	@Autowired
	IWebSocketSessionService webSocketSessionService;
	@Autowired
	IExchangeModelService exchangeModelService;
	@Autowired
	ILoginService loginService;
	@Autowired
	IUserService userService;
	@Autowired
	IFriendshipService friendshipService;

	String username;
	Map<String, ExchangeModel> exchangeMap;

	public AbstractBaseHandler() {
		logger = Logger.getLogger(this.getClass());
	}

	public abstract SessionType getSessionType();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws IOException {

		logger.info("connected");

		String sessionid = (String) session.getAttributes().get(Constant.sessionid);
		if (sessionid == null) {
			logger.error("can't get sessionid from Attributes after connection established");
			session.close(CloseStatus.SERVER_ERROR);
			return;
		}
		username = loginService.getUsername(sessionid);
		if (username == null) {
			logger.error("can't get username with sessionid=" + sessionid);
			session.close(CloseStatus.SERVER_ERROR);
			return;
		}

		logger.info("username=" + username);
		webSocketSessionService.get(username).put(getSessionType(), session);
		exchangeMap = exchangeModelService.get();

	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO 所有handler:连接断开？……
		super.handleTextMessage(session, message);
		logger.info("username=" + username + ", receive: " + message.getPayload());
	}

	@Override
	public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
		if (wss.isOpen()) {
			wss.close();
		}
		logger.warn("username=" + username + ", TransportError");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
		logger.info("username=" + username + ", closed");
		if (username != null)
			webSocketSessionService.get(username).remove(SessionType.invite);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	public void sendJSONErrorMessage(WebSocketSession session, String message) throws IOException {

		JSONObject returnObject = new JSONObject();
		returnObject.put("success", false);
		returnObject.put("error", message);
		logger.info("send error: " + message);

		TextMessage returnMessage = new TextMessage(returnObject.toString());
		synchronized (session) {
			session.sendMessage(returnMessage);
		}

	}

}