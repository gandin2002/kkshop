package cn.bohoon.basicSetup.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.SpringContextHolder;

/**
 * 系统参数缓存信息
 * @author djq
 *
 */
public class SysParamCache {

	private static SysParamCache cache ;
	
	private static Map<String,SysParam> sysMap = null  ;
	
	private static Map<String,String> sysMapV = null  ;
	
	private SysParamCache(){
		init() ;
	}
	
	public static SysParamCache getCache() {
		if(null == cache) {
			cache = new SysParamCache() ;
		}
		return cache ;
	}
	
	public void init() {
		sysMap = new HashMap<String,SysParam>() ;
		sysMapV = new HashMap<String,String>() ; 
		SysParamService service = SpringContextHolder.getBean(SysParamService.class) ;
		List<SysParam> params = service.list() ;
		for(SysParam param :params) {
			sysMap.put(param.getCode(), param) ;
			sysMapV.put(param.getCode(), param.getValue()) ;
		}
	}
	
	/**
	 * 通过参数CODE 获取参数值
	 * 
	 * @param code
	 * @return
	 */
	public String getSysParamValue(String code) {
		return  sysMapV.get(code) ;
	}
	
	/**
	 * 获取所有参数值 以 key - value 形式
	 * 
	 * @return
	 */
	public Map<String,String> getSysParamsV(){
		return sysMapV ;
	}
}
