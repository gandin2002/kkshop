package cn.bohoon.order.domain;


import cn.bohoon.excel.util.ExportConfig;

public class SendGoodsItmeExcel {
	
	@ExportConfig(name = "货品编号")
	String Id;

	@ExportConfig(name = "货品名称")
	String name;

	@ExportConfig(name = "规格")
	String unit;

	@ExportConfig(name = "品牌")
	String brand;

	@ExportConfig(name = "购买数量")
	Integer quantity;
	
	@ExportConfig(name = "发货数量")
	Integer sgnum;

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSgnum() {
		return sgnum;
	}

	public void setSgnum(Integer sgnum) {
		this.sgnum = sgnum;
	}
}
