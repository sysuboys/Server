package org.sysuboys.diaryu.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sysuboys.diaryu.business.dao.FriendshipDao;
import org.sysuboys.diaryu.business.dao.UserDao;
import org.sysuboys.diaryu.business.entity.Friendship;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.exception.EntityExistError;
import org.sysuboys.diaryu.exception.NoSuchUser;

@Service
public class FriendshipServiceImpl implements FriendshipService {

	@Autowired
	UserDao userDao;
	@Autowired
	FriendshipDao friendshipDao;

	Map<String, List<String>> map = new ConcurrentHashMap<String, List<String>>();

	public boolean registerRequest(String from, String to) {
		List<String> list = getRequests(from);
		synchronized (list) {
			if (list.contains(to))
				return false;
			list.add(to);
		}
		return true;
	}

	public boolean removeRequest(String from, String to) {
		List<String> list = getRequests(from);
		synchronized (list) {
			return list.remove(to);
		}
	}

	List<String> getRequests(String username) {
		List<String> _map = map.get(username);
		if (_map == null) {
			_map = new ArrayList<String>();
			map.put(username, _map);
		}
		return _map;
	}

	@Transactional
	public List<String> findFriends(String username) throws NoSuchUser {
		User user = userDao.findByUsername(username);
		if (user == null)
			throw new NoSuchUser(username);
		List<Long> ids = friendshipDao.findFriends(user.getId());
		List<String> friends = new ArrayList<String>();
		for (Long userId : ids)
			friends.add(userDao.findOne(userId).getUsername());
		return friends;
	}

	@Transactional
	public void makeFriend(String username1, String username2) throws NoSuchUser, EntityExistError {
		User user1 = userDao.findByUsername(username1);
		if (user1 == null)
			throw new NoSuchUser(username1);
		User user2 = userDao.findByUsername(username2);
		if (user2 == null)
			throw new NoSuchUser(username2);
		if (friendshipDao.isFriend(user1.getId(), user2.getId()))
			throw new EntityExistError(Friendship.class.getName());
		Friendship friendship1 = new Friendship(user1.getId(), user2.getId());
		friendshipDao.create(friendship1);
		Friendship friendship2 = new Friendship(user2.getId(), user1.getId());
		friendshipDao.create(friendship2);
	}

}
