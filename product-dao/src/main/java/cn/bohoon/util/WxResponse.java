package cn.bohoon.util;

import java.util.HashMap;
import java.util.Map;

import cn.bohoon.framework.util.JsonUtil;

public class WxResponse {

	private String message = "成功";

	private int state = 0;

	private Map<String, Object> result = new HashMap<String, Object>();
	
	public WxResponse() {
		super();
	}

	public WxResponse(String message, int status) {
		this.message = message;
		this.state = status;
	}

	public void add(String key, Object value) {
		if (null == result) {
			result = new HashMap<String, Object>();
		}
		result.put(key, value);
	}
	
    public String jsonRepresent() {
        result.put("message", message);
        result.put("state", state);
        return JsonUtil.toJson(result) ;
    }

}
