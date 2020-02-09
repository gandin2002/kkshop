package cn.bohoon.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.TranType;
import cn.bohoon.userInfo.entity.ShippingInfo;

/**
 * 换货单信息
 * 
 * @author djq
 *
 */
@Entity
@Table(name = "t_order_barter" )
public class OrderBarter {

	@Id
	@Column(length=64)
	String id; // 换货单ID

	@Column(length=64)
	String orderId; // 原订单ID
	
	@Column(length=64)
	String amOrder ;

	/* 配送方式 */
	@Enumerated(EnumType.STRING)
	TranType tranType;

	/* 换货ID */
	String userId;
	/* 换货人会员号 */
	String username;

	/* 企业会员ID */
	String memberId;

	/* 企业名称 */
	String company;

	@Column(columnDefinition = "varchar(500) default ''")
	String shippingInfo; // 收货信息

	String receiver;// 收货人

	/* 换货单创建时间 */
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date();

	/* 换货单状态 */
	@Enumerated(EnumType.STRING)
	OrderState orderState;

	/* 订单备注 */
	@Column(columnDefinition = "varchar(1000) default ''")
	String note = "";
	
	public OrderBarter() {
		super();
	}

	public OrderBarter(Order order) {
		this.orderId = order.getId() ;
		this.tranType = TranType.EXPRESS ;
		this.userId = order.getUserId() ;
		this.username = order.getUsername() ;
		this.company = order.getCompany() ;
		this.memberId = order.getMemberId() ;
		this.shippingInfo = order.getShippingInfo() ;
		this.receiver = order.getReceiver() ;
		this.orderState = OrderState.WAIT_DELIVERY ;
	}
	
	@Transient
	public ShippingInfo getShippingInfoModel() {
		if(!StringUtils.isEmpty(shippingInfo)) {
			return JsonUtil.parse(shippingInfo, ShippingInfo.class) ;
		}
		return null ;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public TranType getTranType() {
		return tranType;
	}

	public void setTranType(TranType tranType) {
		this.tranType = tranType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getShippingInfo() {
		return shippingInfo;
	}

	public void setShippingInfo(String shippingInfo) {
		this.shippingInfo = shippingInfo;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public OrderState getOrderState() {
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAmOrder() {
		return amOrder;
	}

	public void setAmOrder(String amOrder) {
		this.amOrder = amOrder;
	}

}
