package org.sysuboys.diaryu.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.service.IExchangeModelMap;
import org.sysuboys.diaryu.business.service.IUserService;
import org.sysuboys.diaryu.exception.NoSuchUser;

@Controller
public class DiaryController {

	@Autowired
	IUserService userService;
	@Autowired
	IExchangeModelMap exchangeModelMap;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody JSONObject upload(HttpServletRequest request) {
		String title = request.getParameter("title");
		String body = request.getParameter("body");

		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userService.findByUsername(username);
		if (user == null)
			throw new NoSuchUser("username: " + username);

		Diary diary = new Diary(user, title, body);
		userService.addDiary(diary);

		JSONObject object = new JSONObject();
		object.put("success", true);
		object.put("title", title);
		return object;
	}

	@RequestMapping("/getFile")
	public @ResponseBody JSONObject getFile() {
		String username = (String) SecurityUtils.getSubject().getPrincipal();

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
