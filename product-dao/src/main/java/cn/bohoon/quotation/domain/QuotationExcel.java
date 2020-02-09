package cn.bohoon.quotation.domain;

import cn.bohoon.excel.util.ExportConfig;

public class QuotationExcel {
	

	
	@ExportConfig(name="货品SKU编号")
	private String skuId;
	
	@ExportConfig(name="货品名称")
	private String productName;

	@ExportConfig(name="货品数量")
	private String quantity;
	
	@ExportConfig(name="询价")
	private String quotationSkuPrice;
	
	@ExportConfig(name="报价")
	private String quotationPrice;
	

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getQuotationSkuPrice() {
		return quotationSkuPrice;
	}

	public void setQuotationSkuPrice(String quotationSkuPrice) {
		this.quotationSkuPrice = quotationSkuPrice;
	}

	public String getQuotationPrice() {
		return quotationPrice;
	}

	public void setQuotationPrice(String quotationPrice) {
		this.quotationPrice = quotationPrice;
	}
	
	
	
}
