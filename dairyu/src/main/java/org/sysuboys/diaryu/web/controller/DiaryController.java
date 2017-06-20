package org.sysuboys.diaryu.web.controller;

import java.io.File;
import java.io.IOException;

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
import org.sysuboys.diaryu.business.service.IExchangeModelService;
import org.sysuboys.diaryu.business.service.IDiaryFileService;
import org.sysuboys.diaryu.business.service.ILoginService;
import org.sysuboys.diaryu.business.service.IUserService;
import org.sysuboys.diaryu.exception.ClientError;
import org.sysuboys.diaryu.exception.NoSuchUser;
import org.sysuboys.diaryu.exception.ServerError;

@Controller
public class DiaryController {

	static Logger logger = Logger.getLogger(DiaryController.class);

	@Autowired
	IDiaryFileService diaryFileService;
	@Autowired
	IUserService userService;
	@Autowired
	ILoginService loginService;
	@Autowired
	IExchangeModelService exchangeModelMap;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody JSONObject upload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			@RequestParam("title") String title) {

		logger.debug("/upload POST");

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

			logger.info("user " + username + " upload diary " + title);

			Diary diary = new Diary(user, title);
			try {
				String filename = diary.getFilename();
				diaryFileService.create(filename, file.getInputStream());
				logger.info(username + "'s diary " + title + " saved in " + filename);
			} catch (IOException e) {
				throw new ServerError("IOException: " + e.getMessage());
			}
			userService.addDiary(diary);

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
		return object;

	}

	@RequestMapping("/getFile")
	public @ResponseBody ResponseEntity<byte[]> getFile(HttpServletRequest request) {

		logger.debug("/getFile GET");

		HttpHeaders headers = new HttpHeaders();
		try {

			String sessionid = (String) request.getSession().getAttribute(Constant.sessionid);
			if (sessionid == null)
				throw new ClientError("can not find " + Constant.sessionid);
			String username = loginService.getUsername(sessionid);
			if (username == null)
				throw new ClientError("unrecognized " + Constant.sessionid);

			ExchangeModel exchangeModel = exchangeModelMap.get().get(username);
			if (exchangeModel == null || !exchangeModel.isMatched())
				throw new ClientError("you have no exchange");

			String title = exchangeModel.getFriendTitle(username);
			Diary diary;
			try {
				diary = userService.findDiaryByUsernameAndTitle(username, title);
			} catch (NoSuchUser e) {
				throw new ServerError("can not find user by username=" + username);
			}
			exchangeModelMap.get().remove(username);

			File file = diaryFileService.get(diary.getFilename());
			// String dfileName = new String(fileName.getBytes("gb2312"),
			// "iso8859-1");
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", title);
			try {
				return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
			} catch (IOException e) {
				throw new ServerError("IOException: " + e.getMessage());
			}
		} catch (ClientError e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<byte[]>(new byte[0], headers, HttpStatus.FORBIDDEN);
		} catch (ServerError e) {
			logger.error(e.getMessage());
			return new ResponseEntity<byte[]>(new byte[0], headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
