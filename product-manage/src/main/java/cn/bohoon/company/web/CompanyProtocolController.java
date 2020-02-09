package cn.bohoon.company.web;

import java.util.ArrayList;
import java.util.HashMap;
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

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.quotation.entity.Quotation;
import cn.bohoon.quotation.service.QuotationService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
/*
 * 报价记录
 */
@Controller
@RequestMapping(value = "protocol")
public class CompanyProtocolController {
	    
 @Autowired
 QuotationService quotationService;
 
 @Autowired
 CompanyService companyService;
 
 @Autowired
 UserInfoService userInfoService;;
	    
 @RequestMapping(value = "list", method = RequestMethod.GET)
 public String list(HttpServletRequest request,Model model,String id) throws Exception {
	    Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
		String companyId = ServletRequestUtils.getStringParameter(request, "companyId","");
	    String startTime = ServletRequestUtils.getStringParameter(request, "startTime","");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime","");
	    Page<Quotation> pageQuotation=new Page<Quotation>(5);
		pageQuotation.setPageNo(pageNo);	
		String hql = " from Quotation q where 1 = 1  ";
        List<Object> params = new ArrayList<Object>();
        if (companyId!=null) {
			   hql += " and q.companyId=? ";
			   params.add(companyId);
		    }
        if (!StringUtils.isEmpty(id)) {
			hql = hql + " and q.id like ? ";
			params.add('%' + id + '%');	
		} 
        if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and q.createTime >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and q.createTime <= ? ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        hql+=" ORDER BY q.createTime desc ";
		pageQuotation=quotationService.page(pageQuotation, hql,params.toArray());
		Map<Quotation,Company> companyMap = new HashMap<Quotation,Company>() ;
		Map<Quotation,UserInfo> userInfoMap = new HashMap<Quotation,UserInfo>() ;
		 for(Quotation quotation:pageQuotation.getResult() ) {
			 	 UserInfo userInfo = userInfoService.get(quotation.getUserInfoId());
			 	 userInfoMap.put(quotation, userInfo) ;
			     Company company = companyService.get(quotation.getCompanyId()) ;
			     companyMap.put(quotation, company) ;
	       }
		model.addAttribute("id", id);
		model.addAttribute("companyId",companyId);
		model.addAttribute("companyMap",companyMap);
		model.addAttribute("userInfoMap",userInfoMap);
		model.addAttribute("pageQuotation", pageQuotation);
	    return "company/companyProtocol";
  }
}
