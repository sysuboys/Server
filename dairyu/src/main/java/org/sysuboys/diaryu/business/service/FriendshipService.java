package org.sysuboys.diaryu.business.service;

import java.util.List;

import org.sysuboys.diaryu.exception.EntityExistError;
import org.sysuboys.diaryu.exception.NoSuchUser;

public interface FriendshipService {

	List<String> findFriends(String username) throws NoSuchUser;

	void makeFriend(String username1, String username2) throws NoSuchUser, EntityExistError;

}
