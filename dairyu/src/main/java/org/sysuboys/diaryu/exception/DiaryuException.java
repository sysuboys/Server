package org.sysuboys.diaryu.exception;

/**
 * 本项目基础异常类，不附带其他信息
 *
 */
public class DiaryuException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7473389435436653532L;

	public DiaryuException() {
		super();
	}

	public DiaryuException(String message) {
		super(message);
	}
}
