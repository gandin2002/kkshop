package cn.bohoon.order.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.util.RequestUtil;
import cn.bohoon.order.dao.OrderLogDao;
import cn.bohoon.order.domain.OrderCheckState;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderBarter;
import cn.bohoon.order.entity.OrderCancel;
import cn.bohoon.order.entity.OrderLog;
import cn.bohoon.order.entity.OrderRepaire;

@Service
@Transactional
public class OrderLogService extends BaseService<OrderLog,Integer>{

	@Autowired
	OrderLogDao orderLogDao;

    @Autowired
    OrderLogService(OrderLogDao orderLogDao){
        super(orderLogDao);
    }

	public void save(String loginUser ,Order order, OrderCheckState ocs, String note) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		// TODO Auto-generated method stub
		OrderLog orderLog = new OrderLog() ;
		orderLog.setOrderId(order.getId());
		orderLog.setOrderUserId(order.getUserId());
		orderLog.setOrderUserName(order.getUsername());
		orderLog.setOperatIp(RequestUtil.getRemoteAddr(request));
		orderLog.setUsername(loginUser);
		orderLog.setOrderCheckState(ocs);
		orderLog.setNote(note);
		orderLog.setCreateDate(new Date());
		save(orderLog) ;
	}

	/**
	 * 换货单日志信息
	 * 
	 * @param loginUser
	 * @param obModel
	 * @param ocs
	 * @param note
	 * @throws Exception
	 */
	public void save(String loginUser ,OrderBarter obModel, OrderCheckState ocs, String note) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		OrderLog orderLog = new OrderLog() ;
		orderLog.setOrderId(obModel.getId());
		orderLog.setOrderUserId(obModel.getUserId());
		orderLog.setOrderUserName(obModel.getUsername());
		orderLog.setOperatIp(RequestUtil.getRemoteAddr(request));
		orderLog.setUsername(loginUser);
		orderLog.setOrderCheckState(ocs);
		orderLog.setNote(note);
		orderLog.setCreateDate(new Date());
		save(orderLog) ;
	}

	/**
	 * 取消订单日志信息
	 * 
	 * @param username
	 * @param oc
	 * @param confirmCancel
	 * @param note
	 */
	public void save(String username, OrderCancel oc, OrderCheckState confirmCancel, String note) {
		// TODO Auto-generated method stub
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		// TODO Auto-generated method stub
		OrderLog orderLog = new OrderLog() ;
		orderLog.setOrderId(oc.getId());
		orderLog.setOrderUserId(oc.getUserId());
		orderLog.setOrderUserName(oc.getUsername());
		orderLog.setOperatIp(RequestUtil.getRemoteAddr(request));
		orderLog.setUsername(username);
		orderLog.setOrderCheckState(confirmCancel);
		orderLog.setNote(note);
		orderLog.setCreateDate(new Date());
		save(orderLog) ;
	}

	/**
	 * 保存订单日志信息
	 * 
	 * @param username
	 * @param order
	 * @param ocs
	 * @param note
	 */
	public void save(String username, OrderRepaire order, OrderCheckState ocs, String note) {
		// TODO Auto-generated method stub
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		OrderLog orderLog = new OrderLog() ;
		orderLog.setOrderId(order.getId());
		orderLog.setOrderUserId(order.getUserId());
		orderLog.setOrderUserName(order.getUsername());
		orderLog.setOperatIp(RequestUtil.getRemoteAddr(request));
		orderLog.setUsername(username);
		orderLog.setOrderCheckState(ocs);
		orderLog.setNote(note);
		orderLog.setCreateDate(new Date());
		save(orderLog) ;
	}
}
