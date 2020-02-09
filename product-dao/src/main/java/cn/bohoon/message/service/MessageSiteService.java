package cn.bohoon.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageSiteDao;
import cn.bohoon.message.entity.MessageSite;

@Service
@Transactional
public class MessageSiteService extends BaseService<MessageSite,Integer>{

	@Autowired
	MessageSiteDao messageSiteDao;

    @Autowired
    MessageSiteService(MessageSiteDao messageSiteDao){
        super(messageSiteDao);
    }

}
