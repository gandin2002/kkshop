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

import cn.bohoon.company.entity.CompanyLevel;
import cn.bohoon.company.service.CompanyLevelService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "companyLevel")
public class CompanyLevelController {

    @Autowired
    CompanyLevelService companyLevelService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model,String name)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);

        Page<CompanyLevel> pageCompanyLevel=new Page<CompanyLevel>(5);
        pageCompanyLevel.setPageNo(pageNo);
        
        String hql = " from CompanyLevel c where 1 = 1 ";
        List<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(name)) {
			hql = hql + " and c.name like ? ";
			params.add('%' + name + '%');	
		}
        hql+=" ORDER BY c.createTime desc";
        pageCompanyLevel=companyLevelService.page(pageCompanyLevel, hql,params.toArray());
        model.addAttribute("name", name);
        model.addAttribute("pageCompanyLevel", pageCompanyLevel);
        return "companyLevel/companyLevelList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "companyLevel/companyLevelAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,CompanyLevel companyLevel) throws Exception  {
    	companyLevel.setCreateTime(new Date());
        companyLevelService.save(companyLevel);
        //保存日志,HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增企业等级:"+companyLevel.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        CompanyLevel companyLevel=companyLevelService.get(id);
        model.addAttribute("item",companyLevel);
        return "companyLevel/companyLevelEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(CompanyLevel companyLevel,HttpServletRequest request) throws Exception {
		companyLevel.setCreateTime(new Date());
        companyLevelService.update(companyLevel);
        //保存日志,HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改企业等级:"+companyLevel.getName());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        companyLevelService.delete(id);
        //保存日志,HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除企业等级id:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}