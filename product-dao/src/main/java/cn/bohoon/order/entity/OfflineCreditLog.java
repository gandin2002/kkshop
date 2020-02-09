package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 线下账款交易统计表   */
@Entity
@Table(name = "t_offlineCreditLog" )
public class OfflineCreditLog {
	 
	@Id
	@Column(length=64)
	String id;// 账款id
	
	String  companyId;//企业主体id
	Date  createDate;  //下单时间
	Date  invoiceDate; //开票时间
	String  willPay;  //即将付款时间
	
	String orderId;  // 销售订单
	String  paymentDate; // 账款日期
	BigDecimal  needMoney; // 应付金额
	BigDecimal   oldMoney;// 已付金额
	BigDecimal   discountMoeny;//折扣金额
	BigDecimal   notMoney; //未付款金额
	Boolean   orInvoice=false;  //是否开票  默认不开
	
	String  finallyPay;//最后付款时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getWillPay() {
		return willPay;
	}

	public void setWillPay(String willPay) {
		this.willPay = willPay;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(BigDecimal needMoney) {
		this.needMoney = needMoney;
	}

	public BigDecimal getOldMoney() {
		return oldMoney;
	}

	public void setOldMoney(BigDecimal oldMoney) {
		this.oldMoney = oldMoney;
	}

	public BigDecimal getDiscountMoeny() {
		return discountMoeny;
	}

	public void setDiscountMoeny(BigDecimal discountMoeny) {
		this.discountMoeny = discountMoeny;
	}

	public BigDecimal getNotMoney() {
		return notMoney;
	}

	public void setNotMoney(BigDecimal notMoney) {
		this.notMoney = notMoney;
	}

	public Boolean getOrInvoice() {
		return orInvoice;
	}

	public void setOrInvoice(Boolean orInvoice) {
		this.orInvoice = orInvoice;
	}

	public String getFinallyPay() {
		return finallyPay;
	}

	public void setFinallyPay(String finallyPay) {
		this.finallyPay = finallyPay;
	}
	
	
	
}
