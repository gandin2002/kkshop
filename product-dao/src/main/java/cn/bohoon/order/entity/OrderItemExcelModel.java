package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.userInfo.entity.UserInfo;

public class OrderItemExcelModel {

	@ExportConfig(name = "商品编码")
	String productId;   
	@ExportConfig(name = "商品名称")
	String productName; // 真实姓名
	@ExportConfig(name = "商品单位")
	String attrAndAttrValues;
	@ExportConfig(name = "单价（元）")
	BigDecimal price = new BigDecimal(0); 
	@ExportConfig(name = "数量")
	Integer saleQuantity; 
	@ExportConfig(name = "总金额（元）")
	BigDecimal amountWithTax; 
/*	@ExportConfig(name = "重量（KG）")
	BigDecimal totalWeight;
	@ExportConfig(name = "体积（m³）")
	BigDecimal totalvolume;*/
	@ExportConfig(name = "状态")
	String orderState;
	
	
     public void setParam(OrderItem orderItem,Order order,String productId){
	
    	 this.productName = orderItem.getProductName();
    	 this.productId = productId;
    	 this.attrAndAttrValues = orderItem.getSaleUnitName();
    	 this.price = orderItem.getPrice();
    	 this.saleQuantity = orderItem.getSaleQuantity();
    	 this.amountWithTax = orderItem.getoAmountWithTax();
    	 /*this.totalWeight = orderItem.getWeight().multiply(new BigDecimal(orderItem.getQuantity()));
    	 this.totalvolume = orderItem.getVolume().multiply(new BigDecimal(orderItem.getQuantity()));*/
    	 this.orderState = order.getOrderState().getName();
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getAttrAndAttrValues() {
		return attrAndAttrValues;
	}


	public void setAttrAndAttrValues(String attrAndAttrValues) {
		this.attrAndAttrValues = attrAndAttrValues;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public Integer getSaleQuantity() {
		return saleQuantity;
	}


	public void setSaleQuantity(Integer saleQuantity) {
		this.saleQuantity = saleQuantity;
	}


	public BigDecimal getAmountWithTax() {
		return amountWithTax;
	}


	public void setAmountWithTax(BigDecimal amountWithTax) {
		this.amountWithTax = amountWithTax;
	}




	public String getOrderState() {
		return orderState;
	}


	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}


	


	
}
