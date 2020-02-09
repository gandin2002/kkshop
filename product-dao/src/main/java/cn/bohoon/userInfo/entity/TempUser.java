package cn.bohoon.userInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 临时用户表---> 用于存在组织管理生产的临时未生效的会员信息。
 * @author Administrator
 *
 */

@Entity
@Table(name = "t_user_temp")
public class TempUser {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Column(length=64)
	String userInfoId = "" ;  // 会员信息表的id
	String mobile ;         //手机号码  (登录的手机号码)
	String companyId;       // 企业id
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	

}
