package com.asiainfo.draw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.domain.Test;
import com.asiainfo.draw.persistence.TestMapper;
import com.asiainfo.draw.service.TestService;

@Service("testService")
@Transactional
public class TestServiceImpl implements TestService {
	
	@Autowired
	private TestMapper testMapper;

	@Override
	public void save(Test test) {
		testMapper.insert(test);
	}

}
