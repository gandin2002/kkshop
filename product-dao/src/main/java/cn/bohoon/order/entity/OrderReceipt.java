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

import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.order.domain.CashWay;
import cn.bohoon.order.domain.PayType;
import cn.bohoon.order.domain.Payway;
import cn.bohoon.order.domain.SettleWay;

/**
 * 订单收款信息
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_order_receipt")
public class OrderReceipt {

	@Id
	private String id; // 主键ID
	/**
	 * 付款会员ID
	 */
	@Column(length=64)
	private String userInfoId;
	/**
	 * 付款会员公司ID
	 */
	private String companyId;
	/**
	 * 付款类型
	 */
	@Column(length=64)
	@Enumerated(EnumType.STRING)
	private PayType payType;
	
	@Column(length=64)
	private String orderId; // 订单ID
	
	/**
	 * 支付方式
	 */
	@Enumerated(EnumType.STRING)
	private Payway payway;
	
	/**
	 * 付款方式
	 */
	@Enumerated(EnumType.STRING)
	private SettleWay settleWay = SettleWay._DEFAULT;
	
	/**
	 * 结算方式
	 */
	@Enumerated(EnumType.STRING)
	private CashWay cashWay = CashWay._DEFAULT;
	
	/**
	 * 汇款银行账户
	 */
	private Integer payBankId;
	
	/**
	 * 收款银行账户
	 */
	private Integer reachBankId;
	private String orderUserName;
	private Boolean status; // 状态  true:支付成功
	private BigDecimal orderFee = new BigDecimal(0.00); // 订单金额 总金额1
	private BigDecimal otherReduction = new BigDecimal(0.00);// 改价减免金额
	private BigDecimal cashFee = new BigDecimal(0.00);// 收款现金
	private BigDecimal couponReduction = new BigDecimal(0.00);// 礼品卡抵现 优惠券
	private BigDecimal promotionsReduction = new BigDecimal(0.00);// 优惠 促销减免
	private BigDecimal creditReduction = new BigDecimal(0.00);// 信用抵现
	//String payUser; // 付款会员 会员昵称
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payDate = new Date(); // 支付日期
	
	public String getOrderUserName() {
		return orderUserName;
	}

	public void setOrderUserName(String orderUserName) {
		this.orderUserName = orderUserName;
	}

	/**
	 * 到账日期时间
	 */
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reachDate = new Date(); 
	
	private Integer giveScore = 0; // 赠送积分
//	String payName; // 付款人姓名
//	String ownCompany; // 付款人所属企业
	
	/**
	 * 备注信息
	 */
	@Column(columnDefinition = "varchar(1000) default ''")
	private String note;
	
	/**
	 * 附件
	 */
	private String attachImage;
	
	@Transient
	public String getTimeString() {
		return DateUtil.formatDate(this.payDate);
	}
	
	@Transient
	public String getReachTimeString() {
		return DateUtil.formatDate(this.reachDate);
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

	public SettleWay getSettleWay() {
		return settleWay;
	}

	public void setSettleWay(SettleWay settleWay) {
		this.settleWay = settleWay;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public BigDecimal getOrderFee() {
		return orderFee;
	}

	public void setOrderFee(BigDecimal orderFee) {
		this.orderFee = orderFee;
	}

	public BigDecimal getOtherReduction() {
		return otherReduction;
	}

	public void setOtherReduction(BigDecimal otherReduction) {
		this.otherReduction = otherReduction;
	}

	public BigDecimal getCashFee() {
		return cashFee;
	}

	public void setCashFee(BigDecimal cashFee) {
		this.cashFee = cashFee;
	}

	public BigDecimal getCouponReduction() {
		return couponReduction;
	}

	public void setCouponReduction(BigDecimal couponReduction) {
		this.couponReduction = couponReduction;
	}

	public BigDecimal getPromotionsReduction() {
		return promotionsReduction;
	}

	public void setPromotionsReduction(BigDecimal promotionsReduction) {
		this.promotionsReduction = promotionsReduction;
	}

//	public String getPayUser() {
//		return payUser;
//	}
//
//	public void setPayUser(String payUser) {
//		this.payUser = payUser;
//	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Integer getGiveScore() {
		return giveScore;
	}

	public void setGiveScore(Integer giveScore) {
		this.giveScore = giveScore;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public Payway getPayway() {
		return payway;
	}

	public void setPayway(Payway payway) {
		this.payway = payway;
	}

	public CashWay getCashWay() {
		return cashWay;
	}

	public void setCashWay(CashWay cashWay) {
		this.cashWay = cashWay;
	}

	public Integer getPayBankId() {
		return payBankId;
	}

	public void setPayBankId(Integer payBankId) {
		this.payBankId = payBankId;
	}

	public Integer getReachBankId() {
		return reachBankId;
	}

	public void setReachBankId(Integer reachBankId) {
		this.reachBankId = reachBankId;
	}

	public Date getReachDate() {
		return reachDate;
	}

	public void setReachDate(Date reachDate) {
		this.reachDate = reachDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAttachImage() {
		return attachImage;
	}

	public void setAttachImage(String attachImage) {
		this.attachImage = attachImage;
	}

	public BigDecimal getCreditReduction() {
		return creditReduction;
	}

	public void setCreditReduction(BigDecimal creditReduction) {
		this.creditReduction = creditReduction;
	}


//	public String getPayName() {
//		return payName;
//	}
//
//	public void setPayName(String payName) {
//		this.payName = payName;
//	}

//	public String getOwnCompany() {
//		return ownCompany;
//	}
//
//	public void setOwnCompany(String ownCompany) {
//		this.ownCompany = ownCompany;
//	}
	
	
}
