package cn.bohoon.product.domain;
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

public class SkuInventoryInputExcelMode {

	
	@ExportConfig(name = "货品Sku编码")
	String skuCode=""; 
	@ExportConfig(name= "仓库编号")
	String warehouseId ="";
	@ExportConfig(name= "导入数量")
	String number="";
	String mark="";//备注
	
	
	
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	} 

	
}
