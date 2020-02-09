package cn.bohoon.order.domain;

import java.math.BigDecimal;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.order.entity.OrderItem;

public class OrderItemReportExeclModel {
	@ExportConfig(name = "货品编号")
	String Id;

	@ExportConfig(name = "货品名称")
	String name;
	@ExportConfig(name = "购买数量")
	Integer quantity;
   //加
	@ExportConfig(name = "采购金额")
	BigDecimal amountWithTax;
	
	@ExportConfig(name = "退货数量")
	Integer backNum;
	
	@ExportConfig(name = "退货金额")
	BigDecimal backMoney;
	@ExportConfig(name = "实际采购数量")
	Integer realProNum;
	
	@ExportConfig(name = "实际采购金额")
	BigDecimal realProMoney;
	
	@ExportConfig(name = "基本单位")
	String nitName;

   public void setParams(OrderItem orderItem){
	   this.Id=orderItem.getSkuCode();
	   this.name=orderItem.getProductName();
	   this.nitName=orderItem.getUnitName();
	   this.quantity=orderItem.getQuantity();
	   this.amountWithTax=orderItem.getoAmountWithTax();
	   if(orderItem.getOrderState().equals("RETURNED_GOODS")){
		   this.backNum=0;
		   this.backMoney=new BigDecimal(0);
	   }else{
		   this.backNum=orderItem.getQuantity();
		   this.backMoney=orderItem.getoAmount();
	   }
	   this.realProNum=orderItem.getQuantity();
	   this.realProMoney=orderItem.getoAmountWithTax();
	   
   }

public String getId() {
	return Id;
}

public void setId(String id) {
	Id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Integer getQuantity() {
	return quantity;
}

public void setQuantity(Integer quantity) {
	this.quantity = quantity;
}

public BigDecimal getAmountWithTax() {
	return amountWithTax;
}

public void setAmountWithTax(BigDecimal amountWithTax) {
	this.amountWithTax = amountWithTax;
}

public Integer getBackNum() {
	return backNum;
}

public void setBackNum(Integer backNum) {
	this.backNum = backNum;
}

public BigDecimal getBackMoney() {
	return backMoney;
}

public void setBackMoney(BigDecimal backMoney) {
	this.backMoney = backMoney;
}

public Integer getRealProNum() {
	return realProNum;
}

public void setRealProNum(Integer realProNum) {
	this.realProNum = realProNum;
}

public BigDecimal getRealProMoney() {
	return realProMoney;
}

public void setRealProMoney(BigDecimal realProMoney) {
	this.realProMoney = realProMoney;
}

public String getNitName() {
	return nitName;
}

public void setNitName(String nitName) {
	this.nitName = nitName;
}
   
   
   
	
}
