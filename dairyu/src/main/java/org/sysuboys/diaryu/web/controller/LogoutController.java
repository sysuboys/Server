package org.sysuboys.diaryu.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.sysuboys.diaryu.business.service.ISessionService;

@Controller
public class LogoutController {

	@Autowired
	ISessionService sessionService;

	@RequestMapping("/logout")
	public String get(HttpServletRequest request) {
		SecurityUtils.getSubject().logout();
		return "logout";
	}

}
