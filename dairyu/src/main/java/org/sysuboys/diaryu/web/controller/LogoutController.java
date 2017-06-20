package org.sysuboys.diaryu.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.sysuboys.diaryu.business.model.Constant;
import org.sysuboys.diaryu.business.service.LoginService;

@Controller
public class LogoutController {

	static Logger logger = Logger.getLogger(LogoutController.class);

	@Autowired
	LoginService loginService;

	@RequestMapping("/logout")
	public String get(HttpServletRequest request) {
		String sessionid = (String) request.getSession().getAttribute(Constant.sessionid);
		request.getSession().removeAttribute(Constant.sessionid);
		String username = loginService.logout(sessionid);
		logger.info(username + " logout");
		return "logout";
	}

}
