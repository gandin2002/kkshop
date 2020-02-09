package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.BackInfomationDao;
import cn.bohoon.order.entity.BackInfomation;

@Service
@Transactional
public class BackInfomationService extends  BaseService<BackInfomation,String>{
    @Autowired
    BackInfomationDao  backInfomationDao;
    
    @Autowired
    BackInfomationService(BackInfomationDao  backInfomationDao){
        super(backInfomationDao);
    }
    
}
