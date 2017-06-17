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
import org.sysuboys.diaryu.business.service.IFriendshipService;
import org.sysuboys.diaryu.business.service.ILoginService;
import org.sysuboys.diaryu.business.service.IUserService;

@Controller
public class LoginController {

	static Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	IUserService userService;
	@Autowired
	ILoginService loginService;
	@Autowired
	IFriendshipService friendshipService;

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
		logger.debug("username: " + username + ", password: " + password + ", rememberMe: " + rememberMe);

		String sessionid = loginService.login(username, password);
		String error = null;
		if (sessionid == null)
			error = "wrong username or password";
		else
			request.getSession().setAttribute(Constant.sessionid, sessionid);

		JSONObject object = new JSONObject();
		if (error != null) {
			object.put("success", false);
			object.put("error", error);
		} else {
			object.put("success", true);
			object.put("username", username);
			object.put("friends", friendshipService.findFriends(username));
		}
		return object.toString();
	}

}
