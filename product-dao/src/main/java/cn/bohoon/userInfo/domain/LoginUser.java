package cn.bohoon.userInfo.domain;

public class LoginUser {

	String username;
	String wxminiOpenid;
	String mobile;
	String email;
	String nickName;
	String userId;
	String sign;
	Long validTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSign() {
		return sign;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Long getValidTime() {
		return validTime;
	}

	public void setValidTime(Long validTime) {
		this.validTime = validTime;
	}

	public String getWxminiOpenid() {
		return wxminiOpenid;
	}

	public void setWxminiOpenid(String wxminiOpenid) {
		this.wxminiOpenid = wxminiOpenid;
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

}
