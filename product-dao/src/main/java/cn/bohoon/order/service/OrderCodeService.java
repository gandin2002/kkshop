package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderCodeDao;
import cn.bohoon.order.entity.OrderCode;

@Service
@Transactional
public class OrderCodeService extends BaseService<OrderCode,Integer>{

	@Autowired
	OrderCodeDao orderCodeDao;

    @Autowired
    OrderCodeService(OrderCodeDao orderCodeDao){
        super(orderCodeDao);
    }

}
