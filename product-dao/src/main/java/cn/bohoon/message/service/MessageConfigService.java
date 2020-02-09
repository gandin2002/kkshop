package cn.bohoon.message.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageConfigDao;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.entity.MessageConfig;

@Service
@Transactional
public class MessageConfigService extends BaseService<MessageConfig,Integer>{

	@Autowired
	MessageConfigDao messageConfigDao;
	
	@Autowired
	SmsService smsService;

    @Autowired
    MessageConfigService(MessageConfigDao messageConfigDao){
        super(messageConfigDao);
    }
    
    public  MessageConfig getMessageConfig(MessageType type){
    	MessageConfig messageConfig = messageConfigDao.select(" from MessageConfig where type = ? ",type);
    	return messageConfig;
    }
    
    public void save(MessageConfig messageConfig ){
    	messageConfigDao.save(messageConfig);
    }
    
    public void initData(){
    	List<MessageConfig> list = list("from MessageConfig");
    	for(MessageConfig mc:list){
    		if(MessageType.Sms.equals(mc.getType())){
    			smsService.initConfig(mc.getConfigJson());
    		}
    	}
    	
    }

}
