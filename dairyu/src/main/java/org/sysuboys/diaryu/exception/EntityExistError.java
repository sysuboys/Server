package org.sysuboys.diaryu.exception;

/**
 * 添加实体的时候出现冲突（如用户名已存在）
 *
 */
public class EntityExistError extends DiaryuException {

	private static final long serialVersionUID = -5579354624629834796L;

	public EntityExistError() {
		super();
	}

	public EntityExistError(String className) {
		super("entity exist: " + className);
	}

}
