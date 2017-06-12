package org.sysuboys.diaryu.business.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.dao.IUserDao;

@Service
public class SessionService implements ISessionService {

	@Autowired
	IUserDao userDao;

	Map<String, Map<String, WebSocketSession>> map = new HashMap<String, Map<String, WebSocketSession>>();

	synchronized public boolean remove(String username) {
		return map.remove(username) != null;
	}

	synchronized public Map<String, WebSocketSession> getMap(String username) {
		Map<String, WebSocketSession> rtn = map.get(username);
		if (rtn == null) {
			rtn = new HashMap<String, WebSocketSession>();
			map.put(username, rtn);
		}
		return rtn;
	}

}
