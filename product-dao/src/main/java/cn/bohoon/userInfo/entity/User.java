package cn.bohoon.userInfo.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.CipherUtil;
import cn.bohoon.userInfo.domain.LoginUser;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.Contexts;

/**
 * 用户登录表
 * @author hj
 * 2017年11月9日,上午10:24:39
 */

@Entity
@Table(name = "t_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Column(length=64)
	String userInfoId = "" ;
	String  username;     //标识（手机号 邮箱 用户名或第三方应用的唯一标识）
	String  password;       //密码凭证（站内的保存密码，站外的不保存或保存token）
	String mobile ;         //手机号码
	String email ;          //邮箱
	String wxminiOpenid ;   //微信小程序openid
	String companyId;		//企业Id
	
	@JSONField(serialize=false)
	@Transient
	public LoginUser getLoginUser() {
		Long validTime = System.currentTimeMillis() + Contexts.COOKIE_MAX_AGE*1000; 
		UserInfoService service = SpringContextHolder.getBean(UserInfoService.class) ;
		UserInfo userInfo = service.get(userInfoId) ;
		String sign = getSign(validTime);
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername(username);
		loginUser.setEmail(email);
		loginUser.setMobile(mobile);
		loginUser.setWxminiOpenid(wxminiOpenid);
		loginUser.setUserId(userInfoId);
		if(null != userInfo) {
			loginUser.setNickName(userInfo.getNickname());
		}
		loginUser.setValidTime(validTime);
		loginUser.setSign(sign);
		return loginUser;
	}
	
	@JSONField(serialize=false)
	@Transient
	public String getSign(Long validTime){
		return CipherUtil.md5(getUsername() + getPassword() + validTime);
	}
	
	
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
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWxminiOpenid() {
		return wxminiOpenid;
	}

	public void setWxminiOpenid(String wxminiOpenid) {
		this.wxminiOpenid = wxminiOpenid;
	}

	
	
}
 