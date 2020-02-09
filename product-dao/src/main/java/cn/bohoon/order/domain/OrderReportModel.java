package cn.bohoon.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.order.entity.Order;
public class OrderReportModel {
	
	@ExportConfig(name = "订单编号")
	private String id;
	@ExportConfig(name = "货品名称")
	private String  skuName;
	
	@ExportConfig(name = "总金额（元）")
	private String total;
	
	//报表统计自动
	
	@ExportConfig(name = "下单时间")
	private Date createDate;
	@ExportConfig(name = "下单人")
	private String username;
	@ExportConfig(name="下单部门")
	private  String  departmentTitle; 
	@ExportConfig(name="支付方式")
	private  String  settleWayName; 
	@ExportConfig(name="货品编码")
	private  String skuErpCode; 
	@ExportConfig(name="货品名称")
	private  String productName; 
	@ExportConfig(name="单位")
	private  String  unitName; 
	@ExportConfig(name="交易数量")
	private Integer  totalNum; 
	@ExportConfig(name="交易金额")
	private  BigDecimal  productFee; 
	

	public void setReportParams(Order order){
		this.id=order.getId();
		this.skuName=order.getOrderItem().getProductName();
		this.createDate=order.getCreateDate();
		this.username=order.getUsername();
		this.departmentTitle=order.getDepartment().getTitle();
		this.settleWayName=order.getSettleWay().getName();
		this.skuErpCode=order.getOrderItems().get(0).getSkuErpCode();
		this.productName=order.getOrderItem().getProductName();
		this.unitName=order.getOrderItem().getUnitName();
		this.totalNum=order.getTotalNum();
		this.productFee=order.getProductFee();
		this.total=order.getTotal()+"";
		
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getSkuName() {
		return skuName;
	}


	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}


	public String getTotal() {
		return total;
	}


	public void setTotal(String total) {
		this.total = total;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getDepartmentTitle() {
		return departmentTitle;
	}


	public void setDepartmentTitle(String departmentTitle) {
		this.departmentTitle = departmentTitle;
	}


	public String getSettleWayName() {
		return settleWayName;
	}


	public void setSettleWayName(String settleWayName) {
		this.settleWayName = settleWayName;
	}


	public String getSkuErpCode() {
		return skuErpCode;
	}


	public void setSkuErpCode(String skuErpCode) {
		this.skuErpCode = skuErpCode;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getUnitName() {
		return unitName;
	}


	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
	
	
	
}
