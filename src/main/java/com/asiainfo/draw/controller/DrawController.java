package com.asiainfo.draw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.service.DrawService;
import com.asiainfo.draw.util.Draw.Prize;

@Controller
@RequestMapping("/draw")
public class DrawController {

	@Autowired
	private DrawService drawService;

	@RequestMapping
	@ResponseBody
	public Prize pick() {

		return null;
	}

}
