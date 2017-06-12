package org.sysuboys.diaryu.business.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

public class SessionService implements ISessionService {

	Map<String, Map<String, WebSocketSession>> _map = new HashMap<String, Map<String, WebSocketSession>>();

	public Map<String, WebSocketSession> getMap(String username) {
		Map<String, WebSocketSession> map = _map.get(username);
		if (map == null) {
			map = new HashMap<String, WebSocketSession>();
			_map.put(username, map);
		}
		return map;
	}

}
