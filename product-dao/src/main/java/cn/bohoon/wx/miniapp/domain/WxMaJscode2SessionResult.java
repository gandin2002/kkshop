package cn.bohoon.wx.miniapp.domain;

import cn.bohoon.framework.util.JsonUtil;

public class WxMaJscode2SessionResult {

	private String sessionKey;

	private Integer expiresin;

	private String openid;

	private String unionid;

	public static WxMaJscode2SessionResult fromJson(String json) {
		System.out.println("---------------------------------"+json+"----------------------------------");
		return JsonUtil.parse(json, WxMaJscode2SessionResult.class);
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Integer getExpiresin() {
		return expiresin;
	}

	public void setExpiresin(Integer expiresin) {
		this.expiresin = expiresin;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

}
