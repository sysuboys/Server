package org.sysuboys.diaryu.business.service;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;
import org.sysuboys.diaryu.business.model.SessionType;

public interface IWebSocketSessionService {

	// 每个用户对应一个连接列表
	Map<SessionType, WebSocketSession> get(String username);

}
