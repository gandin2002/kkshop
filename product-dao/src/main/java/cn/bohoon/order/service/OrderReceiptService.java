package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderReceiptDao;
import cn.bohoon.order.entity.OrderReceipt;

@Service
@Transactional
public class OrderReceiptService extends BaseService<OrderReceipt,String>{

	@Autowired
	OrderReceiptDao orderReceiptDao;

    @Autowired
    OrderReceiptService(OrderReceiptDao orderReceiptDao){
        super(orderReceiptDao);
    }

}
