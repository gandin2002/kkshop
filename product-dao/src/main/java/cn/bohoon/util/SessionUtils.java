package cn.bohoon.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import cn.bohoon.framework.util.CookieUtil;
import cn.bohoon.framework.util.JsonUtil;

public class SessionUtils {

	private static int default_interval = 300 ;
	
	public static void addSession(String name,Object value) {
		addSession(name,value,default_interval);
	}
	
	public static void addSession(String name,Object value,int interval) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		HttpSession session = request.getSession() ;
		session.setMaxInactiveInterval(default_interval);
		session.setAttribute(name, value);
	}
	
	public static Object getSession(String name) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		HttpSession session = request.getSession() ;
		return session.getAttribute(name) ;
	}
	
	public static void addCookie(String key,Object value) {
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		String cvalue = JsonUtil.toJson(value) ;
		try {
			cvalue = URLEncoder.encode(cvalue, "utf-8") ;
		} catch (Exception e) {
			
		}
		CookieUtil.setCookie(key, cvalue, response, Contexts.MOBILE_COOKIE_MAX_AGE, "/");
	}
	
	public static void addCookie(String key,Object value,String domain) {
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		String cvalue = JsonUtil.toJson(value) ;
		try {
			cvalue = URLEncoder.encode(cvalue, "utf-8") ;
		} catch (Exception e) {
			
		}
		CookieUtil.setCookie(key, cvalue, response, Contexts.MOBILE_COOKIE_MAX_AGE, "/",domain);
	}
	
	public static void addCookie(String key,Object value ,int scope) {
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		String cvalue = JsonUtil.toJson(value) ;
		try {
			cvalue = URLEncoder.encode(cvalue, "utf-8") ;
		} catch (Exception e) {
			
		}
		CookieUtil.setCookie(key, cvalue, response, scope, "/");
	}
	
	public static <X> X getCookie(String key,Class<X> returnClass) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		String value = CookieUtil.getCookie(request, key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (Exception e) {
			
		}
		return JsonUtil.parse(value, returnClass);
	}
	
	public static void remove(String key) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		request.getSession().invalidate();
		CookieUtil.removeCookieValue(response, key, "/");
	}
	
	public static void removeAllCookie() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		request.getSession().invalidate();
		CookieUtil.removeAllCookieValue(request, response);
	}
	
}
