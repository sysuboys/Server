package org.sysuboys.diaryu.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sysuboys.diaryu.business.service.IFriendshipService;
import org.sysuboys.diaryu.business.service.IUserService;

@Controller
public class LoginController {

	static Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	IUserService userService;
	@Autowired
	IFriendshipService friendshipService;

	// 只是保留测试。
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public @ResponseBody Body get() {
		return new Body();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected @ResponseBody Body login(HttpServletRequest request, HttpServletResponse response, Model model) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");

		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe != null);
		String error = null;

		try {
			subject.login(token);
		} catch (UnknownAccountException e) {
			error = "wrong username";
		} catch (IncorrectCredentialsException e) {
			error = "wrong password";
		} catch (AuthenticationException e) {
			error = "server error";
		}

		Body body = new Body();
		if (error != null) {
			body.error = error;
			return body;
		}
		body.username = username;
		body.friends = friendshipService.findFriends(username);
		return body;
	}

	class Body {
		public boolean success = false;
		public String error = null;
		public String username = null;
		public List<String> friends = new ArrayList<String>();
	}

}
