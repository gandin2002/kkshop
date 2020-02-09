package cn.bohoon.product.domain;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.service.SkuService;

public class ProdExcelModel {

	// 商品编号 商品名称  商品分类 商品品牌 商品规格 单位 数量 商品售价 备注
	
	@ExportConfig(name = "商品编码")
	private String code;// 商品编码
	@ExportConfig(name = "商品名称")
	private String name; // 商品名称
	@ExportConfig(name = "商品分类")
	private String category; // 商品分类
	@ExportConfig(name = "商品品牌")
	private String brand; // 商品品牌
	private String proAttr;// 商品规格
	private String unitName; // 单位名称
	@ExportConfig(name = "库存")
	private String inventorys; // 库存
	@ExportConfig(name = "售价")
	private String skuPrice; // 售价

	private String mark; // 备注

	@ExportConfig(name = "销量")
	private String salesNum;// 销量
	
	@ExportConfig(name = "标签")
	private String tag;
	
	private String skuCode; //Sku
	
	private String score;//积分
	
	public void setParams(Product product){
		this.code = product.getCode();
		this.name = product.getName();
		this.brand=product.getBrandName();
		this.category=product.getCategoryName();
		this.skuPrice=product.getSalesPrice().toString();
		this.salesNum=product.getSalesNum().toString();
		this.tag=product.getLables();
		SkuService skuService= SpringContextHolder.getBean(SkuService.class);
		Long inventory = skuService.select("select sum(inventory) from Sku where productId = ? and status = 1 ", Long.class,product.getId());
		if(inventory!= null){
			this.inventorys = inventory.toString();
		}
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
	public String getProAttr() {
		return proAttr;
	}
	public void setProAttr(String proAttr) {
		this.proAttr = proAttr;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getInventorys() {
		return inventorys;
	}
	public void setInventorys(String inventorys) {
		this.inventorys = inventorys;
	}
	public String getSkuPrice() {
		return skuPrice;
	}
	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getSalesNum() {
		return salesNum;
	}
	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
}
