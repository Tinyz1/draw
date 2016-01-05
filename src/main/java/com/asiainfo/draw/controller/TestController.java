package com.asiainfo.draw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.draw.domain.Test;
import com.asiainfo.draw.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private TestService testService;

	@RequestMapping("/hello")
	public Test main() {
		Test test = new Test();
		test.settName("hello world!");
		testService.save(test);
		return test;
	}

}
