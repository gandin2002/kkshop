package cn.bohoon.company.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.company.domain.CreditType;
import cn.bohoon.company.entity.Credit;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.company.service.CreditService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
/*
 * 信用记录
 */
@Controller
@RequestMapping(value ="credit")
public class CreditController {
  @Autowired
  CompanyService companyService;
  
  @Autowired
  CreditService creditService;
		      
  @RequestMapping(value = "list", method = RequestMethod.GET)
  public String list(HttpServletRequest request,Model model,String particular,CreditType creditType) throws Exception {
         Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
         String companyId=ServletRequestUtils.getStringParameter(request, "companyId","");
         String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
 		 String endTime = ServletRequestUtils.getStringParameter(request, "endTime","");
		 Page<Credit> pageCredit=new Page<Credit>(5);
		 pageCredit.setPageNo(pageNo);
		 String hql = " from Credit c where 1 = 1";
			List<Object> params =new ArrayList<>();
			if(!StringUtils.isEmpty(companyId)){
				hql += " and c.companyId = ?";
				params.add(companyId);
				model.addAttribute("companyId", companyId);
			} 
			if(!StringUtils.isEmpty(creditType)){
				hql += " and c.creditType = ?";
				params.add(creditType);
				model.addAttribute("creditType", creditType);
			} 
	        if(!StringUtils.isEmpty(particular)){
	        	hql = hql + " and c.particular like ?";
	        	params.add("%"+particular+"%");
	        	model.addAttribute("particular", particular);
	        }
	        if (!StringUtils.isEmpty(startTime)){
	    		hql = hql + " and c.correlationTime >= ?";
	    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
	    		model.addAttribute("startTime", startTime);
	        }
	        if (!StringUtils.isEmpty(endTime)){
	        	hql = hql + " and c.correlationTime <= ?";
	        	params.add(DateUtil.getNDayAfter(endTime, 1));
	        	model.addAttribute("endTime", endTime);
	        }	
	     hql+="ORDER BY c.correlationTime desc";
	     pageCredit=creditService.page(pageCredit, hql,params.toArray());
		 model.addAttribute("pageCredit",pageCredit);
		 
		 return "company/creditList";
   }
}
