package cn.bohoon.interfaces.dao;

import org.springframework.stereotype.Repository;

import cn.bohoon.framework.orm.jpa.AbstractDao;
import cn.bohoon.interfaces.entity.SyncDataLog;


@Repository
public class SyncDataLogDao extends AbstractDao<SyncDataLog,Integer>{

}
