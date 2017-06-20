package org.sysuboys.diaryu.business.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.sysuboys.diaryu.business.entity.Diary;

@Repository
public class DiaryDaoImpl extends AbstractJpaDAO<Diary> implements DiaryDao {

	@SuppressWarnings("unchecked")
	public Diary findByUserIdAndTitle(long user_id, String title) {
		String sql = "select id, user_id, title, filename from diarys d where user_id = :user_id and title = :title";
		Query query = entityManager.createNativeQuery(sql, Diary.class);
		query.setParameter("user_id", user_id);
		query.setParameter("title", title);
		List<Diary> list = query.getResultList();
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<String> findTitlesByUserId(long user_id) {
		String sql = "select d.title from diarys d where d.user_id = :user_id";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("user_id", user_id);
		return query.getResultList();
	}

}
