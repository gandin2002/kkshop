package cn.bohoon.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageEmailDao;
import cn.bohoon.message.entity.MessageEmail;

@Service
@Transactional
public class MessageEmailService extends BaseService<MessageEmail,Integer>{

	@Autowired
	MessageEmailDao messageEmailDao;

    @Autowired
    MessageEmailService(MessageEmailDao messageEmailDao){
        super(messageEmailDao);
    }
    
    
}
