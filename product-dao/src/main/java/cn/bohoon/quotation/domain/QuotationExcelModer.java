package cn.bohoon.quotation.domain;

import cn.bohoon.excel.util.ExportConfig;
import cn.bohoon.quotation.entity.Quotation;

public class QuotationExcelModer {
	

	
	@ExportConfig(name="议价单号")
	private String id="";
	
	@ExportConfig(name="企业名称")
	private String companyName="";
	
	@ExportConfig(name="申请会员")
	private String userInfoId="";
	
	@ExportConfig(name="货品数量")
	private String productSkuNum="";
	
	@ExportConfig(name="询价")
	private String quotationSkuPrice="";
	
	@ExportConfig(name="报价")
	private String quotationPrice="";
	
	@ExportConfig(name="申请时间")
	private String createTime="";
	
	@ExportConfig(name="议价时间")
	private String quotationTime="";
	
	@ExportConfig(name="状态")
	private String quotationState="";
	
	public void setParams(Quotation quotation){
		if (quotation.getId()!=null) {
			this.id=quotation.getId();
		}
		this.companyName=quotation.getCompanyName();
		this.userInfoId=quotation.getUserInfoId();
		this.productSkuNum=quotation.getProductSkuNum().toString();
		this.quotationSkuPrice=quotation.getQuotationSkuPrice().toString();
		this.quotationPrice=quotation.getQuotationPrice().toString();
		this.createTime=quotation.getCreateTime().toString();
		if (quotation.getQuotationTime()!=null) {
			this.quotationTime=quotation.getQuotationTime().toString();
		}else{
			this.quotationTime="";
		}
		
	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	public String getProductSkuNum() {
		return productSkuNum;
	}

	public void setProductSkuNum(String productSkuNum) {
		this.productSkuNum = productSkuNum;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getQuotationTime() {
		return quotationTime;
	}

	public void setQuotationTime(String quotationTime) {
		this.quotationTime = quotationTime;
	}

	public String getQuotationState() {
		return quotationState;
	}

	public void setQuotationState(String quotationState) {
		this.quotationState = quotationState;
	}
	
	
	
}