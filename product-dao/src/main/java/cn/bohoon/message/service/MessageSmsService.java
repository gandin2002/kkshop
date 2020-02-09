package cn.bohoon.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageSmsDao;
import cn.bohoon.message.entity.MessageSms;

@Service
@Transactional
public class MessageSmsService extends BaseService<MessageSms,Integer>{

	@Autowired
	MessageSmsDao messageSmsDao;

    @Autowired
    MessageSmsService(MessageSmsDao messageSmsDao){
        super(messageSmsDao);
    }
    
    
}
