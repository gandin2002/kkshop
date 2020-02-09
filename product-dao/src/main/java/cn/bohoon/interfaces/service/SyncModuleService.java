package cn.bohoon.interfaces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.interfaces.dao.SyncModuleDao;
import cn.bohoon.interfaces.entity.SyncModule;

@Service
@Transactional
public class SyncModuleService extends BaseService<SyncModule,Integer>{

	@Autowired
	SyncModuleDao syncModuleDao;

    @Autowired
    SyncModuleService(SyncModuleDao syncModuleDao){
        super(syncModuleDao);
    }
    
    
}
