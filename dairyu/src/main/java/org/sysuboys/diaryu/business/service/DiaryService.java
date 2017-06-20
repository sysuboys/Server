package org.sysuboys.diaryu.business.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.exception.EntityExistError;
import org.sysuboys.diaryu.exception.NoSuchUser;

public interface DiaryService {

	File getFile(String filename);

	void create(Diary diary, InputStream in) throws IOException, EntityExistError;

	Diary findByUsernameAndTitle(String username, String title) throws NoSuchUser;
	
	List<String> findAllTitle(String username) throws NoSuchUser;

}
