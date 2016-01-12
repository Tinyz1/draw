package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.persistence.WinningRecordMapper;
import com.asiainfo.draw.service.RecordService;

@Service("recordService")
@Transactional
public class RecordServiceImpl implements RecordService {

	private final Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);

	@Autowired
	private WinningRecordMapper recordMapper;

	@Override
	public void saveRecord(WinningRecord record) {
		checkNotNull(record);
		logger.info("<<-----------±£´æÖÐ½±¼ÇÂ¼£º{}" + record);
		record.setCreateDate(new Date());
		recordMapper.insert(record);
	}

}
