package cn.bohoon.excel.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.product.domain.ProdExcelModel;
import cn.bohoon.product.service.UnitService;
import cn.bohoon.userInfo.entity.UserInfo;

/**
 * 解析员工
 * @author Administrator
 *
 */

public class ExcelEmployee {
	
	
	public static List<UserInfo> getExcel(InputStream file) throws Exception {
		
		
		List<List<String>> list = ExcelRead.readExcel(file);
		List<UserInfo> userInfoList = new ArrayList<>();
		
		
		
		for (List<String>  li : list){
			
			
			if (StringUtils.isEmpty( li.get(0).trim() )){
				continue;
			}
			
			String phone = li.get(0);
			
			if (phone.length() != 11){
				
				continue;
			}
			
//			phone = phone.substring(0, 11);
			String regex = "^1[0-9]\\d{9}$";
			boolean flag = phone.matches(regex);
			
			// 则说明是正确的手机号
			if (flag){
				UserInfo userInfo = new UserInfo();
				userInfo.setPhone(phone);
				userInfo.setRealname(li.get(1));
				
				userInfoList.add(userInfo);
			}
			
			
		}
		
		return userInfoList;
	}
	

	

}
