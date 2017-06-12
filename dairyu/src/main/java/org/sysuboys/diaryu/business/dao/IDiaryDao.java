package org.sysuboys.diaryu.business.dao;

import org.sysuboys.diaryu.business.entity.Diary;

public interface IDiaryDao {

	void create(Diary diary);

	Diary update(Diary diary);

	void delete(long id);

	Diary findOne(long id);

	Diary findByUserIdAndTitle(long id, String title);

}
