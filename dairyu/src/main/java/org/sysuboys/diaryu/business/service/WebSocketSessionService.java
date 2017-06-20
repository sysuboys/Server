package org.sysuboys.diaryu.business.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.SessionType;

@Service
public class WebSocketSessionService implements IWebSocketSessionService {

	Map<String, Map<SessionType, WebSocketSession>> map = new ConcurrentHashMap<String, Map<SessionType, WebSocketSession>>();

	public Map<SessionType, WebSocketSession> get(String username) {
		return map.getOrDefault(username, new ConcurrentHashMap<SessionType, WebSocketSession>());
	}

}
