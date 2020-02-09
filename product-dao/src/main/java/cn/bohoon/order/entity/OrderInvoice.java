package cn.bohoon.order.entity;

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
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.order.domain.InvoiceType;

/** 发票信息 */
@Entity
@Table(name = "t_order_invoice")
public class OrderInvoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Column(length=64)
	String memberId; // 会员Id
	@Enumerated(EnumType.STRING)
	InvoiceType invoiceType = InvoiceType.NOTNEED; // 发票类型
	String title = ""; // 发票抬头 个人发票 公司名称
	@Column(columnDefinition = "varchar(255) default ''")
	String content; // 发票内容（明细）
	
	@Column(length=64)
	String companyId; // 企业ID 信息
	String companyName; // 单位名称
	String taxpayerNumber; // 纳税人识别号
	String companyAddress; // 单位注册地址
	String companyPhone; // 注册电话
	String depositBank; // 开户银行
	String accountBank; // 银行账户
	String socialCreditCode; // 社会统一信用代码
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date makeDate; // 开票时间
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date creatDate = new Date(); // 申请时间
	Integer state = 0; // 开票状态 0 申请中 ，1已开票
	String generalTaxpayer = ""; // 一般纳税人
	String otherTaxpayer = ""; // 其他

	// 新加字段
	String orderId; // 订单编号
	String name; // 开票人
	BigDecimal payment = new BigDecimal(0); // 支付现金
	Integer invoiceKind = 0; // 发票性质 0:纸质 ;1:电子
	String sendAddr; // 发票寄送地址

	
	@Transient
	public String getInvoiceTypeString(){
		return this.invoiceType.getName();
	}
	
	@Transient
	public @ExportConfig(name = "申请时间") String getTimeString() {
		return DateUtil.formatDate(this.creatDate);
	}

	@Transient
	public @ExportConfig(name = "开票时间") String getMakeTimeString() {
		return DateUtil.formatDate(this.makeDate);
	}

	@Transient
	public @ExportConfig(name = "开票状态 ") String getStateString() {
		if (state == 1) {
			return "已开票";
		}
		return "申请中";
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@ExportConfig(name = "开票人员")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExportConfig(name="开票金额")
	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ExportConfig(name = "会员Id")
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

	@ExportConfig(name="发票抬头")
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

	@ExportConfig(name = "公司名称")
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

	@ExportConfig(name="社会统一信用代码")
	public String getSocialCreditCode() {
		return socialCreditCode;
	}

	public void setSocialCreditCode(String socialCreditCode) {
		this.socialCreditCode = socialCreditCode;
	}

	public Date getCreatDate() {
		return creatDate;
	}

	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getInvoiceKind() {
		return invoiceKind;
	}

	public void setInvoiceKind(Integer invoiceKind) {
		this.invoiceKind = invoiceKind;
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

	public String getSendAddr() {
		return sendAddr;
	}

	public void setSendAddr(String sendAddr) {
		this.sendAddr = sendAddr;
	}

}
