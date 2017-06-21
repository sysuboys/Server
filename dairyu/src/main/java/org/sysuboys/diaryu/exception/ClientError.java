package org.sysuboys.diaryu.exception;

/**
 * 标明错误由客户端操作导致，接收返回客户端的反馈信息
 *
 */
public class ClientError extends DiaryuException {

	private static final long serialVersionUID = 1727569940898139698L;

	public ClientError() {
		super();
	}

	public ClientError(String message) {
		super(message);
	}

}
