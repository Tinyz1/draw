package com.asiainfo.draw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asiainfo.draw.service.RoomService;

/**
 * 房间创建
 * 
 * @author yecl
 *
 */
@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private RoomService roomService;

}
