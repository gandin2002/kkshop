package cn.bohoon.quotation.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//阶梯价格

@Entity
@Table(name = "t_tieredPrice")
public class TieredPrice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private   Integer id;//阶梯结果Id
	private  Integer CompanyId;//ID
	private  String  productCode; //产品code
	private  String startNum; //左区间数量
	private  String   endNum;  //右区间数量
	private   BigDecimal   companyVipPrice; //企业折扣价
	private   BigDecimal   customerPrice;  //自定义价格
	private   String  remark;   //备注
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCompanyId() {
		return CompanyId;
	}
	public void setCompanyId(Integer companyId) {
		CompanyId = companyId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getStartNum() {
		return startNum;
	}
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	public String getEndNum() {
		return endNum;
	}
	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}
	public BigDecimal getCompanyVipPrice() {
		return companyVipPrice;
	}
	public void setCompanyVipPrice(BigDecimal companyVipPrice) {
		this.companyVipPrice = companyVipPrice;
	}
	public BigDecimal getCustomerPrice() {
		return customerPrice;
	}
	public void setCustomerPrice(BigDecimal customerPrice) {
		this.customerPrice = customerPrice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public TieredPrice() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TieredPrice(Integer id, Integer companyId, String productCode, String startNum, String endNum,
			BigDecimal companyVipPrice, BigDecimal customerPrice, String remark) {
		super();
		this.id = id;
		CompanyId = companyId;
		this.productCode = productCode;
		this.startNum = startNum;
		this.endNum = endNum;
		this.companyVipPrice = companyVipPrice;
		this.customerPrice = customerPrice;
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "TieredPrice [id=" + id + ", CompanyId=" + CompanyId + ", productCode=" + productCode + ", startNum="
				+ startNum + ", endNum=" + endNum + ", companyVipPrice=" + companyVipPrice + ", customerPrice="
				+ customerPrice + ", remark=" + remark + "]";
	}
	
	
	
	
}
