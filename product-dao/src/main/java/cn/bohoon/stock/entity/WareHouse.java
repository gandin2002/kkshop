package cn.bohoon.stock.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 发货地址管理
 */
@Entity
@Table(name = "t_warehouse")
public class WareHouse {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id; // 发货主键id
	String erp;
	
	String typeId; // 仓库类型
	String typeName; // 仓库类型名称
    String operatorId;//创库管理员id
   
    String  houseName = "";//创库管理员姓名
	@Column(columnDefinition = "tinyint(2)")
	Integer shipAddress = 0; // 发货地址 不是默认 0 1 设为默认

	@Column(columnDefinition = "tinyint(2)")
	Integer withdrawAddress = 0; // 退货地址 不是默认 0 1 设为默认

	@Column(length = 64)
	String corporateName=""; // 仓库名称

	@Column(length = 64)
	String linkman; // 联系人
	String province=""; // 省名称
	String city= ""; // 市名称
	String county= ""; // 区名称
	String address = ""; // 详细地址
	String regionCode; // 地区邮编
	String landlinePhone=""; // 座机号码
	String mobilePhone; // 手机号码
	String remake; // 备注
	
	
	
	@Column(columnDefinition = "tinyint(2)")
	Boolean isDefault = false ; //是否默认仓库

	@Column(columnDefinition="tinyint(2)")
	Boolean status = true ; //仓库状态
	
	BigDecimal eastLongitude = new BigDecimal(0); //东经
	BigDecimal northernLatitude = new BigDecimal(0);//北纬
	
	@Column(columnDefinition = "text not null default ''")
	String intro = "";//jieshao
	 
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(Integer shipAddress) {
		this.shipAddress = shipAddress;
	}
   
	
	

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public Integer getWithdrawAddress() {
		return withdrawAddress;
	}

	public void setWithdrawAddress(Integer withdrawAddress) {
		this.withdrawAddress = withdrawAddress;
	}

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getLandlinePhone() {
		return landlinePhone;
	}

	public void setLandlinePhone(String landlinePhone) {
		this.landlinePhone = landlinePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getRemake() {
		return remake;
	}

	public void setRemake(String remake) {
		this.remake = remake;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getErp() {
		return erp;
	}

	public void setErp(String erp) {
		this.erp = erp;
	}

	public BigDecimal getEastLongitude() {
		return eastLongitude;
	}

	public void setEastLongitude(BigDecimal eastLongitude) {
		this.eastLongitude = eastLongitude;
	}

	public BigDecimal getNorthernLatitude() {
		return northernLatitude;
	}

	public void setNorthernLatitude(BigDecimal northernLatitude) {
		this.northernLatitude = northernLatitude;
	}
	
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
}
