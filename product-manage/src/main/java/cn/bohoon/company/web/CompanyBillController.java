package cn.bohoon.company.web;

import java.util.ArrayList;
import java.util.List;

/*
 * 账单记录
 */

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyBill;
import cn.bohoon.company.service.CompanyBillService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;

@Controller
@RequestMapping(value = "companyBill")
public class CompanyBillController {

    @Autowired
    CompanyBillService companyBillService;
    
    @Autowired
    CompanyService companyService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model,String billcode)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String companyId = ServletRequestUtils.getStringParameter(request, "companyId","");
        Page<CompanyBill> pageCompanyBill=new Page<CompanyBill>(5);
        pageCompanyBill.setPageNo(pageNo);
        String hql = " from CompanyBill where 1 = 1 ";
        List<Object> params = new ArrayList<Object>();
        if (companyId!=null) {
			hql = hql + "  and companyId=?";
			params.add(companyId);	
	        model.addAttribute("companyId", companyId);
		} 
        if (!StringUtils.isEmpty(billcode)) {
			hql = hql + " and billcode like ? ";
			params.add('%' + billcode + '%');	
			model.addAttribute("billcode",billcode);
		}
        hql+="ORDER BY id";
        pageCompanyBill=companyBillService.page(pageCompanyBill, hql,params.toArray());
        Company company=companyService.get(companyId);
        model.addAttribute("company",company);
        model.addAttribute("pageCompanyBill", pageCompanyBill);
        return "company/billList";
    }
    
//    @RequestMapping(value = "add", method = RequestMethod.GET)
//    public String addGet(HttpServletRequest request,Model model)  {
//        return "company/billList";
//    }
//    
//    @ResponseBody
//    @RequestMapping(value = "add", method = RequestMethod.POST)
//    public String addPost(HttpServletRequest request,CompanyBill companyBill)  {
//        companyBillService.save(companyBill);
//		return ResultUtil.getSuccessMsg();
//    }
//    
//    @RequestMapping(value = "edit", method = RequestMethod.GET)
//    public String editGet(HttpServletRequest request,Model model) {
//        String id=ServletRequestUtils.getStringParameter(request, "id","");
//        CompanyBill companyBill=companyBillService.get(id);
//        model.addAttribute("item",companyBill);
//        return "companyBill/companyBillEdit";
//    }
//
//	  @ResponseBody
//    @RequestMapping(value = "edit", method = RequestMethod.POST)
//    public String editPost(CompanyBill companyBill) {
//        companyBillService.save(companyBill);
//        return ResultUtil.getSuccessMsg();
//    }
}