package cn.bohoon.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderRepaireDao;
import cn.bohoon.order.entity.AmOrderItem;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderRepaire;

@Service
@Transactional
public class OrderRepaireService extends BaseService<OrderRepaire,String>{

	@Autowired
	OrderRepaireDao orderRepaireDao;
	@Autowired
	OrderItemService orderItemService ;

    @Autowired
    OrderRepaireService(OrderRepaireDao orderRepaireDao){
        super(orderRepaireDao);
    }

	public void createRepaireOrder(OrderRepaire order, List<AmOrderItem> aois) {
		
		for(AmOrderItem aoi : aois) {
			aoi.setOrderId(order.getAmOrder());
			OrderItem oi = new OrderItem(aoi) ;
			orderItemService.save(oi);
		}
		orderRepaireDao.save(order) ;
	}

}
