package org.sysuboys.diaryu.exception;

public class NoSuchUser extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchUser() {
		super();
	}

	public NoSuchUser(String message) {
		super(message);
	}

}
