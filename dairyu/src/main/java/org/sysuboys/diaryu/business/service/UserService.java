package org.sysuboys.diaryu.business.service;

import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.exception.EntityExistError;

public interface UserService {

	boolean exist(String username);

	void create(User user) throws EntityExistError;

	void changePassword(Long userId, String newPassword);

	User findByUsername(String username);

}
