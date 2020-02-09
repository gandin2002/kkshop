package cn.bohoon.interfaces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.interfaces.dao.SyncDataJobDao;
import cn.bohoon.interfaces.entity.SyncDataJob;

@Service
@Transactional
public class SyncDataJobService extends BaseService<SyncDataJob,Integer>{

	@Autowired
	SyncDataJobDao syncDataJobDao;

    @Autowired
    SyncDataJobService(SyncDataJobDao syncDataJobDao){
        super(syncDataJobDao);
    }
    
    
}
