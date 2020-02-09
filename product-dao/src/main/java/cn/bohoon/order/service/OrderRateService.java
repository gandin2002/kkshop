package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderRateDao;
import cn.bohoon.order.entity.OrderRate;

@Service
@Transactional
public class OrderRateService extends BaseService<OrderRate, Integer> {

	@Autowired
	OrderRateDao orderRateDao;

	@Autowired
	OrderRateService(OrderRateDao orderRateDao) {
		super(orderRateDao);
	}

}
