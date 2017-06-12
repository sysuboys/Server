package org.sysuboys.diaryu.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.business.service.ISessionService;
import org.sysuboys.diaryu.business.service.IUserService;

@Controller
public class DiaryController {

	@Autowired
	IUserService userService;
	@Autowired
	ISessionService sessionService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody Body upload(HttpServletRequest request) {
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userService.findByUsername(username);
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		Diary diary = new Diary(user, title, body);
		userService.addDiary(diary);
		return new Body(true, title);
	}

	@RequestMapping("/getFile")
	public String getFile() {
		// TODO
		return "";
	}

	class Body {
		boolean success;
		String message = null;

		public Body(boolean success, String message) {
			this.success = success;
			this.message = message;
		}
	}

}
