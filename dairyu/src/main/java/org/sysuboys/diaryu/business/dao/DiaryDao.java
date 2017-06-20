package org.sysuboys.diaryu.business.dao;

import java.util.List;

import org.sysuboys.diaryu.business.entity.Diary;

public interface DiaryDao {

	void create(Diary diary);

	Diary update(Diary diary);

	void delete(long id);

	Diary findOne(long id);

	Diary findByUserIdAndTitle(long id, String title);
	
	List<String> findTitlesByUserId(long user_id);

}
