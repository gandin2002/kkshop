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

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.order.domain.OperateIdentity;
import cn.bohoon.order.domain.OrderActivity;

@Entity
@Table(name = "t_order_service_log")
public class AmOrderLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Column(length=64)
	String amOrderId; //售后订单ID
	
	@Column(length=64)
	String orderUserId; //下单人ID
	
	String orderUserName; //下单人用户名
	
	String username; //处理人
	
	String operatIp; //操作IP
	
	@Enumerated(EnumType.STRING)
	OrderActivity orderActivity; //处理动作

	@Enumerated(EnumType.STRING)
	OperateIdentity operateIdentity; //操作人员身份

	@Column(columnDefinition = "varchar(1000) default ''")
	String note; //备注

	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date(); //创建时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAmOrderId() {
		return amOrderId;
	}

	public void setAmOrderId(String amOrderId) {
		this.amOrderId = amOrderId;
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

	public OrderActivity getOrderActivity() {
		return orderActivity;
	}

	public void setOrderActivity(OrderActivity orderActivity) {
		this.orderActivity = orderActivity;
	}

	public OperateIdentity getOperateIdentity() {
		return operateIdentity;
	}

	public void setOperateIdentity(OperateIdentity operateIdentity) {
		this.operateIdentity = operateIdentity;
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
