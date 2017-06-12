package org.sysuboys.diaryu.business.service;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

public interface ISessionService {

	// 每个（用户）id：Long，对应多个名字标识的Session
	Map<String, WebSocketSession> getMap(String username);

}
