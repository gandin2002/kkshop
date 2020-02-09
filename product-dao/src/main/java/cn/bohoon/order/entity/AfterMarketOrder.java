package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.ReOrderReason;
import cn.bohoon.order.domain.ReOrderType;

/** 售后订单信息 */
@Entity
@Table(name = "t_order_service")
public class AfterMarketOrder {
	@Id
	@Column(length=64)
	String id; // 售后订单ID（普通订单编号：SC20170719001）
	@Column(length=64)
	String orderId;// 订单ID
	@Column(length=64)
	String userId; // 下单人ID
	String username; // 下单人名称
	@Column(length=64)
	String memberId; // 企业ID
	String company; // 公司名称
	String memberErpCode; // 企业ErpCode编码
	@Enumerated(EnumType.STRING)
	OrderState orderState; // 订单状态

	@Enumerated(EnumType.STRING)
	ReOrderType reOrderType; // 售后类型

	@Enumerated(EnumType.STRING)
	ReOrderReason reOrderReason; // 原因

	String applyReason;//原因
	
	BigDecimal reFundFee = new BigDecimal(0); // 退款money
	String wareHouserName; //退款仓库
	String reason; // 说明


	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date(); // 订单创建时间

	String image1 = ""; // 图片凭证1
	String image2 = ""; // 图片凭证2
	String image3 = ""; // 图片凭证3
	String image4 = ""; // 图片凭证4
	String image5 = ""; // 图片凭证5
	String image6 = ""; // 图片凭证6

	String transCompany; // 物流公司
	String transCompanyCode; // 物流公司的code
	String transNum; // 物流编码
	Integer wareHouseId ; //退货仓库地址

	
	public String getTransCompanyCode() {
		return transCompanyCode;
	}

	public void setTransCompanyCode(String transCompanyCode) {
		this.transCompanyCode = transCompanyCode;
	}

	@Transient
	public String getReOrderTypes(){
		return reOrderType.getName();
	}
	
	@Transient
	public String getReOrderReasons(){
		return reOrderReason.getName();
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMemberErpCode() {
		return memberErpCode;
	}

	public void setMemberErpCode(String memberErpCode) {
		this.memberErpCode = memberErpCode;
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

	public ReOrderReason getReOrderReason() {
		return reOrderReason;
	}

	public void setReOrderReason(ReOrderReason reOrderReason) {
		this.reOrderReason = reOrderReason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getImage5() {
		return image5;
	}

	public void setImage5(String image5) {
		this.image5 = image5;
	}

	public String getImage6() {
		return image6;
	}

	public void setImage6(String image6) {
		this.image6 = image6;
	}

	public String getTransCompany() {
		return transCompany;
	}

	public void setTransCompany(String transCompany) {
		this.transCompany = transCompany;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public BigDecimal getReFundFee() {
		return reFundFee;
	}

	public void setReFundFee(BigDecimal reFundFee) {
		this.reFundFee = reFundFee;
	}

	public Integer getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Integer wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getWareHouserName() {
		return wareHouserName;
	}
	
	public void setWareHouserName(String wareHouserName) {
		this.wareHouserName = wareHouserName;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
}
