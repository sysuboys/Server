package org.sysuboys.diaryu.business.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sysuboys.diaryu.business.dao.DiaryDao;
import org.sysuboys.diaryu.business.dao.UserDao;
import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.exception.EntityExistError;
import org.sysuboys.diaryu.exception.NoSuchUser;

@Service
public class DiaryServiceImpl implements DiaryService {

	@Autowired
	UserDao userDao;
	@Autowired
	DiaryDao diaryDao;

	static final String path = "." + File.separator + "diary" + File.separator;

	public File getFile(String filename) {
		return new File(path + filename);
	}

	@Transactional
	public void create(Diary diary, InputStream in) throws IOException, EntityExistError {
		if (diaryDao.findByUserIdAndTitle(diary.getUser().getId(), diary.getTitle()) != null)
			throw new EntityExistError(Diary.class.getName());
		FileUtils.copyInputStreamToFile(in, new File(path + diary.getFilename()));
		diaryDao.create(diary);
	}

	@Transactional
	public Diary findByUsernameAndTitle(String username, String title) throws NoSuchUser {
		User user = userDao.findByUsername(username);
		if (user == null)
			throw new NoSuchUser(username);
		return diaryDao.findByUserIdAndTitle(user.getId(), title);
	}

	@Transactional
	public List<String> findAllTitle(String username) throws NoSuchUser {
		User user = userDao.findByUsername(username);
		if (user == null)
			throw new NoSuchUser(username);
		return diaryDao.findTitlesByUserId(user.getId());
	}

}
