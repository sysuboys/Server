package org.sysuboys.diaryu.business.service;

import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.exception.NoSuchUser;

public interface IUserService {

	boolean exist(String username);

	void create(User user);

	boolean checkPassword(String username, String password);

	void changePassword(Long userId, String newPassword);

	User findByUsername(String username);

	void addDiary(Diary diary);

	Diary findDiaryByUsernameAndTitle(String username, String title) throws NoSuchUser;

}
