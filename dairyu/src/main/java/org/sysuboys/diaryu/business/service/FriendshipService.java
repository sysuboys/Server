package org.sysuboys.diaryu.business.service;

import java.util.List;

import org.sysuboys.diaryu.exception.EntityExistError;
import org.sysuboys.diaryu.exception.NoSuchUser;

public interface FriendshipService {

	/**
	 * 登记加好友请求
	 * @param from
	 * @param to
	 * @return 登记成功true，记录已存在false
	 */
	boolean registerRequest(String from, String to);
	
	/**
	 * 移除请求，接受或拒绝的时候使用
	 * @param from
	 * @param to
	 * @return 记录是否存在。
	 */
	boolean removeRequest(String from, String to);
	
	List<String> removeAllRequests(String to);

	List<String> findFriends(String username) throws NoSuchUser;

	void makeFriend(String username1, String username2) throws NoSuchUser, EntityExistError;

}
