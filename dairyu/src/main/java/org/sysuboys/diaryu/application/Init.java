package org.sysuboys.diaryu.application;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.business.service.IFriendshipService;
import org.sysuboys.diaryu.business.service.IUserService;

public class Init {

	static Logger logger = Logger.getLogger(Init.class);

	@Autowired
	IUserService userService;
	@Autowired
	IFriendshipService friendshipService;

	public void doInit() {
		logger.debug("doInit begin....");

		User a = new User("a", "a");
		User b = new User("b", "b");
		userService.create(a);
		userService.create(b);
		userService.create(new User("c", "c"));
		logger.debug("created 3 users a, b, c with username as password");

		friendshipService.makeFriend("a", "b");
		friendshipService.makeFriend("a", "c");
		logger.debug("a and b, a and c become friend");

		logger.debug("a has friends: " + friendshipService.findFriends("a").size());
		assert (friendshipService.findFriends("a").size() == 2);

		userService.addDiary(new Diary(a, "atitle", "abody"));
		logger.debug("a add a diary with title \"atitle\"");
		userService.addDiary(new Diary(b, "btitle", "bbody"));
		logger.debug("b add a diary with title \"btitle\"");

		logger.debug("doInit end....");
	}

}
