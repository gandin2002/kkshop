package cn.bohoon.message.dao;

import org.springframework.stereotype.Repository;

import cn.bohoon.framework.orm.jpa.AbstractDao;
import cn.bohoon.message.entity.MessageLog;

@Repository
public class MessageLogDao extends AbstractDao<MessageLog,Integer> {

}
