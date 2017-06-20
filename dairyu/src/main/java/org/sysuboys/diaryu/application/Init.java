package org.sysuboys.diaryu.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.business.service.DiaryService;
import org.sysuboys.diaryu.business.service.FriendshipService;
import org.sysuboys.diaryu.business.service.UserService;
import org.sysuboys.diaryu.exception.EntityExistError;
import org.sysuboys.diaryu.exception.NoSuchUser;

public class Init {

	static Logger logger = Logger.getLogger(Init.class);

	@Autowired
	UserService userService;
	@Autowired
	DiaryService diaryService;
	@Autowired
	FriendshipService friendshipService;

	public void doInit() {
		logger.info("doInit begin....");

		try {

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
			logger.debug("b has friends: " + friendshipService.findFriends("b").size());
			assert (friendshipService.findFriends("b").size() == 1);
			logger.debug("a has friend b: " + friendshipService.findFriends("a").contains("b"));
			assert (friendshipService.findFriends("a").contains("b") == true);

			try {
				File file1 = new File("./sample_diary/1.html");
				diaryService.create(new Diary(a, "1.html"), new FileInputStream(file1));
				logger.debug("a add a diary with title \"1.html\"");

				File file2 = new File("./sample_diary/2.html");
				diaryService.create(new Diary(b, "2.html"), new FileInputStream(file2));
				logger.debug("b add a diary with title \"2.html\"");

				assert (diaryService.findAllTitle("a").size() == 1);
			} catch (FileNotFoundException e) {
				logger.error("can't find sample diary");
			} catch (IOException e) {
				logger.error("IOException while reading sample diary");
			}

		} catch (NoSuchUser e) {
			logger.error("NoSuchUser in initialize");
		} catch (EntityExistError e) {
			logger.error("EntityExistError, may be database was initialized and not dropped");
		}

		logger.info("doInit end....");
	}

}
