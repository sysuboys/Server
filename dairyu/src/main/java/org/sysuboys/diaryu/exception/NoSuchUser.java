package org.sysuboys.diaryu.exception;

public class NoSuchUser extends DiaryuException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6990866450644444704L;

	public NoSuchUser() {
		super();
	}

	public NoSuchUser(String username) {
		super("user not found by username: " + username);
	}

}
