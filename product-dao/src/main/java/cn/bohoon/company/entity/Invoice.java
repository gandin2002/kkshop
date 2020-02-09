package cn.bohoon.company.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.order.domain.InvoiceType;

/** 发票信息 */
@Entity
@Table(name = "t_invoice")
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String memberId; //会员Id
	@Enumerated(EnumType.STRING)
	InvoiceType invoiceType = InvoiceType.NOTNEED; //发票类型
	String title = "个人发票"; //发票抬头
	@Column(columnDefinition="varchar(255) default ''")
	String content; //发票内容（明细）
	String companyId ;//企业ID
	String companyName; //单位名称
	String taxpayerNumber; //纳税人识别号
	@Override
	public String toString() {
		return "Invoice [id=" + id + ", memberId=" + memberId + ", invoiceType=" + invoiceType + ", title=" + title
				+ ", content=" + content + ", companyId=" + companyId + ", companyName=" + companyName
				+ ", taxpayerNumber=" + taxpayerNumber + ", companyAddress=" + companyAddress + ", companyPhone="
				+ companyPhone + ", depositBank=" + depositBank + ", accountBank=" + accountBank + ", isDefault="
				+ isDefault + ", generalTaxpayer=" + generalTaxpayer + ", otherTaxpayer=" + otherTaxpayer + "]";
	}
	String companyAddress; //单位注册地址 
	String companyPhone; //注册电话
	String depositBank; //开户银行
	String accountBank; //银行账户 
	@Column(columnDefinition="tinyint(2)")
	Boolean isDefault = false ; //是否默认发票
	String generalTaxpayer ; //一般纳税人
	String otherTaxpayer ; // 其他
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public InvoiceType getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getTaxpayerNumber() {
		return taxpayerNumber;
	}
	public void setTaxpayerNumber(String taxpayerNumber) {
		this.taxpayerNumber = taxpayerNumber;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public String getDepositBank() {
		return depositBank;
	}
	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}
	public String getAccountBank() {
		return accountBank;
	}
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getGeneralTaxpayer() {
		return generalTaxpayer;
	}
	public void setGeneralTaxpayer(String generalTaxpayer) {
		this.generalTaxpayer = generalTaxpayer;
	}
	public String getOtherTaxpayer() {
		return otherTaxpayer;
	}
	public void setOtherTaxpayer(String otherTaxpayer) {
		this.otherTaxpayer = otherTaxpayer;
	}
	
}
