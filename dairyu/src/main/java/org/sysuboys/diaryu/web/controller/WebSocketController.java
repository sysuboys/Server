package org.sysuboys.diaryu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSocketController {

	@RequestMapping("/websocket")
	public String get() {
		return "websocket";
	}
}
