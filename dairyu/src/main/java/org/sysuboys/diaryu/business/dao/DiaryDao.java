package org.sysuboys.diaryu.business.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.sysuboys.diaryu.business.entity.Diary;

@Repository
public class DiaryDao extends AbstractJpaDAO<Diary> implements IDiaryDao {

	@SuppressWarnings("unchecked")
	public Diary findByUserIdAndTitle(long user_id, String title) {
		String sql = "select id, user_id, title, body from diarys d where user_id = :user_id and title = :title";
		Query query = entityManager.createNativeQuery(sql, Diary.class);
		query.setParameter("user_id", user_id);
		query.setParameter("title", title);
		List<Diary> list = query.getResultList();
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

}
