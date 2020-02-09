package cn.bohoon.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "t_MymaintenanceCompany")
public class MymanenceCompany {
	
	@Id
	@Column(length=64)
	String id;// 企业ID
	//String code;// 企业code
	String name;// 企业名称
	Integer cataId = 0; // 企业分类
	Integer level=1; // 企业等级
	Integer industryId = 0;// 行业
	BigDecimal credits = new BigDecimal(0); // 企业信用额度
	//BigDecimal creditOver = new BigDecimal(0); // 企业信用余额
	
	@Column(length=64)
	String userId; // 负责人ID
	String userName; // 负责人姓名
	String province; // 省名称
	String phone; // 企业电话
	
	Integer provinceCode;//省份Code
	String city; // 市名称
	Integer cityCode;//市Code
	String county; // 区名称
	Integer countyCode;//区Code
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date regtime; // 注册日期
	String nature; // 企业性质

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date applyTime = new Date();//申请时间
	
	String  experts; //技术顾问
	
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date  ceperiodTime ;//有效期
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCataId() {
		return cataId;
	}
	public void setCataId(Integer cataId) {
		this.cataId = cataId;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getIndustryId() {
		return industryId;
	}
	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
	}
	public BigDecimal getCredits() {
		return credits;
	}
	public void setCredits(BigDecimal credits) {
		this.credits = credits;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(Integer provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getCityCode() {
		return cityCode;
	}
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public Integer getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(Integer countyCode) {
		this.countyCode = countyCode;
	}
	public Date getRegtime() {
		return regtime;
	}
	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getExperts() {
		return experts;
	}
	public void setExperts(String experts) {
		this.experts = experts;
	}
	public Date getCeperiodTime() {
		return ceperiodTime;
	}
	public void setCeperiodTime(Date ceperiodTime) {
		this.ceperiodTime = ceperiodTime;
	}
	
	

}
