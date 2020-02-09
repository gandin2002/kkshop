package cn.bohoon.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderBarterDao;
import cn.bohoon.order.entity.AmOrderItem;
import cn.bohoon.order.entity.OrderBarter;
import cn.bohoon.order.entity.OrderItem;

@Service
@Transactional
public class OrderBarterService extends BaseService<OrderBarter,String>{

	@Autowired
	OrderBarterDao orderBarterDao;
	@Autowired
	OrderItemService orderItemService ;

    @Autowired
    OrderBarterService(OrderBarterDao orderBarterDao){
        super(orderBarterDao);
    }

	public void createBarterOrder(OrderBarter orderBarter, List<AmOrderItem> aois) {
		
		for(AmOrderItem aoi : aois) {
			aoi.setOrderId(orderBarter.getAmOrder());
			OrderItem oi = new OrderItem(aoi) ;
			orderItemService.save(oi);
		}
		orderBarterDao.save(orderBarter) ;
	}

}
