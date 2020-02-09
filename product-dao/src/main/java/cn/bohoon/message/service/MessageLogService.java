package cn.bohoon.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageLogDao;
import cn.bohoon.message.entity.MessageLog;

@Service
@Transactional
public class MessageLogService extends BaseService<MessageLog, Integer> {

	@Autowired
	MessageLogDao messageLogDao;
 
	@Autowired
	MessageLogService(MessageLogDao dao) {
		super(dao);
	}
	
}
