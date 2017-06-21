package org.sysuboys.diaryu.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sysuboys.diaryu.business.model.Constant;
import org.sysuboys.diaryu.business.service.FriendshipService;
import org.sysuboys.diaryu.business.service.LoginService;
import org.sysuboys.diaryu.business.service.UserService;
import org.sysuboys.diaryu.exception.ClientError;
import org.sysuboys.diaryu.exception.NoSuchUser;
import org.sysuboys.diaryu.exception.ServerError;

@Controller
public class LoginController {

	static Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	UserService userService;
	@Autowired
	LoginService loginService;
	@Autowired
	FriendshipService friendshipService;

	// 保留的HTTP登录界面
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String get() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected @ResponseBody String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");
		logger.info("username: " + username + ", password: " + password + ", rememberMe: " + rememberMe);

		JSONObject object = new JSONObject();
		try {

			String sessionid = loginService.login(username, password);
			if (sessionid == null)
				throw new ClientError("wrong username or password");
			request.getSession().setAttribute(Constant.sessionid, sessionid);

			logger.info(username + " login with sessionid=" + sessionid);

			object.put("success", true);
			object.put("username", username);
			try {
				object.put("friends", friendshipService.findFriends(username));
			} catch (NoSuchUser e) {
				throw new ServerError("can not find user " + username + " after login");
			}

		} catch (ClientError e) {
			logger.warn(e.getMessage());
			object.put("success", false);
			object.put("error", e.getMessage());
		} catch (ServerError e) {
			logger.error(e.getMessage());
			object.put("success", false);
			object.put("error", "server error");
		}
		return object.toString();
	}

}
