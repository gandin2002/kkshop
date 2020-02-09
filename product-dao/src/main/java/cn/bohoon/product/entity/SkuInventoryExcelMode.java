package cn.bohoon.product.entity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.StringUtil;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareService;

public class SkuInventoryExcelMode {
	//货品编码  货品名称  分类  品牌  售价  积分  库存量  预警量  仓库
	
	@ExportConfig(name = "货品Sku编码")
	String code=""; 
	@ExportConfig(name= "货品名称")
	String name="";
	@ExportConfig(name= "分类")
	String   category=""; 
	@ExportConfig(name= "品牌")
	String  brand="";  
	@ExportConfig(name= "售价")
	String  skuPrice=""; 
	@ExportConfig(name= "积分")
	String score="";  
	@ExportConfig(name= "库存量")
	String   quantity=""; 
	@ExportConfig(name= "预警量")
	String  inventoryHint="";
	@ExportConfig(name= "仓库")
	String wareHouse="";

	public void setParams(Sku sku){
		SkuWareService skuWareService=SpringContextHolder.getBean(SkuWareService.class);
		CategoryService categoryService=SpringContextHolder.getBean(CategoryService.class);
		this.name=sku.getName();
		this.code=sku.getCode();
		Product product=sku.getProduct();
		if (!StringUtils.isEmpty(product)) {
			if (!StringUtils.isEmpty(product.getCategoryName())) {
				this.category=product.getCategoryName();
			}
			if (!StringUtils.isEmpty(product.getBrandName())) {
				this.brand=product.getBrandName();
			}
			if (!StringUtils.isEmpty(product.getInventoryHint())) {
				this.inventoryHint=product.getInventoryHint().toString();
			}
		}
		
		Category category=categoryService.get(product.getCategoryId());
		if (!StringUtils.isEmpty(category)) {
			this.category=category.getName();
		}
		
		this.skuPrice=sku.getSkuPrice().toString();
		if (!StringUtils.isEmpty(sku.getScore())) {
			this.score=sku.getScore().toString();
		}
		if (!StringUtils.isEmpty(sku.getInventory())) {
			this.quantity=sku.getInventory().toString();
		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("skuId",sku.getId());
		List<String> result = skuWareService.list("select wareHouseName from SkuWare where skuId= :skuId",String.class,param);
		this.wareHouse=result.toString();
		
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

	public String getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getInventoryHint() {
		return inventoryHint;
	}

	public void setInventoryHint(String inventoryHint) {
		this.inventoryHint = inventoryHint;
	}

	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}
	
	
	
	


	
}
