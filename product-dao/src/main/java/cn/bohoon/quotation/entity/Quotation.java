package cn.bohoon.quotation.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.quotation.domain.QuotationState;

//报价单
@Entity
@Table(name = "t_quotation")
public class Quotation {
	@Id
	String id;//报价单号
	String userInfoId;//申请会员ID
	BigDecimal quotationSkuPrice;//商品售价总金额(SKU报价时)
	BigDecimal quotationPrice;//商品报价总金额
	Integer productSkuNum;//报价单商品数量
	@Enumerated(EnumType.STRING)
	QuotationState quotationState;//状态【未报价/已报价/已通过/未通过】
	Integer modifyNum = 0;//修改货品数量
	@Column(columnDefinition="varchar(500) default ''")
	String denyDetail;//未通过原因
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date ValidDate;    //报价单有效期【截止日期】
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createTime ;//下单时间
	
	String companyId; //企业名称 
	String companyName ; //企业名称
	String operatorId;//报价人
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date quotationTime ;//报价时间
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date checkTime ;//审核时间
	
	@Transient
	public String getTimeString() {
		return DateUtil.formatDate(this.createTime);
	}
	
	@Transient
	public String getValidDateString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.ValidDate);
	}
	
	@Transient
	public String getQuotationTimeString() {
		return DateUtil.formatDate(this.quotationTime);
	}
	
	@Transient
	public String getCheckTimeString() {
		return DateUtil.formatDate(this.checkTime);
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

	public String getId() {
		return id; 
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	public BigDecimal getQuotationSkuPrice() {
		return quotationSkuPrice;
	}
	public void setQuotationSkuPrice(BigDecimal quotationSkuPrice) {
		this.quotationSkuPrice = quotationSkuPrice;
	}
	public BigDecimal getQuotationPrice() {
		return quotationPrice;
	}
	public void setQuotationPrice(BigDecimal quotationPrice) {
		this.quotationPrice = quotationPrice;
	}
	public Integer getProductSkuNum() {
		return productSkuNum;
	}
	public void setProductSkuNum(Integer productSkuNum) {
		this.productSkuNum = productSkuNum;
	}
	public QuotationState getQuotationState() {
		return quotationState;
	}
	public void setQuotationState(QuotationState quotationState) {
		this.quotationState = quotationState;
	}
	public Integer getModifyNum() {
		return modifyNum;
	}
	public void setModifyNum(Integer modifyNum) {
		this.modifyNum = modifyNum;
	}
	public String getDenyDetail() {
		return denyDetail;
	}
	public void setDenyDetail(String denyDetail) {
		this.denyDetail = denyDetail;
	}
	public Date getValidDate() {
		return ValidDate;
	}
	public void setValidDate(Date validDate) {
		ValidDate = validDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public Date getQuotationTime() {
		return quotationTime;
	}
	public void setQuotationTime(Date quotationTime) {
		this.quotationTime = quotationTime;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
}
