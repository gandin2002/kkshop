package cn.bohoon.userInfo.domain;

import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.userInfo.entity.UserInfo;

public class Member {

 
	
	private UserInfo userInfo;
	
	private ShippingInfo shippingInfo ;
	
 
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public ShippingInfo getShippingInfo() {
		return shippingInfo;
	}

	public void setShippingInfo(ShippingInfo shippingInfo) {
		this.shippingInfo = shippingInfo;
	}

 
}
