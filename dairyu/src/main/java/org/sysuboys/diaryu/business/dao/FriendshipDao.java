package org.sysuboys.diaryu.business.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.sysuboys.diaryu.business.entity.Friendship;

@Repository
public class FriendshipDao extends AbstractJpaDAO<Friendship> implements IFriendshipDao {

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

}
