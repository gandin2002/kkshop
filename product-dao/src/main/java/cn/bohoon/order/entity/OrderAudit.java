package cn.bohoon.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.order.domain.OrderAuditState;

/** 订单审核状态 */
@Entity
@Table(name = "t_order_state" )
public class OrderAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	@Column(length=64)
	String orderId; // 订单ID
	Integer departmentId; // 部门id
	OrderAuditState orderAuditState = OrderAuditState.WAIT_AUDIT; // 订单状态
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public OrderAuditState getOrderAuditState() {
		return orderAuditState;
	}
	public void setOrderAuditState(OrderAuditState orderAuditState) {
		this.orderAuditState = orderAuditState;
	}
	
	
	
	

	
}