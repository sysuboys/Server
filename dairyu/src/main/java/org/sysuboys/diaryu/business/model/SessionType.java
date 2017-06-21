package org.sysuboys.diaryu.business.model;

/**
 * 标识同一用户的不同WebSocket连接
 *
 */
public enum SessionType {
	invite, isInvited, ready, match,
}
