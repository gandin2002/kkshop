package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.ReOrderType;

/**
 * 付款取消的订单
 * @author djq
 *
 */
@Entity
@Table(name = "t_order_cancel")
public class OrderCancel {

	@Id
	String id; // 售后订单ID（普通订单编号：OC20170719001）
	@Column(length=64)
	String orderId;// 订单ID
	String userId; // 下单人ID
	String username; // 下单人名称
	String company; // 所属企业
	@Enumerated(EnumType.STRING)
	OrderState orderState; // 订单状态
	@Enumerated(EnumType.STRING)
	ReOrderType reOrderType; // 售后类型

	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date(); // 订单创建时间
	BigDecimal refundMoney = new BigDecimal(0); // 退款金额

	
	
	public OrderCancel() {
		super();
	}

	public OrderCancel(Order order) {
		this.orderId = order.getId() ;
		this.userId = order.getUserId() ;
		this.username = order.getUsername() ;
		this.company = order.getCompany() ;
		this.orderState = OrderState.WAIT_AUDIT ;
		this.reOrderType = ReOrderType.CANCEL_ORDER ;
		this.refundMoney = order.getProductFee() ;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public OrderState getOrderState() {
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}

	public ReOrderType getReOrderType() {
		return reOrderType;
	}

	public void setReOrderType(ReOrderType reOrderType) {
		this.reOrderType = reOrderType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
	}

}
