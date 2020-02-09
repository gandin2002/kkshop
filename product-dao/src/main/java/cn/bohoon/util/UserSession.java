package cn.bohoon.util;

import cn.bohoon.userInfo.domain.LoginUser;


public class UserSession {

	public static final String COOKIE_USER = "_user";
	public static final String COOKIE_COMPANY_STATE = "_company_state" ;
	public static final String COOKIE_USER_RESP = "_user_wether_company_resp";
	
	
	
	public static void addLoginUser(LoginUser loginUser){
			// 将要被保存的完整的Cookie值
		SessionUtils.addCookie(COOKIE_USER, loginUser);
	}
	
	
	public static LoginUser getLoginUser() {
		return SessionUtils.getCookie(COOKIE_USER, LoginUser.class) ;
	}
	
	public static void removeLoginUser(){
		SessionUtils.remove(COOKIE_USER);
		SessionUtils.remove(COOKIE_USER_RESP) ;
	}

	public static void addLoginUser(LoginUser loginUser, String domain) {
		// TODO Auto-generated method stub
		SessionUtils.addCookie(COOKIE_USER, loginUser,domain);
		SessionUtils.addCookie(COOKIE_USER_RESP, true,domain);
	}
	
	
	public static void addLoginUserFlag(boolean isResp){
		// 将要被保存的完整的Cookie值
		SessionUtils.addCookie(COOKIE_USER_RESP, isResp);
	}
	public static boolean getLoginUserFlag() {
		Object obj = SessionUtils.getCookie(COOKIE_USER_RESP, Boolean.class) ;
		boolean flag = false ;
		if(null != obj) {
			flag = Boolean.valueOf(obj+"") ;
		}
		return flag ;
	}
	
	
	
	public static void addLoginCompanyState(boolean state){
		// 将要被保存的完整的Cookie值
		SessionUtils.addCookie(COOKIE_COMPANY_STATE, state);
	}
	
	public static boolean getLoginCompanyState() {
		Object obj = SessionUtils.getCookie(COOKIE_COMPANY_STATE, Boolean.class) ;
		boolean flag = false ;
		if(null != obj) {
			flag = Boolean.valueOf(obj+"") ;
		}
		return flag ;
	}
	
}
