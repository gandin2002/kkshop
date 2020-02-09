package cn.bohoon.company.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyLevel;
import cn.bohoon.company.service.CompanyLevelService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;

public class CompanyLevelCache {

	private static CompanyLevelCache cache;

	private Map<String,CompanyLevel> userCompMap = null ;

	
	public static CompanyLevelCache getCache(){
		if(null == cache) {
			cache = new CompanyLevelCache() ;
		}
		return cache ;
	}
	
	/**
	 * 清除缓存
	 */
	public static void cleanCache(){
		CompanyLevelCache cache  = CompanyLevelCache.getCache();
		cache.userCompMap.clear();
		cache.userCompMap = null;
		CompanyLevelCache.cache = null;
	}
	
	private CompanyLevelCache(){
		init() ;
	}

	public void init() {
		// TODO Auto-generated method stub
		userCompMap = new HashMap<String, CompanyLevel>() ;
		CompanyService companyService = SpringContextHolder.getBean(CompanyService.class) ;
		UserInfoService userInfoService = SpringContextHolder.getBean(UserInfoService.class) ;
		CompanyLevelService companyLevelService = SpringContextHolder.getBean(CompanyLevelService.class) ;
		List<UserInfo> users = userInfoService.list() ;
		for(UserInfo user : users) {
			String userId = user.getId() ;
			Company company = companyService.getCompanyByUserId(userId) ;
			if(null != company && company.getLevel() != null) {
				CompanyLevel level = companyLevelService.get(company.getLevel()) ;
				userCompMap.put(userId, level) ;
			} else {
				userCompMap.put(userId, null) ;
			}
		}
	}

	public Float getDisCount(String userInfoId){
		Float disCount = 100f;
		if(userCompMap == null){
			return disCount;
		}
		CompanyLevel level = userCompMap.get(userInfoId);
		if(null !=level && null != level.getDiscount()) {
			disCount = level.getDiscount() ;
		}
		return disCount ;
	}
}
