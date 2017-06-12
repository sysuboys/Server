package org.sysuboys.diaryu.business.service;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

public interface ISessionService {
	
	boolean remove(String username);
	
	// 每个用户对应一张表，含多个名字标识的Session
	Map<String, WebSocketSession> getMap(String username);

}
