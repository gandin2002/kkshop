package cn.bohoon.interfaces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.interfaces.dao.SyncDataLogDao;
import cn.bohoon.interfaces.entity.SyncDataLog;

@Service
@Transactional
public class SyncDataLogService extends BaseService<SyncDataLog,Integer>{

	@Autowired
	SyncDataLogDao syncDataLogDao;

    @Autowired
    SyncDataLogService(SyncDataLogDao syncDataLogDao){
        super(syncDataLogDao);
    }
    
    
}
