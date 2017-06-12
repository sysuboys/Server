package org.sysuboys.diaryu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DiaryController {

	@RequestMapping("/upload")
	public String upload() {
		return "";
	}

	@RequestMapping("/getFile")
	public String getFile() {
		return "";
	}

}
