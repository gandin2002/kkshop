package cn.bohoon.message.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageRemindInfoDao;
import cn.bohoon.message.entity.MessageRemind;
import cn.bohoon.message.entity.MessageRemindInfo;
 

@Service
@Transactional
public class MessageRemindInfoService extends BaseService<MessageRemindInfo,Integer> {

	@Autowired
	MessageRemindInfoDao messageRemindInfoDao;
	
	@Autowired
	MessageRemindService messageRemindService;
	

    @Autowired
    MessageRemindInfoService(MessageRemindInfoDao messageRemindInfoDao){
        super(messageRemindInfoDao);
    }
    //发送消息
	public void sendMessage(Integer id, String string) {
		MessageRemind messageRemind = messageRemindService.getMessageRemind(string);
		if(messageRemind.getState() == true){
			MessageRemindInfo messageRemindInfo = new MessageRemindInfo();
			messageRemindInfo.setTitle(messageRemind.getTitle());
			messageRemindInfo.setContent(String.format(messageRemind.getMsg(),id));
			messageRemindInfo.setCreateTime(new Date());
			messageRemindInfo.setMessageRemindId(messageRemind.getId());
			this.save(messageRemindInfo);
		}
	}
	
}
