package cn.bohoon.order.domain;

import java.math.BigDecimal;

import cn.bohoon.excel.util.ExportConfig;

/**
 * 导出 订单明细 Excel
 * 
 * @author HJ 2017年12月26日,下午3:04:59
 */
public class OrderDetailExcel {

	@ExportConfig(name = "订单号")
	String orderId;

	@ExportConfig(name = "订单状态")
	String orderState;

	@ExportConfig(name = "订单总额")
	BigDecimal orderTotalAmount;

	@ExportConfig(name = "货品总额")
	BigDecimal GoodsTotalAmount;

	@ExportConfig(name = "运费")
	BigDecimal freightFee;

	@ExportConfig(name = "优惠减免金额")
	BigDecimal PreferentialReduction;

	@ExportConfig(name = "送出积分数")
	BigDecimal score;

	@ExportConfig(name = "付款时间")
	String payTime;

	@ExportConfig(name = "退款金额")
	BigDecimal reFundAmount;

	@ExportConfig(name = "扣除积分")
	Integer DeductScore;

	@ExportConfig(name = "所属企业")
	String companyName;

	@ExportConfig(name = "所属会员")
	String userInfoId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public BigDecimal getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public BigDecimal getGoodsTotalAmount() {
		return GoodsTotalAmount;
	}

	public void setGoodsTotalAmount(BigDecimal goodsTotalAmount) {
		GoodsTotalAmount = goodsTotalAmount;
	}

	public BigDecimal getFreightFee() {
		return freightFee;
	}

	public void setFreightFee(BigDecimal freightFee) {
		this.freightFee = freightFee;
	}

	public BigDecimal getPreferentialReduction() {
		return PreferentialReduction;
	}

	public void setPreferentialReduction(BigDecimal preferentialReduction) {
		PreferentialReduction = preferentialReduction;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public BigDecimal getReFundAmount() {
		return reFundAmount;
	}

	public void setReFundAmount(BigDecimal reFundAmount) {
		this.reFundAmount = reFundAmount;
	}

	public Integer getDeductScore() {
		return DeductScore;
	}

	public void setDeductScore(Integer deductScore) {
		DeductScore = deductScore;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

}
