package cn.bohoon.util;

import java.util.HashMap;
import java.util.Map;

import cn.bohoon.message.domain.SendType;

public class Contexts {
	
	public static final int COOKIE_MAX_AGE = 30 * 60;// 30分鈡（毫秒）
	
	public static final int MOBILE_COOKIE_MAX_AGE = 24 * 60 * 60; //前端超时时间为一天
	
	public static final String BROWSE_PRODUCT_ID_SET = "BROWSE_PRODUCT_ID_SET" ;
	
	//购物车缓存货品及货品数量信息
	public static final String SHOPPINGCART_MAP = "SHOPPINGCART_MAP" ;
	
	//购物车显示缓存总货品数量
	public static final String SHOPPINGCART_NUM = "ShoppingcartNum" ;
	
	public static Map<SendType,String> SMS_TEMPLATES = new HashMap<SendType,String>();
	
}
