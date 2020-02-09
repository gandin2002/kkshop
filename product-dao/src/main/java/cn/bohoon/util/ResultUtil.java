package cn.bohoon.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.util.JsonUtil;

public class ResultUtil {
	
	
	/**
	 * 国际化取值
	 * 
	 * @param key
	 * @return
	 */
	
	public static String getErrorMessage(String key) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", 1);
		MessageSourceUtil msu = SpringContextHolder.getBean(MessageSourceUtil.class) ;
		map.put("msg", msu.getMessage(key));
		return JsonUtil.toJson(map);
	}
	
	/**
	 * 国际化取值 成功
	 * @return
	 */
	public static String getSuccessMsg(){
		MessageSourceUtil msu = SpringContextHolder.getBean(MessageSourceUtil.class) ;
		String msg = msu.getMessage("operation.success") ;
		if(StringUtils.isEmpty(msg)) {
			msg = "操作成功！" ;
		}
		return ResultUtil.getMessage(msg) ;
	}

	
	public static String getData(Integer code,String msg,Object data){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		return JsonUtil.toJson(map);
	}
	
	
	public static String getError(String msg){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", 1);
		map.put("msg", msg);
		return JsonUtil.toJson(map);
	}
	
	public static String getMessage(String msg){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", 0);
		map.put("msg", msg);
		return JsonUtil.toJson(map);
	}
 
	
}
