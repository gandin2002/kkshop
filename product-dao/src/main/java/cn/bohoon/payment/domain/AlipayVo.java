package cn.bohoon.payment.domain;

public class AlipayVo {
	
	String partner; //合作身份者ID
	String notify_url; //服务器异步通知页面路径
	String return_url; // 页面跳转步通知页面路径
	String private_key; //私钥
	String public_key ; //公钥
	
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getPublic_key() {
		return public_key;
	}
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	
	
	

}
