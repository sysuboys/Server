package org.sysuboys.diaryu.business.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.SessionType;

@Service
public class WebSocketSessionServiceImpl implements WebSocketSessionService {

	Map<String, Map<SessionType, WebSocketSession>> map = new ConcurrentHashMap<String, Map<SessionType, WebSocketSession>>();

	public Map<SessionType, WebSocketSession> get(String username) {
		Map<SessionType, WebSocketSession> _map = map.get(username);
		if (_map == null) {
			_map = new ConcurrentHashMap<SessionType, WebSocketSession>();
			map.put(username, _map);
		}
		return _map;
	}

}
