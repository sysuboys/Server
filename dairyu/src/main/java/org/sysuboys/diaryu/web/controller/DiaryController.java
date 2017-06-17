package org.sysuboys.diaryu.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.business.model.Constant;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.service.IExchangeModelMap;
import org.sysuboys.diaryu.business.service.ILoginService;
import org.sysuboys.diaryu.business.service.IUserService;
import org.sysuboys.diaryu.exception.NoSuchUser;

@Controller
public class DiaryController {

	static Logger logger = Logger.getLogger(DiaryController.class);

	@Autowired
	IUserService userService;
	@Autowired
	ILoginService loginService;
	@Autowired
	IExchangeModelMap exchangeModelMap;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody JSONObject upload(HttpServletRequest request) {
		logger.debug("/upload POST");
		
		String title = request.getParameter("title");
		String body = request.getParameter("body");

		String sessionid = (String) request.getSession().getAttribute(Constant.sessionid);
		if (sessionid == null)
			throw new RuntimeException("can't find sessionid");  // TODO
		String username = loginService.getUsername(sessionid);
		User user = userService.findByUsername(username);
		if (user == null)
			throw new NoSuchUser("username: " + username);
		
		logger.debug("upload: username: " + username + ", title: " + title);

		Diary diary = new Diary(user, title, body);
		userService.addDiary(diary);

		JSONObject object = new JSONObject();
		object.put("success", true);
		object.put("title", title);
		return object;
	}

	@RequestMapping("/getFile")
	public @ResponseBody JSONObject getFile(HttpServletRequest request) {
		logger.debug("/getFile GET");
		
		String sessionid = (String) request.getSession().getAttribute(Constant.sessionid);
		if (sessionid == null)
			throw new RuntimeException("can't find sessionid");  // TODO
		String username = loginService.getUsername(sessionid);

		JSONObject object = new JSONObject();
		ExchangeModel exchangeModel = exchangeModelMap.get().get(username);
		if (exchangeModel == null) {
			object.put("success", false);
			object.put("error", "you didn't exchange with someone");
		} else if (!exchangeModel.isMatched()) {
			object.put("success", false);
			object.put("error", "you hasn't match with your friend");
		} else {
			object.put("success", true);
			String title = exchangeModel.getFriendTitle(username);
			Diary diary = userService.findDiaryByUsernameAndTitle(username, title);
			object.put("title", title);
			object.put("body", diary.getBody());
			exchangeModelMap.get().remove(username);
		}
		return object;
	}

}
