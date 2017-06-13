package org.sysuboys.diaryu.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sysuboys.diaryu.business.dao.IDiaryDao;
import org.sysuboys.diaryu.business.dao.IUserDao;
import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.exception.NoSuchUser;

@Service
public class UserService implements IUserService {

	@Autowired
	IUserDao userDao;
	@Autowired
	IDiaryDao diaryDao;

	public boolean exist(String username) {
		return userDao.findByUsername(username) != null;
	}

	@Transactional
	public void create(User user) {
		userDao.create(user);
	}

	public boolean checkPassword(String username, String password) {
		User user = userDao.findByUsername(username);
		return user != null && user.getPassword() == password;
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

	@Transactional
	public void addDiary(Diary diary) {
		diaryDao.create(diary);
	}

	public Diary findDiaryByUsernameAndTitle(String username, String title) {
		User user = findByUsername(username);
		if (user == null)
			throw new NoSuchUser("username: " + username);
		return diaryDao.findByUserIdAndTitle(user.getId(), title);
	}

}
