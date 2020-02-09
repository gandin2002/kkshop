package cn.bohoon.order.service;

import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderItemDao;
import cn.bohoon.order.entity.OrderItem;

@Service
@Transactional
public class OrderItemService extends BaseService<OrderItem,Integer>{

	@Autowired
	OrderItemDao orderItemDao;
	
    List<OrderItem> listItem=null;
    public List<OrderItem> getListItem() {
		return listItem;
	}

	public void setListItem(List<OrderItem> listItem) {
		this.listItem = listItem;
	}

	@Autowired
    OrderItemService(OrderItemDao orderItemDao){
        super(orderItemDao);
    }
   
   
}
