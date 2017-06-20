package org.sysuboys.diaryu.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sysuboys.diaryu.business.dao.UserDao;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.exception.EntityExistError;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	public boolean exist(String username) {
		return userDao.findByUsername(username) != null;
	}

	@Transactional
	public void create(User user) throws EntityExistError {
		if (exist(user.getUsername()))
			throw new EntityExistError(User.class.getName());
		userDao.create(user);
	}

	@Transactional
	public void changePassword(Long userId, String newPassword) {
		User user = userDao.findOne(userId);
		user.setPassword(newPassword);
		userDao.update(user);
	}

	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

}
