package org.sysuboys.diaryu.business.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.sysuboys.diaryu.business.entity.Friendship;

@Repository
public class FriendshipDaoImpl extends AbstractJpaDAO<Friendship> implements FriendshipDao {

	@SuppressWarnings("unchecked")
	public List<Long> findFriends(long userId) {
		String sql = "select uid2 from friendships f where f.uid1 = :userId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("userId", userId);
		List<BigInteger> bi = query.getResultList();
		List<Long> list = new ArrayList<Long>();
		for (BigInteger i : bi)
			list.add(i.longValue());
		return list;
	}

	public boolean isFriend(long userId1, long userId2) {
		String sql = "select id, uid1, uid2 from friendships f where f.uid1 = :userId1 and f.uid2 = :userId2";
		Query query = entityManager.createNativeQuery(sql, Friendship.class);
		query.setParameter("userId1", userId1);
		query.setParameter("userId2", userId2);
		return !query.getResultList().isEmpty();
	}

}
