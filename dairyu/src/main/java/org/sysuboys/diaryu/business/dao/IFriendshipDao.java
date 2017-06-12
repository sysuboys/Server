package org.sysuboys.diaryu.business.dao;

import java.util.List;

import org.sysuboys.diaryu.business.entity.Friendship;

public interface IFriendshipDao {

	void create(Friendship friendship);

	Friendship update(Friendship friendship);

	void delete(long friendshipId);

	Friendship findOne(long friendshipId);

	List<Long> findFriends(long userId);

}
