package org.sysuboys.diaryu.exception;

/**
 * websocket连接session不存在
 *
 */
public class NoSessionError extends DiaryuException {

	private static final long serialVersionUID = 8320119069898807064L;

	final String username;

	public NoSessionError() {
		super();
		username = null;
	}

	public NoSessionError(String message, String username) {
		super(message);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}
