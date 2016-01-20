package com.asiainfo.draw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.service.RecordService;

/**
 * ÖÐ½±¼ÇÂ¼¿ØÖÆÆ÷
 * 
 * @author yecl
 *
 */
@Controller
@RequestMapping("/record")
public class RecordController {

	@Autowired
	private RecordService recordService;

	@RequestMapping("/query")
	@ResponseBody
	public List<WinningRecord> add(String partcipantName, Integer linkId) {
		return recordService.getRecordByParticipantNameAndLinkId(partcipantName, linkId);
	}

}
