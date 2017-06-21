package org.sysuboys.diaryu.exception;

/**
 * 标明错误由服务端导致
 *
 */
public class ServerError extends DiaryuException {

	private static final long serialVersionUID = 575080077807304755L;

	public ServerError() {
		super();
	}

	public ServerError(String message) {
		super(message);
	}

}
