package cn.bohoon.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderRefundDao;
import cn.bohoon.order.entity.OrderRefund;

@Service
@Transactional
public class OrderRefundService extends BaseService<OrderRefund,String>{

	@Autowired
	OrderRefundDao orderRefundDao;
	
	Logger logger = LoggerFactory.getLogger(OrderRefundService.class) ;
 
	 @Autowired
	 OrderRefundService(OrderRefundDao orderRefundDao){
	        super(orderRefundDao);
	    }

}
