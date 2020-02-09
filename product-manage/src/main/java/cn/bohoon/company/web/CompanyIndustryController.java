package cn.bohoon.company.web;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.company.entity.CompanyIndustry;
import cn.bohoon.company.service.CompanyIndustryService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "companyIndustry")
public class CompanyIndustryController {

    @Autowired
    CompanyIndustryService companyIndustryService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model,String name)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);

        Page<CompanyIndustry> pageCompanyIndustry=new Page<CompanyIndustry>(5);
        pageCompanyIndustry.setPageNo(pageNo);
        String hql = " from CompanyIndustry c where 1 = 1 ";
        List<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(name)) {
			hql = hql + " and c.name like ? ";
			params.add('%' + name + '%');	
		}
        hql+="ORDER BY c.sort";
        pageCompanyIndustry=companyIndustryService.page(pageCompanyIndustry, hql,params.toArray());
        model.addAttribute("name", name);
        model.addAttribute("pageCompanyIndustry", pageCompanyIndustry);
        return "companyIndustry/companyIndustryList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "companyIndustry/companyIndustryAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,CompanyIndustry companyIndustry) throws Exception  {
    	companyIndustry.setCreateTime(new Date());
        companyIndustryService.save(companyIndustry);
        //保存日志,HttpServletRequest request
  		 LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "行业设置新增:"+companyIndustry.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        CompanyIndustry companyIndustry=companyIndustryService.get(id);
        model.addAttribute("item",companyIndustry);
        return "companyIndustry/companyIndustryEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(CompanyIndustry companyIndustry,HttpServletRequest request) throws Exception {
		companyIndustry.setCreateTime(new Date());
        companyIndustryService.save(companyIndustry);
        //保存日志,HttpServletRequest request
 		 LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "行业设置修改:"+companyIndustry.getName());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        companyIndustryService.delete(id);
        //保存日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "行业设置删除id:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}