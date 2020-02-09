package cn.bohoon.index.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.order.entity.OrderRate;
import cn.bohoon.order.service.OrderRateService;
import cn.bohoon.userInfo.entity.UserInRate;
import cn.bohoon.userInfo.service.UserInRateService;

@Controller
public class CalcController {

	@Autowired
	OrderRateService orderRateService ;
	@Autowired
	UserInRateService userInRateService ;
	
	@RequestMapping("/dayUserIncrementRate")
	@ResponseBody
	public String dayUserIncrementRate() {
		String hql = " from UserInRate order by id desc " ;
		List<UserInRate> rates = userInRateService.list(hql) ;
		List<Object[]> results = new ArrayList<>() ;
		for(UserInRate rate:rates) {
			Object[] obj = new Object[2] ;
			obj[0] = rate.getDatDay() ;
			obj[1] = rate.getRate() ;
			results.add(obj) ;
		}
		Collections.reverse(results) ;
		return JsonUtil.toJson(results) ;
	}
	
	
	@RequestMapping("/dayOrderIncrementRate")
	@ResponseBody
	public String dayOrderIncrementRate() {
		String hql = " from OrderRate order by id desc " ;
		List<OrderRate> rates = orderRateService.list(hql) ;
		Map<String,Object> result = new HashMap<String,Object>() ;
		int len = rates.size() ;
		List<String> categories = new ArrayList<>() ;
		List<Long> numData = new ArrayList<Long>() ;
		List<Integer> amountData = new ArrayList<Integer>() ;
		for(int i=0 ;i<len;i++) {
			if(i >6) {
				break ;
			}
			OrderRate or = rates.get(i) ;
			categories.add(or.getDat()) ;
			numData.add(or.getTransNum()) ;
			amountData.add(or.getAmount().intValue()) ;
		}
		Collections.reverse(numData) ;
		Collections.reverse(categories) ;
		Collections.reverse(amountData) ;
		
		
		result.put("numData", numData) ;
		result.put("categories", categories) ;
		result.put("amountData", amountData) ;
		
		return JsonUtil.toJson(result) ;
	}
	
	
}
