package cn.bohoon.order.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.order.service.OrderSalesSumService;

/**
 * 账房 >收支查询 >销售额汇总
 */
@Controller
@RequestMapping(value = "orderSalesSum")
public class OrderSalesSumController {
	
	private final String SUM_MONTH = "SUM_MONTH";
	private final String SUM_DAY = "SUM_DAY";

	@Autowired
    OrderSalesSumService orderSalesSumService;
    
    @Autowired
    CompanyService companyService;
    
    @RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws ParseException {
		Integer year = ServletRequestUtils.getIntParameter(request, "year",0);
		
		if(year == 0){
			year = Calendar.getInstance().get(Calendar.YEAR);
		}
		
		List<Map<String,Object>> sumList = orderSalesSumService.monthList(year);	
		
		model.addAttribute("SUM_TYPE", this.SUM_MONTH);
		model.addAttribute("year", year);
		model.addAttribute("yearList", getYear(50,50));
		model.addAttribute("sumList", sumList);
		
		return "orderSalesSum/orderSalesSumList";
	}
 
    @RequestMapping(value = "dayList", method = RequestMethod.GET)
	public String dayList(HttpServletRequest request, Model model) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
        String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
        
        Page<Map<String,Object>> pageDaySum = new Page<Map<String,Object>>();
        pageDaySum.setPageNo(pageNo);
        
        if(StringUtils.isEmpty(startTime)){
        	startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if(StringUtils.isEmpty(endTime)){
        	endTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
		
        pageDaySum = orderSalesSumService.pageDay(pageDaySum,startTime,endTime);
		
		model.addAttribute("SUM_TYPE", this.SUM_DAY);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("pageDaySum", pageDaySum);
		return "orderSalesSum/orderSalesSumDayList";
	}
    
    private static List<Integer> getYear(int before,int after){
    	List<Integer> yearList = new ArrayList<>();
    	
    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -before);
		int startYear = calendar.get(Calendar.YEAR);
		calendar.add(Calendar.YEAR, before+after);
		int endYear = calendar.get(Calendar.YEAR);
		
		for (int i=startYear,j=endYear;i<=j;i++) { 
			yearList.add(i);
		}
    	return yearList;
    }
}