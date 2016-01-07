package com.asiainfo.draw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asiainfo.draw.service.PrizeService;

/**
 * 奖品相关控制类
 * 
 * @author yecl
 *
 */
@Controller
@RequestMapping("/prize")
public class PrizeController {
	@Autowired
	private PrizeService prizeService;

}
