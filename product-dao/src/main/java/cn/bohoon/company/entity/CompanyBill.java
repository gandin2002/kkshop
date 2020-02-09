package cn.bohoon.company.entity;

import java.math.BigDecimal;
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

import cn.bohoon.company.domain.BillType;
/*
 * 账单记录
 */
@Entity
@Table(name = "t_company_bill")
public class CompanyBill {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;                                            //主键
	String  billcode;                                      //账单号
	String companyId;                                     //企业ID
	String orderId;                                        //订单ID
	@Column(columnDefinition = "datetime(0)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date billPeriod;                                       //账单周期
	Integer repayment;                                     //还款天数
	@Enumerated(EnumType.STRING)
	BillType billType;                                     //账单状态
	BigDecimal currentBalance = new BigDecimal(0);         //本期应还总额      
	String currency = "人民币";                              //清算货币
	BigDecimal consumptionQuota = new BigDecimal(0);       //户口消费额度
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBillcode() {
		return billcode;
	}
	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getBillPeriod() {
		return billPeriod;
	}
	public void setBillPeriod(Date billPeriod) {
		this.billPeriod = billPeriod;
	}
	public Integer getRepayment() {
		return repayment;
	}
	public void setRepayment(Integer repayment) {
		this.repayment = repayment;
	}
	public BillType getBillType() {
		return billType;
	}
	public void setBillType(BillType billType) {
		this.billType = billType;
	}
	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getConsumptionQuota() {
		return consumptionQuota;
	}
	public void setConsumptionQuota(BigDecimal consumptionQuota) {
		this.consumptionQuota = consumptionQuota;
	}
}
