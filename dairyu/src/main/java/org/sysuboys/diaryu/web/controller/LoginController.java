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
		String msg = null;

		try {
			subject.login(token);
		} catch (UnknownAccountException e) {
			msg = "wrong username";
		} catch (IncorrectCredentialsException e) {
			msg = "wrong password";
		} catch (AuthenticationException e) {
			msg = "server error";
		}

		Body body = new Body();
		if (msg != null) {
			body.message = msg;
			return body;
		}
		body.name = username;
		body.friends = friendshipService.findFriends(username);
		return body;
	}

	class Body {
		public boolean success = false;
		public String message = null;
		public String name = null;
		public List<String> friends = new ArrayList<String>();
	}

}
