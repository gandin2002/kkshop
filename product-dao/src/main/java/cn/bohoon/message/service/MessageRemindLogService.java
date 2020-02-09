package cn.bohoon.message.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageRemindLogDao;
import cn.bohoon.message.entity.MessageRemindLog;

@Service
@Transactional
public class MessageRemindLogService extends BaseService<MessageRemindLog,Integer> {

	@Autowired
	MessageRemindLogDao messageRemindLogDao;

    @Autowired
    MessageRemindLogService(MessageRemindLogDao messageRemindLogDao){
        super(messageRemindLogDao);
    }
	
    public List<Integer> findListMsgId(Integer oid){
    	List<MessageRemindLog> listlog = this.list(" from MessageRemindLog where operatorId = ? ", oid);
    	List<Integer> list = new ArrayList<>();
    	for (MessageRemindLog messageRemindLog : listlog) {
    		list.add(messageRemindLog.getMsgId());
		}
    	return list;
    }

	public List<MessageRemindLog> findMsg(Integer oId) {
		return  dao.findAll(" from MessageRemindLog where operatorId = ? ", oId);
	}

    
}
