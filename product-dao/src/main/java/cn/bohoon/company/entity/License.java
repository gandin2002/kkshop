package cn.bohoon.company.entity;
/*
 * 营业执照信息
 */

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 营业执照
 * @author HJ
 * 2018年4月3日,下午2:23:23
 */
@Entity
@Table(name = "t_License")
public class License {
	@Id
	String id;                         //主键
	String companyid;                  //企业id
    String registration;                //注册号
    String image;                       //执照电子版
    @Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    Date startTime;                     //开始时间
    @Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    Date endTime;                       //结束时间
    Double capital;                     //注册资本
    String province;                    //省名称
    String city;                        //市名称
    String county;                      //区名称
    String address;                     //详细地址  
    String name;                        //法人姓名
    String idCard;                      //身份证号
    String idCardImage;                 //身份证电子版
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Double getCapital() {
		return capital;
	}
	public void setCapital(Double capital) {
		this.capital = capital;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getIdCardImage() {
		return idCardImage;
	}
	public void setIdCardImage(String idCardImage) {
		this.idCardImage = idCardImage;
	}
    
    
}
