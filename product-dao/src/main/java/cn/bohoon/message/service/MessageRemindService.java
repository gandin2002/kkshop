package cn.bohoon.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageRemindDao;
import cn.bohoon.message.entity.MessageRemind;

@Service
@Transactional
public class MessageRemindService extends BaseService<MessageRemind,Integer>{

	@Autowired
	MessageRemindDao messageRemindDao;

    @Autowired
    MessageRemindService(MessageRemindDao messageRemindDao){
        super(messageRemindDao);
    }
    
    public MessageRemind getMessageRemind(String code){
    	return  messageRemindDao.select(" from MessageRemind where code = ? ", code);
    }

}
