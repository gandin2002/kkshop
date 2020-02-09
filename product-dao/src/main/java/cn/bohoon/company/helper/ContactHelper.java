package cn.bohoon.company.helper;

import org.springframework.util.StringUtils;

import cn.bohoon.company.domain.DailyExpenditure;
import cn.bohoon.company.domain.PurchasingCategory;

/**
 * 合约企业注册的工具类
 * @author Administrator
 *
 */

public class ContactHelper {
	
	
	/**
	 * 将字符串分割，获取对应枚举的值，并返回成一个字符串(主要采购用户类)
	 */
	public static String getPurchasingCategory(String purchasingCategory){
		
		if(StringUtils.isEmpty( purchasingCategory )){
			
			return "";
		}
		
		String[] pc = purchasingCategory.split(",");
		PurchasingCategory[] values = PurchasingCategory.values(); 
		
		String sum = "";
		for (String str : pc){
			
			for (PurchasingCategory vs : values){
				
				if (str.equals(vs.toString())){
				  
					sum = sum + vs.getName() + "、";
				}
			}
		}
		
		return sum.substring(0,sum.length()-1);
		
	}
	
	
	/**
	 * (用于日常支出方式 )
	 */
	public static String getDailyExpenditures(String dailyExpenditures){
		
		if(StringUtils.isEmpty( dailyExpenditures )){
			
			return "";
		}
		
		String[] pc = dailyExpenditures.split(",");
		DailyExpenditure[] values = DailyExpenditure.values(); 
		
		String sum = "";
		for (String str : pc){
			
			for (DailyExpenditure vs : values){
				
				if (str.equals(vs.toString())){
				  
					sum = sum + vs.getName() + "、";
				}
			}
		}
		
		return sum.substring(0,sum.length()-1);
		
	}

}
