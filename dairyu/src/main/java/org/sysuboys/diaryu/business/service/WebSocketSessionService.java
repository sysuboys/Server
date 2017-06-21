package org.sysuboys.diaryu.business.service;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.SessionType;

public interface WebSocketSessionService {

	/**
	 * 获取单个用户对应的WebSocket连接列表
	 * 
	 * @param username
	 * @return
	 */
	Map<SessionType, WebSocketSession> get(String username);

}
