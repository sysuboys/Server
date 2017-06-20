package org.sysuboys.diaryu.web.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.business.service.UserService;
import org.sysuboys.diaryu.exception.EntityExistError;

@Controller
public class SignupController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String get() {
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	protected String login(HttpServletRequest request, Model model) throws ServletException, IOException {
		// TODO 用户名合法性和密码安全性

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = new User(username, password);
		try {
			userService.create(user);
		} catch (EntityExistError e) {
			model.addAttribute("msg", "username already exists");
			return "signup";
		}

		return "redirect:/login";
	}
}
