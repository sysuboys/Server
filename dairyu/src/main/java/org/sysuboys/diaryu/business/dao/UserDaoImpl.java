package org.sysuboys.diaryu.business.dao;

import org.springframework.stereotype.Repository;
import org.sysuboys.diaryu.business.entity.User;

import java.util.List;

import javax.persistence.Query;

@Repository
public class UserDaoImpl extends AbstractJpaDAO<User> implements UserDao {

	public UserDaoImpl() {
		super();
		setClazz(User.class);
	}

	@SuppressWarnings("unchecked")
	public User findByUsername(String username) {
		String sql = "select id, username, password, salt from users u where username = :username";
		Query query = entityManager.createNativeQuery(sql, User.class);
		query.setParameter("username", username);
		List<User> list = query.getResultList();
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

}
