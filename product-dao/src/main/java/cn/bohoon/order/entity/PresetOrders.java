package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.OrderType;
import cn.bohoon.order.domain.Payway;
import cn.bohoon.order.domain.SettleWay;
import cn.bohoon.order.domain.TranType;
import cn.bohoon.order.service.AmOrderItemService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.PresetOrderItemService;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.util.ConvertUtils;

/** 预制订单信息 */
@Entity
@Table(name = "t_preset_orders" )
public class PresetOrders {
	@Id
	@Column(length=64)
	String id; // 订单编号（普通订单编号：SC20170719001）

	@Column(columnDefinition = "date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JSONField(format="yyyy-MM-dd")
	Date expectedTime;  //预置下单时间
	@Column(length=64)
	String userId; //下单人ID
	String username; //下单人名称
	
	@Column(length=64)
	String memberId; //企业ID
	String company; //公司名称 
	String memberErpCode; // 企业ErpCode编码
	@Enumerated(EnumType.STRING)
	OrderState orderState; //订单状态 
	@Enumerated(EnumType.STRING)
	TranType tranType; //配送方式 
	@Enumerated(EnumType.STRING)
	Payway payway;  //支付方式 
	@Enumerated(EnumType.STRING)
	SettleWay settleWay; //结算方式 

	/**
	 * 收货地址 ，格式如：
	 * {"address":"民族大道","city":"武汉","county":"洪山区","first":false,"id":30,"phone":"15071397971","province":"湖北","receiver":"李波","userId":10065}
	 */
	@JSONField(serialize=false)
	@Column(columnDefinition = "varchar(500) default ''")
	String shippingInfo;
	String address="";//地址
	String city="";//市 
	String count="";//区
	String shippingInfoId=""; //地址ID
	String shippingPhone=""; //手机号
	String province=""; //省份
	String DeptId ="";//部门ID
	BigDecimal oTax = new BigDecimal(0);//总税金
	BigDecimal afterTax = new BigDecimal(0);//税收 纳税后
	
	String receiver;// 收货人
	String managerId; //专营业务员【中间表ID】 
	BigDecimal deliverFee = new BigDecimal(0); //配送费
	BigDecimal productFee = new BigDecimal(0); //商品金额
	BigDecimal promotionsReduction = new BigDecimal(0); //促销减免
	BigDecimal couponReduction = new BigDecimal(0); //优惠券抵扣
	BigDecimal otherReduction = new BigDecimal(0); //人工减免
	BigDecimal total = new BigDecimal(0); //订单总金额
	Integer needScore = 0 ; //所需积分
	String RequirementDate; //商品期望收货日期
	String ConsignmentDate; //商品预计收货日期
	@Column(columnDefinition = "varchar(1000) default ''")
	String productNote = ""; //商品备注
	@Enumerated(EnumType.STRING)
	OrderType orderType =  OrderType.SHOPPING ; //订单类型，区分商城订单和积分订单
	@JSONField(serialize=false)
	Boolean isReplace = false; //代下订单
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date();  //订单创建时间
	
	
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date cancelDate ;  //订单取消时间
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date payDate ; //付款时间
	Boolean payState = false; //支付状态
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date finishDate = new Date(); //完成时间
	
	Integer invoiceId;// 发票信息
	BigDecimal totalWeight = new BigDecimal(0); // 总重量
	BigDecimal totalVolume = new BigDecimal(0); // 总体积
	Integer totalNum = 0; //总货品数量

	@Column(columnDefinition = "varchar(1000) default ''")
	String note = ""; //订单备注

	@Column(columnDefinition = "varchar(1000) default ''")
	String leaveMsg = ""; //买家留言
	Integer serviceTimes; //服务次数
	String transactionId; // 第三方平台交易订单
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date delayTime;//延长时间 只能延长一次
	
	@JSONField(serialize=false)
	@Column(columnDefinition = "varchar(1000) default ''")
	String wxPayParams ; //微信支付统一下单参数
	
	String addressAll;
	
	String title;
	
	@JSONField(serialize=false)
	@Transient
	public String getExpectedTimeStr(){
		if (this.expectedTime!=null) {
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			return simpleDateFormat.format(this.expectedTime);
		}
		return "";
	}
	
	
	@JSONField(serialize=false)
	@Transient
	public String getTimeString() {
		return DateUtil.formatDate(this.createDate);
	}
	@JSONField(serialize=false)
	@Transient
	public String getPayTimeString() {
		return DateUtil.formatDate(this.payDate);
	}
	
	@JSONField(serialize=false)
	@Transient
	public String getPayedOrderTime(){
		String date = DateUtil.formatDate(this.payDate) ;
		if(this.getOrderState().equals(OrderState.WAIT_DELIVERY)) {
			SysParamService service = SpringContextHolder.getBean(SysParamService.class) ;
			SysParam sp = service.findParam(SysParamHelper.NON_DELIVERY_ORDER_TIME, SysParamType.ORDER_PARAM) ;
			if(sp != null && !StringUtils.isEmpty(sp.getValue())) {
				Integer x = ConvertUtils.parseInteger(sp.getValue()) ;
				Date dn = DateUtil.getNHoursAfter(this.payDate,x);
				date = DateUtil.formatDate(dn) ;
			}
		}
		
		return date ;
	}

	@JSONField(serialize=false)
	@Transient
	public BigDecimal getSubtotal() {
		BigDecimal subtotal = deliverFee.add(productFee);
		return subtotal;
	}

	@JSONField(serialize=false)
	@Transient
	public boolean getCanService(){
		boolean canService = true ;
		//通过积分兑换的货品不能进行售后
		if(OrderType.SCOREEXCHANGE.equals(orderType)) {
			return false ;
		}
		AmOrderItemService service = SpringContextHolder.getBean(AmOrderItemService.class) ;
		String sql1 = "select count(1) from AmOrderItem where orderId=? " ;
		OrderItemService oItemservice = SpringContextHolder.getBean(OrderItemService.class) ;
		String sql2 = "select count(1) from OrderItem where orderId=? " ;
		Long c1 = service.select(sql1, Long.class, id) ;
		Long c2 = oItemservice.select(sql2, Long.class, id) ;
		if( c1 ==  c2) {
			canService = false ;
		}
		return canService ;
	}
	
	
	@Transient
	public ShippingInfo getShippingInfoModel() {
		if (!StringUtils.isEmpty(shippingInfo)) {
			return JsonUtil.parse(shippingInfo, ShippingInfo.class);
		}
		return null;
	}
	
	@Transient
	public List<PresetOrderItem> getPresetOrderItem(){
		PresetOrderItemService service = SpringContextHolder.getBean(PresetOrderItemService.class) ;
		String hql = " from PresetOrderItem where orderId=? " ;
		return service.list(hql, id) ;
	}
	@Transient
	public Order getOrder(){
		Order order =new Order();
		order.setId(this.id);
		order.setUserId(this.userId);
		order.setUsername(this.username);
		order.setMemberId(this.memberId);
		order.setCompany(this.company);
		order.setMemberErpCode(this.memberErpCode);
		order.setOrderState(this.orderState);
		order.setTranType(this.tranType);
		order.setPayway(this.payway);
		order.setSettleWay(this.settleWay);
		order.setShippingInfo(this.shippingInfo);
		order.setReceiver(this.receiver);
		order.setManagerId(this.managerId);
		order.setDeliverFee(this.deliverFee);
		order.setProductFee(this.productFee);
		order.setPromotionsReduction(this.promotionsReduction);
		order.setCouponReduction(this.couponReduction);
		order.setOtherReduction(this.otherReduction);
		order.setTotal(this.total);
		order.setRequirementDate(this.RequirementDate);
		order.setConsignmentDate(this.ConsignmentDate);
		order.setProductNote(this.productNote);
		order.setIsReplace(this.isReplace);
		order.setCreateDate(this.createDate);
		order.setCancelDate(this.cancelDate);
		order.setPayDate(this.payDate);
		order.setPayState(this.payState);
		order.setFinishDate(this.finishDate);
		order.setInvoiceId(this.invoiceId);
		order.setTotalWeight(this.totalWeight);
		order.setTotalVolume(this.totalVolume);
		order.setTotalNum(this.totalNum);
		order.setNote(this.note);
		order.setLeaveMsg(this.leaveMsg);
		order.setServiceTimes(this.serviceTimes);
		order.setTransactionId(this.transactionId);
		order.setDelayTime(this.delayTime);
		order.setWxPayParams(this.wxPayParams);
		order.setAddress(this.address);
		order.setCity(this.city);
		order.setCount(this.count);
		order.setProvince(this.province);
		order.setShippingInfoId(this.shippingInfoId);
		order.setShippingPhone(this.shippingPhone);
		order.setDeptId(this.DeptId);
		order.setoTax(this.oTax);
		order.setAfterTax(this.afterTax);
		order.setOrderType(this.orderType);
		order.setNeedScore(this.needScore);
		order.setAddressAll(this.addressAll);
		return order;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	public Integer getNeedScore() {
		return needScore;
	}
	public void setNeedScore(Integer needScore) {
		this.needScore = needScore;
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

	public OrderState getOrderState() {
		return orderState;
	}
	
	@Transient
	public String getOrderStates() {
		return orderState.getName() ;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}

	public TranType getTranType() {
		return tranType;
	}

	public void setTranType(TranType tranType) {
		this.tranType = tranType;
	}

	public BigDecimal getDeliverFee() {
		return deliverFee;
	}

	public void setDeliverFee(BigDecimal deliverFee) {
		this.deliverFee = deliverFee;
	}

	public BigDecimal getProductFee() {
		return productFee;
	}

	public void setProductFee(BigDecimal productFee) {
		this.productFee = productFee;
	}

	public BigDecimal getPromotionsReduction() {
		return promotionsReduction;
	}

	public void setPromotionsReduction(BigDecimal promotionsReduction) {
		this.promotionsReduction = promotionsReduction;
	}

	public BigDecimal getCouponReduction() {
		return couponReduction;
	}

	public void setCouponReduction(BigDecimal couponReduction) {
		this.couponReduction = couponReduction;
	}

	public BigDecimal getOtherReduction() {
		return otherReduction;
	}

	public void setOtherReduction(BigDecimal otherReduction) {
		this.otherReduction = otherReduction;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public SettleWay getSettleWay() {
		return settleWay;
	}

	public void setSettleWay(SettleWay settleWay) {
		this.settleWay = settleWay;
	}

	public Payway getPayway() {
		return payway;
	}

	public void setPayway(Payway payway) {
		this.payway = payway;
	}

	public Boolean getPayState() {
		return payState;
	}

	public void setPayState(Boolean payState) {
		this.payState = payState;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getRequirementDate() {
		return RequirementDate;
	}

	public void setRequirementDate(String requirementDate) {
		RequirementDate = requirementDate;
	}

	public String getConsignmentDate() {
		return ConsignmentDate;
	}

	public void setConsignmentDate(String consignmentDate) {
		ConsignmentDate = consignmentDate;
	}

	public String getProductNote() {
		return productNote;
	}

	public void setProductNote(String productNote) {
		this.productNote = productNote;
	}
	
	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getLeaveMsg() {
		return leaveMsg;
	}

	public void setLeaveMsg(String leaveMsg) {
		this.leaveMsg = leaveMsg;
	}

	public Boolean getIsReplace() {
		return isReplace;
	}

	public void setIsReplace(Boolean isReplace) {
		this.isReplace = isReplace;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getShippingInfo() {
		return shippingInfo;
	}

	public void setShippingInfo(String shippingInfo) {
		this.shippingInfo = shippingInfo;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMemberErpCode() {
		return memberErpCode;
	}

	public void setMemberErpCode(String memberErpCode) {
		this.memberErpCode = memberErpCode;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public BigDecimal getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}

	public BigDecimal getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getServiceTimes() {
		return serviceTimes;
	}

	public void setServiceTimes(Integer serviceTimes) {
		this.serviceTimes = serviceTimes;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Date getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Date delayTime) {
		this.delayTime = delayTime;
	}
	public String getWxPayParams() {
		return wxPayParams;
	}
	public void setWxPayParams(String wxPayParams) {
		this.wxPayParams = wxPayParams;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getShippingInfoId() {
		return shippingInfoId;
	}
	public void setShippingInfoId(String shippingInfoId) {
		this.shippingInfoId = shippingInfoId;
	}
	public String getShippingPhone() {
		return shippingPhone;
	}
	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getDeptId() {
		return DeptId;
	}
	public void setDeptId(String deptId) {
		DeptId = deptId;
	}
	public BigDecimal getoTax() {
		return oTax;
	}
	public void setoTax(BigDecimal oTax) {
		this.oTax = oTax;
	}
	public BigDecimal getAfterTax() {
		return afterTax;
	}
	public void setAfterTax(BigDecimal afterTax) {
		this.afterTax = afterTax;
	}
	public String getAddressAll() {
		return addressAll;
	}
	public void setAddressAll(String addressAll) {
		this.addressAll = addressAll;
	}
	public Date getExpectedTime(){
		return this.expectedTime;
	}
	public void setExpectedTime(Date expectedTime) {
		this.expectedTime = expectedTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}