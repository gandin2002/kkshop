package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderCancelDao;
import cn.bohoon.order.domain.OrderCheckState;
import cn.bohoon.order.entity.OrderCancel;

@Service
@Transactional
public class OrderCancelService extends BaseService<OrderCancel, String> {

	@Autowired
	OrderCancelDao orderCancelDao;
	@Autowired
	OrderLogService orderLogService ;

	@Autowired
	OrderCancelService(OrderCancelDao orderCancelDao) {
		super(orderCancelDao);
	}

	public void save(OrderCancel oc, String username, String note) {
		// TODO Auto-generated method stub
		orderCancelDao.save(oc) ;
		orderLogService.save(username, oc, OrderCheckState.CONFIRM_CANCEL, note);
	}

	public void auditPass(OrderCancel order, String managerName) {
		// TODO Auto-generated method stub
		orderCancelDao.save(order) ;
		String note = "管理员审核通过！" ;
		orderLogService.save(managerName, order, OrderCheckState.CONFIRM_PASS, note);
	}

	public void auditRefund(OrderCancel order, String managerName) {
		// TODO Auto-generated method stub
		orderCancelDao.save(order) ;
		String note = "管理员审核通过并退款！" ;
		orderLogService.save(managerName, order, OrderCheckState.CONFIRM_PASS, note);
	}

}
