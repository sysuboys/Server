package org.sysuboys.diaryu.business.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sysuboys.diaryu.business.dao.UserDao;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.util.SecurityUtil;;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	UserDao userDao;

	Map<String, String> sessionids = new HashMap<String, String>();
	Map<String, String> users = new HashMap<String, String>();

	public String login(String username, String password) {
		User user = userDao.findByUsername(username);
		if (user == null || !user.checkPassword(password))
			return null;
		String sessionid = SecurityUtil.generate32();
		sessionids.put(username, sessionid);
		users.put(sessionid, username);
		return sessionid;
	}

	public String getUsername(String sessionid) {
		return users.get(sessionid);
	}

	public String logout(String sessionid) {
		String username = users.remove(sessionid);
		sessionids.remove(username);
		return username;
	}

}
