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
import org.sysuboys.diaryu.business.service.DiaryService;
import org.sysuboys.diaryu.business.service.ExchangeModelService;
import org.sysuboys.diaryu.business.service.FriendshipService;
import org.sysuboys.diaryu.business.service.LoginService;
import org.sysuboys.diaryu.business.service.UserService;
import org.sysuboys.diaryu.business.service.WebSocketSessionService;

/**
 * 4个Handler的基类，在事件时进行log，同时提供一些功能函数
 *
 */
public abstract class AbstractBaseHandler extends TextWebSocketHandler {

	Logger logger;

	@Autowired
	WebSocketSessionService webSocketSessionService;
	@Autowired
	ExchangeModelService exchangeModelService;
	@Autowired
	LoginService loginService;
	@Autowired
	UserService userService;
	@Autowired
	DiaryService diaryService;
	@Autowired
	FriendshipService friendshipService;

	Map<String, ExchangeModel> exchangeMap;

	public AbstractBaseHandler() {
		logger = Logger.getLogger(this.getClass());
	}

	public abstract SessionType getSessionType();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws IOException {

		String username = getUsername(session);

		logger.info("[" + username + "] connected");
		webSocketSessionService.get(username).put(getSessionType(), session);
		exchangeMap = exchangeModelService.get();

	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO 所有handler:连接断开？……
		super.handleTextMessage(session, message);
		logger.debug("[" + getUsername(session) + "] receive: " + message.getPayload());
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable thrwbl) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		logger.warn("[" + getUsername(session) + "] TransportError");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus cs) throws Exception {
		String username = getUsername(session);
		logger.info("[" + username + "] closed");
		if (username != null)
			webSocketSessionService.get(username).remove(getSessionType());
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

	public void sendJSON(WebSocketSession session, JSONObject json) throws IOException {
		TextMessage returnMessage = new TextMessage(json.toString());
		synchronized (session) {
			session.sendMessage(returnMessage);
		}
	}

	public void cancelExchange(String username) {
		ExchangeModel model = exchangeMap.remove(username);
		if (model != null)
			exchangeMap.remove(model.getAnother(username));
	}

	/**
	 * get username according to sessionid in session attributes
	 * 
	 * @param session
	 * @return username nullable
	 */
	public String getUsername(WebSocketSession session) {
		String sessionid = (String) session.getAttributes().get(Constant.sessionid);
		if (sessionid == null)
			logger.error("can't get sessionid from session attributes");
		String username = loginService.getUsername(sessionid);
		if (username == null)
			logger.error("can't get username by sessionid");
		return username;
	}

}