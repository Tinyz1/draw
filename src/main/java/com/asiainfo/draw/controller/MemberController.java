package com.asiainfo.draw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.service.LinkMemberService;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private LinkMemberService memberService;

	@RequestMapping(value = "/link/{linkId}", method = RequestMethod.GET)
	@ResponseBody
	public List<LinkMember> getMemberByLinkAndState(@PathVariable Integer linkId) {
		return memberService.getMemberByLinkIdAndState(linkId, null);
	}
}
