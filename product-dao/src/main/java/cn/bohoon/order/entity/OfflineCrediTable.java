package cn.bohoon.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 线下信用明细表
 */
@Entity
@Table(name = "t_OfflineCrediTable" )
public class OfflineCrediTable {
	@Id
	@Column(length=64)
	String id;// 账款id
	String  companyId;//企业主体id
	Date  createDate;  //单据日期
	String  creditKind;//信用占用类别
	
	Date  invoiceDate; //开票时间
	String  willPay;  //即将付款时间
	String  creditMoney;//信用占用金额
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
	public String getCreditKind() {
		return creditKind;
	}
	public void setCreditKind(String creditKind) {
		this.creditKind = creditKind;
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
	public String getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(String creditMoney) {
		this.creditMoney = creditMoney;
	}
	
	

}
