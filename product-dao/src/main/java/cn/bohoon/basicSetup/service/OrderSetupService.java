package cn.bohoon.basicSetup.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.basicSetup.dao.OrderSetupDao;
import cn.bohoon.basicSetup.entity.OrderSetup;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class OrderSetupService extends BaseService<OrderSetup,Integer>{

	@Autowired
	OrderSetupDao orderSetupDao;
	
    @Autowired
    OrderSetupService(OrderSetupDao orderSetupDao){
        super(orderSetupDao);
    }

    
}
