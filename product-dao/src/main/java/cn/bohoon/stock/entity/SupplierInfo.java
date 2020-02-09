package cn.bohoon.stock.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 供应商信息
 * 
 * @author dujianqiao
 *
 */
@Entity
@Table(name = "t_supplier")
public class SupplierInfo {

	@Id
	@Column(length = 64)
	String id; // 主键

	@Column(length = 64)
	String name; //名称

	Integer typeId; // 类型ID
	String typeName; // 供应商类型名称

	@Column(columnDefinition = "float(6,2)")
	Float rebate = new Float(0); // 折扣
	String linkMan; // 联系人
	String phone; // 联系电话
	String faxNo; // 传真
	String mobile; // 手机号码
	String postCode; // 邮编
	String province; // 省
	String city; // 市
	String area; // 县
	String adress;// 联系地址
	String qqNum; // QQ号码
	String email; // 邮箱号码
	@Column(columnDefinition = "float(6,2)")
	Float tickRate = new Float(0); // 开票税率
	Boolean isRateCost = false; // 税额是否计入成本

	String webSite; // 网址

	@Column(columnDefinition = "tinyint(2)")
	Boolean status = true; // 状态

	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createDate = new Date(); // 创建时间
	
	String mark ; //备注
	
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

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Float getRebate() {
		return rebate;
	}

	public void setRebate(Float rebate) {
		this.rebate = rebate;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getQqNum() {
		return qqNum;
	}

	public void setQqNum(String qqNum) {
		this.qqNum = qqNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Float getTickRate() {
		return tickRate;
	}

	public void setTickRate(Float tickRate) {
		this.tickRate = tickRate;
	}

	public Boolean getIsRateCost() {
		return isRateCost;
	}

	public void setIsRateCost(Boolean isRateCost) {
		this.isRateCost = isRateCost;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
