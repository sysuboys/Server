package org.sysuboys.diaryu.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {

	@RequestMapping("/logout")
	public String get(HttpServletRequest request) {
		SecurityUtils.getSubject().logout();
		return "logout";
	}

}
