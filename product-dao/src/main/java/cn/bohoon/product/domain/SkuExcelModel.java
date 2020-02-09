package cn.bohoon.product.domain;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;

public class SkuExcelModel {

	@ExportConfig(name="货品编码")
	String code;
	@ExportConfig(name="SKU")
	String skuCode;
	@ExportConfig(name="货品名称")
	String name;
	@ExportConfig(name="分类")
	String category;
	@ExportConfig(name="品牌")
	String brand;
	@ExportConfig(name="售价")
	String price;
	@ExportConfig(name="库存")
	String inventory;
	@ExportConfig(name="销量")
	String volume;
	@ExportConfig(name="标签")
	String tag;
	@ExportConfig(name = "采购数量")
	String quantity = "";
	@ExportConfig(name = "辅助计量")
	String unitNum = "";
	@ExportConfig(name="总价")
	String  totalPrice = "";
	
	public SkuExcelModel() {
	}
	public SkuExcelModel(Sku sku,Product product) {
		
		code = product.getCode();
		skuCode = sku.getCode();
		name = product.getName();
		category = product.getCategoryName();
		brand = product.getBrandName();
		price = sku.getSkuPrice().toString();
		if(sku.getInventory()!=null){
			inventory =  sku.getInventory().toString();
		}
		if(sku.getVolume() != null){
			volume = sku.getVolume().toString();
		}
		tag = product.getLables();
		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getUnitNum() {
		return unitNum;
	}
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
}
