package cn.bohoon.quotation.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.quotation.domain.QuotationState;
import cn.bohoon.quotation.service.QuotationService;

//报价单商品明细
@Entity
@Table(name = "t_quotation_item")
public class QuotationItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String quotationId; //报价单ID
	private Integer skuId;//货品SKU编号
	private Integer productId ; //商品ID
	private Integer quantity = 1;//货品数量
	private BigDecimal quotationSkuPrice;//询价时SKU总价格
	private BigDecimal quotationPrice;//报价总价格
	@Column(columnDefinition="tinyint(2)")
	private Boolean modify = false;//是否修改过

	private String userInfoId;//申请会员ID
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date ValidDate;    //报价单有效期【截止日期】
	@Enumerated(EnumType.STRING)
	private QuotationState quotationState;//状态【未报价/已报价/已通过/未通过】
	private String unitErpCode;//erpCode  unit
	private String companyId;//企业ID
	private String erpCode;//erp Code
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(String quotationId) {
		this.quotationId = quotationId;
	}
	
	@Transient
	public Quotation getQuotation() {
		if(null != quotationId &&!"".equals(quotationId)) {
			QuotationService service = SpringContextHolder.getBean(QuotationService.class) ;
			Quotation quotation =  service.get(quotationId) ;
			return quotation;
		}
		return null ;
	}
	
	public Integer getSkuId() {
		return skuId;
	}
	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getQuotationSkuPrice() {
		return quotationSkuPrice;
	}
	public void setQuotationSkuPrice(BigDecimal quotationSkuPrice) {
		this.quotationSkuPrice = quotationSkuPrice;
	}
	public BigDecimal getQuotationPrice() {
		return quotationPrice;
	}
	public void setQuotationPrice(BigDecimal quotationPrice) {
		this.quotationPrice = quotationPrice;
	}
	public Boolean getModify() {
		return modify;
	}
	public void setModify(Boolean modify) {
		this.modify = modify;
	}
	public String getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	public Date getValidDate() {
		return ValidDate;
	}
	public void setValidDate(Date validDate) {
		ValidDate = validDate;
	}
	public QuotationState getQuotationState() {
		return quotationState;
	}
	public void setQuotationState(QuotationState quotationState) {
		this.quotationState = quotationState;
	}
	public String getUnitErpCode() {
		return unitErpCode;
	}
	public void setUnitErpCode(String unitErpCode) {
		this.unitErpCode = unitErpCode;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getErpCode() {
		return erpCode;
	}
	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}
}
