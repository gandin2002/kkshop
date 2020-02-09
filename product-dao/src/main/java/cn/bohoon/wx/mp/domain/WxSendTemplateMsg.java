package cn.bohoon.wx.mp.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class WxSendTemplateMsg {
	String touser;// "OPENID";
	String template_id;// "ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY";
	String url;// "http://weixin.qq.com/download";
	Map<String, String> miniprogram = new HashMap<String, String>();
	JSONObject data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getMap() {
		return miniprogram;
	}

	public void setMap(Map<String, String> map) {
		this.miniprogram = map;
	}
	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public void setAppid(String appId) {
		this.miniprogram.put("appId", appId);
	}

	public void setPagePath(String PagePath) {
		this.miniprogram.put("PagePath", PagePath);
	}
}
