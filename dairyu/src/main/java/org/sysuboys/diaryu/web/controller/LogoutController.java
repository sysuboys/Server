package org.sysuboys.diaryu.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.sysuboys.diaryu.business.model.Constant;
import org.sysuboys.diaryu.business.service.ILoginService;

@Controller
public class LogoutController {

	@Autowired
	ILoginService loginService;
	
	@RequestMapping("/logout")
	public String get(HttpServletRequest request) {
		String sessionid = (String) request.getSession().getAttribute(Constant.sessionid);
		request.getSession().removeAttribute(Constant.sessionid);
		loginService.logout(sessionid);
		return "logout";
	}

}
