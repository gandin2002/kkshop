package cn.bohoon.interfaces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.interfaces.dao.SyncGroupDao;
import cn.bohoon.interfaces.entity.SyncGroup;

@Service
@Transactional
public class SyncGroupService extends BaseService<SyncGroup,Integer>{

	@Autowired
	SyncGroupDao syncGroupDao;

    @Autowired
    SyncGroupService(SyncGroupDao syncGroupDao){
        super(syncGroupDao);
    }
    
    
}
