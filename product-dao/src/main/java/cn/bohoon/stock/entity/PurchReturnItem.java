package cn.bohoon.stock.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 采购清单
 * 
 * @author dujianqiao
 *
 */

@Entity
@Table(name = "t_purchreturn_item")
public class PurchReturnItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id; // 主键

	@Column(length = 64)
	String purchaseId; // 退购购单ID
	Integer skuId; // SkuId
	String barCode;// 条形码
	String skuCode; // SKU 编码
	String productName; // 货品名称
	String attrName; // 货品规格
	String categoryName; // 货品分类名称
	String wareLocation; // 货品库位
	String unitName; // 货品基本单位
	Integer quantity; // 采购数量
	BigDecimal salePrice = new BigDecimal(0); // 售价
	BigDecimal costPrice = new BigDecimal(0); // 标准进价
	BigDecimal factPrice = new BigDecimal(0); // 实际进价

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getWareLocation() {
		return wareLocation;
	}

	public void setWareLocation(String wareLocation) {
		this.wareLocation = wareLocation;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getFactPrice() {
		return factPrice;
	}

	public void setFactPrice(BigDecimal factPrice) {
		this.factPrice = factPrice;
	}

}
