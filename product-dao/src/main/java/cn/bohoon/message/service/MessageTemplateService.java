package cn.bohoon.message.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageTemplateDao;
import cn.bohoon.message.domain.CacheManager;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.domain.SendType;
import cn.bohoon.message.entity.MessageTemplate;
import cn.bohoon.util.Contexts;

@Service
@Transactional
public class MessageTemplateService extends BaseService<MessageTemplate, Integer> {

	@Autowired
	MessageTemplateDao messageTemplateDao;
	
	private CacheManager<List<MessageTemplate>> cacheManager; 

	@Autowired
	MessageTemplateService(MessageTemplateDao messageTemplateDao) {
		super(messageTemplateDao);
	}

	public void initData(){
		List<MessageTemplate> listSms = list("from MessageTemplate ");
		for(MessageTemplate mt : listSms){
			if(MessageType.Sms.equals(mt.getType())){
				Contexts.SMS_TEMPLATES.put(mt.getSendType(), mt.getContent());
			}
		}
		cacheManager = new CacheManager<List<MessageTemplate>>();
	}

	public List<MessageTemplate> findTemplate(SendType sendType) {
		List<MessageTemplate> mtlist= this.list(" from  MessageTemplate where sendType = ? and state = 1  ", sendType);
		return mtlist;
	}
	
	public List<MessageTemplate> findTemplate(SendType sendType,MessageType messageType){
		List<MessageTemplate> mtlist= this.list(" from  MessageTemplate where type = ? and sendType = ? and state = 1 ", messageType,sendType);
		return mtlist;
	}

	public MessageTemplate update(MessageTemplate entity) {
		return super.update(entity);
	}
	
	public void delete(Integer id) {
		super.delete(id);
	}
	
	public void delete(MessageTemplate entity) {
		super.delete(entity);
	}
	public void save(MessageTemplate entity) {
		super.save(entity);
	}
}
