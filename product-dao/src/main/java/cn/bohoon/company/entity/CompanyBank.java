package cn.bohoon.company.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_company_bank")
public class CompanyBank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; // 信息ID
	String companyId; // 企业ID
	String bankNum; // 银行卡号
	String bankName; // 开户银行
	String province; // 省份
	String city; // 城市
	String county;// 地区
	String subbranch;// 支行
	@Column(columnDefinition="tinyint(2)")
	Boolean isDefault = false; // 是否默认

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBankNum() {
		return bankNum;
	}

	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getSubbranch() {
		return subbranch;
	}

	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

}
