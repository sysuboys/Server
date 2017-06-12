package org.sysuboys.diaryu.business.service;

import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;

public interface IUserService {

	boolean exist(String username);

	void create(User user);
	
	boolean checkPassword(String username, String password);

	void changePassword(Long userId, String newPassword);

	User findByUsername(String username);

	void addDiary(Diary diary);

}
