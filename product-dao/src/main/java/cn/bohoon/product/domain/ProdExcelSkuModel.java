package cn.bohoon.product.domain;

import cn.bohoon.excel.util.ExportConfig;

public class ProdExcelSkuModel {

	// 商品编号 商品名称  商品分类 商品品牌 商品规格 单位 数量 商品售价 备注
	
	@ExportConfig(name = "货品编码")
	private String code;// 商品编码
	@ExportConfig(name = "货品名称")
	private String name; // 商品名称
	@ExportConfig(name = "货品分类")
	private String categoryName; // 商品分类
	@ExportConfig(name = "货品品牌")
	private String brandName; // 商品品牌
	@ExportConfig(name = "基本单位")
	private String unitName;
	@ExportConfig(name = "起订量")
	private String startNum;
	@ExportConfig(name = "库存预警量")
	private String inventoryHint;
	@ExportConfig(name = "市场价")
	private String displayPrice;
	@ExportConfig(name = "交易价")
	private String salesPrice;
	@ExportConfig(name = "体积(m3)")
	private String volume;
	@ExportConfig(name = "重量(KG)")
	private String weight;
	@ExportConfig(name = "货品规格")
	private String attrValues;
	@ExportConfig(name = "积分获取(%)")
	private String score;
	@ExportConfig(name = "换算率")
	private String translateRate;
	@ExportConfig(name = "换算单位")
	private String  auxUnitName;
	@ExportConfig(name = "条形码")
	private String  barCode;
	@ExportConfig(name = "货品简称")
	private String  forShort;
	
	private String mark; // 备注
	
	private String skuCode; //Sku
	
	
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getForShort() {
		return forShort;
	}
	public void setForShort(String forShort) {
		this.forShort = forShort;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getStartNum() {
		return startNum;
	}
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	public String getInventoryHint() {
		return inventoryHint;
	}
	public void setInventoryHint(String inventoryHint) {
		this.inventoryHint = inventoryHint;
	}
	public String getDisplayPrice() {
		return displayPrice;
	}
	public void setDisplayPrice(String displayPrice) {
		this.displayPrice = displayPrice;
	}
	public String getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getAttrValues() {
		return attrValues;
	}
	public void setAttrValues(String attrValues) {
		this.attrValues = attrValues;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getTranslateRate() {
		return translateRate;
	}
	public void setTranslateRate(String translateRate) {
		this.translateRate = translateRate;
	}
	public String getAuxUnitName() {
		return auxUnitName;
	}
	public void setAuxUnitName(String auxUnitName) {
		this.auxUnitName = auxUnitName;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	
	
	

}