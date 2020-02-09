package cn.bohoon.order.domain;

import java.math.BigDecimal;

import cn.bohoon.excel.util.ExportConfig;
/**
 * 导出订单项
 * @author HJ
 * 2017年12月26日,上午9:44:56
 */
public class OrderReceiptExcel {
	
	@ExportConfig(name="收款单ID")
	String id;
	
	@ExportConfig(name="订单ID")
	String orderId;
	
	@ExportConfig(name="订单金额")
	BigDecimal orderFee;
	
	@ExportConfig(name="支付类型")
	String settleWay;
	
	@ExportConfig(name="付款会员")
	String userId;
	
	@ExportConfig(name="所属企业")
	String companyName;
	
	@ExportConfig(name="付款时间")
	String paymentTime;
	
	

	public OrderReceiptExcel() {
	}

	public OrderReceiptExcel(String id, String orderId, BigDecimal orderFee, String settleWay, String userId,
			String companyName, String paymentTime) {
		this.id = id;
		this.orderId = orderId;
		this.orderFee = orderFee;
		this.settleWay = settleWay;
		this.userId = userId;
		this.companyName = companyName;
		this.paymentTime = paymentTime;
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

	public BigDecimal getOrderFee() {
		return orderFee;
	}

	public void setOrderFee(BigDecimal orderFee) {
		this.orderFee = orderFee;
	}

	public String getSettleWay() {
		return settleWay;
	}

	public void setSettleWay(String settleWay) {
		this.settleWay = settleWay;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}
}
