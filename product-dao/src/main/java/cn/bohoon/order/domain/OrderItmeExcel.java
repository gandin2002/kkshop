package cn.bohoon.order.domain;

import java.math.BigDecimal;

import cn.bohoon.excel.util.ExportConfig;
/**
 * 导出EXCEl 实体
 * @author HJ
 * 2017年12月25日,下午2:39:09
 */
public class OrderItmeExcel {

	@ExportConfig(name = "货品编号")
	String Id;

	@ExportConfig(name = "货品名称")
	String name;

	@ExportConfig(name = "规格")
	String unit;

	@ExportConfig(name = "品牌")
	String brand;

	@ExportConfig(name = "销售单位")
	BigDecimal unitPrice;

	@ExportConfig(name = "单品积分")
	Integer score;

	@ExportConfig(name = "购买数量")
	Integer quantity;

	public OrderItmeExcel() {
	}

	public OrderItmeExcel(String id, String name, String unit, String brand, BigDecimal unitPrice, Integer score,
			Integer quantity) {
		Id = id;
		this.name = name;
		this.unit = unit;
		this.brand = brand;
		this.unitPrice = unitPrice;
		this.score = score;
		this.quantity = quantity;
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

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
 
}
