package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.AmOrderItemDao;
import cn.bohoon.order.entity.AmOrderItem;

@Service
@Transactional
public class AmOrderItemService extends BaseService<AmOrderItem,String>{

	@Autowired
	AmOrderItemDao amOrderItemDao;

    @Autowired
    AmOrderItemService(AmOrderItemDao amOrderItemDao){
        super(amOrderItemDao);
    }
 

}
