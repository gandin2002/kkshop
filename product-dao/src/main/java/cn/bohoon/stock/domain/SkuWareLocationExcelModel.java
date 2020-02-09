package cn.bohoon.stock.domain;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.SkuWareLocation;
import cn.bohoon.product.service.SkuService;

public class SkuWareLocationExcelModel {
	// 启用状态   货品编号   货品条码   货品名称  规格  仓库  库位 是否默认库位  
	@ExportConfig(name = "启用状态")
	private String status;//启用状态
	@ExportConfig(name = "货品ID")
	private String skuId;//货品ID
	@ExportConfig(name = "货品条码")
	private String barCode;//货品条码
	@ExportConfig(name = "货品名称")
	private String skuName; //货品名称
	@ExportConfig(name = "规格")
	private String attrName;//货品规格
	@ExportConfig(name = "仓库")
	private String wareHouseName;//仓库名称、
	@ExportConfig(name = "库位")
	private String localtionName;//库位名称
	@ExportConfig(name = "是否默认库位")
	private String isDefault;//是否 默认库位

	public void setParams(SkuWareLocation skuware){
		
	}
}
