package com.mit.log.service.impl;

import com.mit.log.model.SysLog;
import com.mit.datasource.annotation.DataSource;
import com.mit.log.dao.LogDao;
import com.mit.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 切换数据源，存储log-center
 */
@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;

	@Async
	@Override
	@DataSource(name= "log")
	public void save(SysLog log) {
		if (log.getCreateTime() == null) {
			log.setCreateTime(new Date());
		}
		if (log.getFlag() == null) {
			log.setFlag(Boolean.TRUE);
		}
		logDao.insert(log);
	}
}
