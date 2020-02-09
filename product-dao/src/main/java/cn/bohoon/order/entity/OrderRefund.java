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
import cn.bohoon.order.domain.SettleWay;

@Entity
@Table(name = "t_order_refund")
public class OrderRefund {

	@Id
	String id; // 退款ID（）
	@Column(length=64)
	String orderId;// 订单ID
	@Column(length=64)
	String amOrderId; // 售后订单ID
	String company; // 所属企业
	@Column(length=64)
	String userId; // 收款会员ID
	String username; // 收款会员

	@Enumerated(EnumType.STRING)
	SettleWay settleWay; // 结算方式
	@Enumerated(EnumType.STRING)
	OrderState orderState; // 退款状态
	@Enumerated(EnumType.STRING)
	ReOrderType reOrderType; // 售后类型
	BigDecimal afterSaleMoney = new BigDecimal(0); // 售后涉及金额
	BigDecimal refundMoney = new BigDecimal(0); // 退款金额
	Integer backScore = 0 ; // 扣除积分
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date(); // 退款时间

	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date appDate = new Date(); // 申请时间

	String operator; // 退款操作人

	public OrderRefund() {
		super();
	}

	public OrderRefund(String id, String orderId, String amOrderId, String company, String userId, String username,
			SettleWay settleWay, OrderState orderState, ReOrderType reOrderType, BigDecimal afterSaleMoney,
			BigDecimal refundMoney,Integer backScore , Date createDate, Date appDate, String operator) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.amOrderId = amOrderId;
		this.company = company;
		this.userId = userId;
		this.username = username;
		this.settleWay = settleWay;
		this.orderState = orderState;
		this.reOrderType = reOrderType;
		this.afterSaleMoney = afterSaleMoney;
		this.refundMoney = refundMoney;
		this.backScore = backScore ;
		this.createDate = createDate;
		this.appDate = appDate;
		this.operator = operator;
	}

	public OrderRefund(Order order, AfterMarketOrder amOrder) {
		this.orderId = order.getId();
		this.amOrderId = amOrder.getId();
		this.company = order.getCompany();
		this.userId = order.getUserId();
		this.username = order.getUsername();
		this.settleWay = order.getSettleWay();
		this.orderState = OrderState.REFUND_AND_FINISH;
		this.reOrderType = amOrder.getReOrderType();
		this.afterSaleMoney = order.getTotal();
		this.refundMoney = amOrder.getReFundFee();
		this.appDate = amOrder.getCreateDate();
	}

	public OrderRefund(Order order, OrderCancel orderCancel) {
		this.orderId = order.getId();
		this.amOrderId = orderCancel.getId();
		this.company = order.getCompany();
		this.userId = order.getUserId();
		this.username = order.getUsername();
		this.settleWay = order.getSettleWay();
		this.orderState = OrderState.REFUND_AND_FINISH;
		this.reOrderType = orderCancel.getReOrderType();
		this.afterSaleMoney = order.getTotal();
		this.refundMoney = orderCancel.getRefundMoney();
		this.appDate = orderCancel.getCreateDate();
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

	public String getAmOrderId() {
		return amOrderId;
	}

	public void setAmOrderId(String amOrderId) {
		this.amOrderId = amOrderId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public OrderState getOrderState() {
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}

	public SettleWay getSettleWay() {
		return settleWay;
	}

	public void setSettleWay(SettleWay settleWay) {
		this.settleWay = settleWay;
	}

	public ReOrderType getReOrderType() {
		return reOrderType;
	}

	public void setReOrderType(ReOrderType reOrderType) {
		this.reOrderType = reOrderType;
	}

	public BigDecimal getAfterSaleMoney() {
		return afterSaleMoney;
	}

	public void setAfterSaleMoney(BigDecimal afterSaleMoney) {
		this.afterSaleMoney = afterSaleMoney;
	}

	public BigDecimal getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getBackScore() {
		return backScore;
	}

	public void setBackScore(Integer backScore) {
		this.backScore = backScore;
	}

	
}
