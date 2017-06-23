package org.sysuboys.diaryu.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.sysuboys.diaryu.business.entity.Diary;
import org.sysuboys.diaryu.business.entity.User;
import org.sysuboys.diaryu.business.model.Constant;
import org.sysuboys.diaryu.business.model.ExchangeModel;
import org.sysuboys.diaryu.business.service.ExchangeModelService;
import org.sysuboys.diaryu.business.service.DiaryService;
import org.sysuboys.diaryu.business.service.LoginService;
import org.sysuboys.diaryu.business.service.UserService;
import org.sysuboys.diaryu.exception.ClientError;
import org.sysuboys.diaryu.exception.EntityExistError;
import org.sysuboys.diaryu.exception.NoSuchUser;
import org.sysuboys.diaryu.exception.ServerError;

@Controller
public class DiaryController {

	static Logger logger = Logger.getLogger(DiaryController.class);

	@Autowired
	DiaryService diaryService;
	@Autowired
	UserService userService;
	@Autowired
	LoginService loginService;
	@Autowired
	ExchangeModelService exchangeModelMap;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {

		logger.debug("/upload POST");
		logger.debug("encoding: " + request.getCharacterEncoding());

		JSONObject object = new JSONObject();
		try {

			String sessionid = (String) request.getSession().getAttribute(Constant.sessionid);
			if (sessionid == null)
				throw new ClientError("can not find " + Constant.sessionid);
			String username = loginService.getUsername(sessionid);
			if (username == null)
				throw new ClientError("unrecognized " + Constant.sessionid);

			User user = userService.findByUsername(username);
			if (user == null)
				throw new ServerError("can not find user by username=" + username);

			String title = file.getOriginalFilename();
			logger.info("user " + username + " upload diary " + title);

			Diary diary = new Diary(user, title);
			try {
				diaryService.create(diary, file.getInputStream());
				logger.info(username + "'s diary " + title + " saved in " + diary.getFilename());
			} catch (IOException e) {
				throw new ServerError("IOException: " + e.getMessage());
			} catch (EntityExistError e) {
				throw new ClientError("diary exist:" + title);
			}

			object.put("success", true);
			object.put("title", title);

		} catch (ClientError e) {
			logger.warn(e.getMessage());
			object.put("success", false);
			object.put("error", e.getMessage());
		} catch (ServerError e) {
			logger.error(e.getMessage());
			object.put("success", false);
			object.put("error", "server error");
		}
		return object.toString();

	}

	@RequestMapping("/getFile")
	public @ResponseBody ResponseEntity<byte[]> getFile(HttpServletRequest request) {

		logger.debug("/getFile GET");

		String username = null;
		HttpHeaders headers = new HttpHeaders();
		try {

			String sessionid = (String) request.getSession().getAttribute(Constant.sessionid);
			if (sessionid == null)
				throw new ClientError("can not find " + Constant.sessionid);
			username = loginService.getUsername(sessionid);
			if (username == null)
				throw new ClientError("unrecognized " + Constant.sessionid);

			ExchangeModel exchangeModel = exchangeModelMap.get().get(username);
			if (exchangeModel == null || !exchangeModel.isMatched())
				throw new ClientError("you have no exchange");

			String title = exchangeModel.getFriendTitle(username);
			String friend = exchangeModel.getAnother(username);
			logger.info("[" + username + "] will get [" + friend + "] diary [" + title + "]");
			Diary diary;
			try {
				diary = diaryService.findByUsernameAndTitle(friend, title);
				if (diary == null)
					throw new ServerError("can't find diary after exchanged");
			} catch (NoSuchUser e) {
				throw new ServerError("can not find user by username=" + username);
			}
			exchangeModelMap.get().remove(username);

			File file = diaryService.getFile(diary.getFilename());
			String dfilename;
			try {
				dfilename = new String(title.getBytes("utf-8"), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				throw new ServerError("UnsupportedEncodingException: " + e.getMessage());
			}
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", dfilename);
			try {
				return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
			} catch (IOException e) {
				throw new ServerError("IOException: " + e.getMessage());
			}
		} catch (ClientError e) { // TODO 暂时没有返回错误信息的方法
			logger.warn("[" + username + "] " + e.getMessage());
			return new ResponseEntity<byte[]>(new byte[0], headers, HttpStatus.FORBIDDEN);
		} catch (ServerError e) {
			logger.error("[" + username + "] " + e.getMessage());
			return new ResponseEntity<byte[]>(new byte[0], headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
