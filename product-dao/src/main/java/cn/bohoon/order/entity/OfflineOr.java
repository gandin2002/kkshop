package cn.bohoon.order.entity;
  //线下订单统计表

import java.math.BigDecimal;
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
import cn.bohoon.company.entity.CompanyDepartment;
import cn.bohoon.company.service.CompanyDepartmentService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.OrderType;
import cn.bohoon.order.domain.Payway;
import cn.bohoon.order.domain.SettleWay;
import cn.bohoon.order.domain.TranType;
import cn.bohoon.order.service.AmOrderItemService;
import cn.bohoon.order.service.OfflineOrderService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.serve.entity.Serve;
import cn.bohoon.serve.service.ServeService;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.util.ConvertUtils;
@Entity
@Table(name = "t_offlinOr" )
public class OfflineOr {
	@Id
	@Column(length=64)
	String id; // 订单编号（普通订单编号：SC20170719001）
	@Column(length=64)
	String userId; //下单人ID
	String username; //下单人名称
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date();  //下单时间
	
	@Column(length=64)
	String memberId; //企业ID
	String company; //公司名称 
	String memberErpCode; // 企业ErpCode编码
	@Enumerated(EnumType.STRING)
	SettleWay settleWay; //结算方式
	Integer totalNum = 0; //交易数量
	BigDecimal productFee = new BigDecimal(0); //订单金额
	BigDecimal total = new BigDecimal(0); //订单总金额

	String DeptId ="";//部门ID
    String deptName;   //下单部门
    
    //获取第一个订单详情
	@Transient
	public  OfflineOrder getOrderItem(){
		OfflineOrderService service = SpringContextHolder.getBean(OfflineOrderService.class) ;
		String hql = " from OfflineOrder where orderId=? " ;
		List<OfflineOrder> list = service.list(hql, id) ;
		return list.get(0);
	}
	@Transient
	public List<OfflineOrder> getOrderItems(){
		OfflineOrderService service = SpringContextHolder.getBean(OfflineOrderService.class) ;
		String hql = " from OfflineOrder where orderId=? " ;
		return service.list(hql, id) ;
	}
	
	
	@Transient
	public CompanyDepartment getDepartment() {
		if (!StringUtils.isEmpty(DeptId)&& !"".equals(DeptId)) {
	 CompanyDepartmentService service = SpringContextHolder.getBean(CompanyDepartmentService.class);
			CompanyDepartment companyDepartment = service.get(ConvertUtils.parseInteger(DeptId));
			
			
			return companyDepartment;
		}
		return null;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public SettleWay getSettleWay() {
		return settleWay;
	}
	public void setSettleWay(SettleWay settleWay) {
		this.settleWay = settleWay;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public BigDecimal getProductFee() {
		return productFee;
	}
	public void setProductFee(BigDecimal productFee) {
		this.productFee = productFee;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public String getDeptId() {
		return DeptId;
	}
	public void setDeptId(String deptId) {
		DeptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
    
	
}
