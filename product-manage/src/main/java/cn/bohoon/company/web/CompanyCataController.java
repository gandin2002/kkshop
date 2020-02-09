package cn.bohoon.company.web;



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

import cn.bohoon.company.entity.CompanyCata;
import cn.bohoon.company.service.CompanyCataService;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "companyCata")
public class CompanyCataController {

    @Autowired
    CompanyCataService companyCataService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model)  {
    	List<CompanyCata> cataList = companyCataService.categorysSorting();
		model.addAttribute("cataList", cataList);
        return "companyCata/companyCataList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model, Integer id)  {
    	if (!StringUtils.isEmpty(id)) {
    		CompanyCata cata = companyCataService.get(id);
    		cata.setLevel(cata.getLevel()+1);
			model.addAttribute("cata", cata);
		}
        return "companyCata/companyCataAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,CompanyCata companyCata) throws Exception  {
    	companyCata.setCreateTime(new Date());
        companyCataService.save(companyCata);
        //新增日志HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增企业分类:"+companyCata.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        CompanyCata companyCata=companyCataService.get(id);
        model.addAttribute("item",companyCata);
        return "companyCata/companyCataEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(CompanyCata companyCata,HttpServletRequest request) throws Exception {
		companyCata.setCreateTime(new Date());
        companyCataService.save(companyCata);
        //保存日志,HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改企业分类:"+companyCata.getName());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        companyCataService.delete(id);
        //保存日志,HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除企业分类id:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}