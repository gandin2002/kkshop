package cn.bohoon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertUtils {
	
	static Logger logger = LoggerFactory.getLogger(ConvertUtils.class) ;
	
	public static Integer parseInteger(String s) {
		Integer result = 0 ;
		try {
			result = Integer.parseInt(s) ;
		} catch (Exception e){
			logger.error("convert str to int error,message ==="+e.getMessage());
		}
		return result ;
	}
	
	public static Integer[] parseIntArr(String ids) {
		String [] strIds = ids.split(",") ;
		Integer [] result = new Integer[strIds.length] ;
		for(int i=0 ;i<strIds.length;i++) {
			String s = strIds[i] ;
			result[i] = parseInteger(s);
		}
		return result ;
		
	}
}
