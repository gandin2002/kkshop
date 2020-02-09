package cn.bohoon.wx.mp.domain;

import java.util.Map;

public class WxSendDateModel {
	public String name;
	
	public Map<String,String> map ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
}
