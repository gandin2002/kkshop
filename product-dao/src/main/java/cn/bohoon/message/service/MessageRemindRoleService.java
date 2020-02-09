package cn.bohoon.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.message.dao.MessageRemindRoleDao;
import cn.bohoon.message.entity.MessageRemindRole;

@Service
@Transactional
public class MessageRemindRoleService extends BaseService<MessageRemindRole,Integer> {

	@Autowired
	MessageRemindRoleDao messageRemindRoleDao;

    @Autowired
	MessageRemindRoleService(MessageRemindRoleDao messageRemindRoleDao) {
		super(messageRemindRoleDao);
	}

}
