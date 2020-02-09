package cn.bohoon.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.order.domain.OrderCheckState;

/** 订单处理记录 */
@Entity
@Table(name = "t_order_log")
public class OrderLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=64)
	private String orderId; // 订单ID 
	/* 下单人ID */
	private String orderUserId;
	/* 下单人用户名 */
	private String orderUserName;
	/* 处理人 */
	private String username;
	/* 操作IP */
	private String operatIp;
	/* 处理动作 */
	@Enumerated(EnumType.STRING)
	private OrderCheckState orderCheckState;
	/* 备注 */
	@Column(columnDefinition = "varchar(1000) default ''")
	private String note;
	/* 创建时间 */
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate = new Date();

	@Transient
	public String getTimeString() {
		return DateUtil.formatDate(this.createDate);
	}

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

	public String getOrderUserId() {
		return orderUserId;
	}

	public void setOrderUserId(String orderUserId) {
		this.orderUserId = orderUserId;
	}

	public String getOrderUserName() {
		return orderUserName;
	}

	public void setOrderUserName(String orderUserName) {
		this.orderUserName = orderUserName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOperatIp() {
		return operatIp;
	}

	public void setOperatIp(String operatIp) {
		this.operatIp = operatIp;
	}

	public OrderCheckState getOrderCheckState() {
		return orderCheckState;
	}

	public void setOrderCheckState(OrderCheckState orderCheckState) {
		this.orderCheckState = orderCheckState;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}