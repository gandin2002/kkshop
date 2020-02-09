package cn.bohoon.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.hql.internal.ast.tree.FromElement;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderItem;

public class OrderExcelModel {
	
	@ExportConfig(name = "订单编号")
	private String id;
	@ExportConfig(name = "货品名称")
	private String  skuName;
	@ExportConfig(name = "单价")
	private String price;
	@ExportConfig(name = "数量")
	private String quantity;
	@ExportConfig(name = "交易数量")
	private String rowNo; 
	@ExportConfig(name = "总金额（元）")
	private String total;
	@ExportConfig(name = "订单状态")
	private String orderState;

	public void setParams(Order order){
		List<OrderItem> orderItems=order.getOrderItems();
		
		this.id = order.getId();
		//商品名称
		String skuName1="";
		for (OrderItem orderItem : orderItems) {
			skuName1+=orderItem.getProductName()+",";
		}
		if (orderItems.size()>0) {
			this.skuName=skuName1.substring(0,skuName1.length()-1);
		}else {
			this.skuName="";
		}
		//单价
		String price1="";
		for (OrderItem orderItem : orderItems) {
			price1+=orderItem.getPrice().toString()+",";
		}
		if (orderItems.size()>0) {
			this.price=price1.substring(0,price1.length()-1);
		}else {
			this.price="";
		}
		//数量
		String quantity1="";
		for (OrderItem orderItem : orderItems) {
			quantity1+=orderItem.getQuantity()+",";
		}
		if (orderItems.size()>0) {
			this.quantity=quantity1.substring(0,quantity1.length()-1);
		}else {
			this.quantity="";
		}
		//交易数量
		String rowNo1="";
		for (OrderItem orderItem : orderItems) {
			rowNo1+=orderItem.getRowNo()+",";
		}
		if (orderItems.size()>0) {
			this.rowNo=rowNo1.substring(0,rowNo1.length()-1);
		}else {
			this.rowNo="";
		}
		this.total=order.getTotal()+"";
		this.orderState=order.getOrderState().getName();

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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getRowNo() {
		return rowNo;
	}

	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}



}
