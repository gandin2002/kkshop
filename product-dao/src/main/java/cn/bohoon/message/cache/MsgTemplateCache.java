package cn.bohoon.message.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.domain.SendType;
import cn.bohoon.message.entity.MessageTemplate;
import cn.bohoon.message.service.MessageTemplateService;

public class MsgTemplateCache {

	private static MsgTemplateCache cache ;
	
	private List<MessageTemplate> sitMsgTemplateList = null ;
	private Map<SendType, MessageTemplate> sitMsgTemplateMap = null ;
	
	private MsgTemplateCache(){
		init() ;
	}


	
	public static MsgTemplateCache getCache(){
		if(null == cache) {
			cache = new MsgTemplateCache() ;
		}
		return cache ;
	}
	
	public void init() {
		sitMsgTemplateMap = new HashMap<SendType, MessageTemplate>() ;
		MessageTemplateService messageTemplateService = SpringContextHolder.getBean(MessageTemplateService.class) ;
		sitMsgTemplateList = messageTemplateService.list(" from MessageTemplate where type = ? ", MessageType.Site);
		for(MessageTemplate msgt :sitMsgTemplateList) {
			sitMsgTemplateMap.put(msgt.getSendType(), msgt) ;
		}
	}
	
	/**
	 * 获取站内消息模板
	 * 
	 * @param st
	 * @return
	 */
	public String getSitMsgTemplateCont(SendType st) {
		MessageTemplate msgt = sitMsgTemplateMap.get(st) ;
		if(StringUtils.isEmpty(msgt)) {
			init() ;
			msgt = sitMsgTemplateMap.get(st) ;
		}
		String cont =  null != msgt ? msgt.getContent() :"" ;
		return cont ;
	}
}
