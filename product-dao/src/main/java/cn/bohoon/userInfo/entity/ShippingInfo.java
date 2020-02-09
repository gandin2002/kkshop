package cn.bohoon.userInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 默认收货人
 * 
 * @author hj 2017年11月9日,上午10:24:27
 */
@Entity
@Table(name = "t_shipping_info")
public class ShippingInfo {

	@Id
	String id;
	/* 用户ID* */
	@Column(length=64)
	String userId;
	//公司ID
	String companyId;
	/* 收 货 人 */
	String receiver;
	/* 省区 */
	String province;
	/* 城市 */
	String city;
	/* 地区 */
	String county;
	/* 邮编 */
	String postCode;
	/* 联系地址 */
	String address;
	/* 手机 */
	String phone;
	/* 默认地址 */
	@Column(columnDefinition="tinyint(2)")
	Boolean first = false;

	Integer rowNo = 0; //个数
	
	String addressInfo = ""; //地址组合
	
	String erpCode;//erpCode
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public String getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}
}
