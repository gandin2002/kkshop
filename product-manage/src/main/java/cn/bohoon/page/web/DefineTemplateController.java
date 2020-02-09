package cn.bohoon.page.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.page.entity.DefinePage;
import cn.bohoon.page.entity.DefineTemplate;
import cn.bohoon.page.service.DefinePageService;
import cn.bohoon.page.service.DefineTemplateService;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "defineTemplate")
public class DefineTemplateController {
	
	@Autowired
	CompanyService companyService ;
	@Autowired
	DefinePageService definePageService;
	@Autowired
	DefineTemplateService defineTemplateService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


	/**
	 * 页面模板列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);

		Page<DefineTemplate> page = new Page<DefineTemplate>();
		page.setPageNo(pageNo);
		page = defineTemplateService.page(page, "from DefineTemplate ");

		model.addAttribute("defineTemplatePage", page);
		return "defineTemplate/defineTemplateList";
	}


	/**
	 * 复制新增
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("copyNew")
	public String addPost(HttpServletRequest request, String id) throws Exception {
		DefineTemplate defineTemplate = defineTemplateService.get(id) ;
		String nid = IDUtil.getInstance().getIdByDb(defineTemplateService, DefineTemplate.class, "DT","id");
		DefineTemplate dt = new DefineTemplate() ;
		dt.setId(nid);
		dt.setCompanyId(defineTemplate.getCompanyId());
		dt.setCompanyName("复制模板");
		dt.setName("复制模板");
		dt.setMainUrl(defineTemplate.getMainUrl());
		defineTemplateService.save(dt);
		String hql = " from DefinePage where templateId =?" ;
		List<DefinePage> dps = definePageService.list(hql,id) ;
		for(DefinePage dp : dps) {
			dp.setId(null);
			dp.setTemplateId(nid);
			dp.setTemplateName("复制模板");
			dp.setTitle("复制模板");
			definePageService.save(dp);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "页面模板复制新增");
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 去编辑模板
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		DefineTemplate defineTemplate = defineTemplateService.get(id);
		List<Company> companys = companyService.list(" from Company where companySate='PASS'") ;
		
		model.addAttribute("companys", companys);
		model.addAttribute("item", defineTemplate);
		return "defineTemplate/defineTemplateEdit";
	}

	/**
	 * 编辑保存模板
	 * 
	 * @param defineTemplate
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(DefineTemplate defineTemplate,HttpServletRequest request) throws Exception {
		String id = defineTemplate.getId() ;
		String name = defineTemplate.getName() ;
		
		defineTemplateService.save(defineTemplate);
		definePageService.execute("update DefinePage set templateName=? where templateId=? ", name,id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改页面模板:"+defineTemplate.getName());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 删除模板
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, String id) throws Exception {
		defineTemplateService.delete(id);
		definePageService.execute("delete from DefinePage where templateId =?", id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除页面模板:id"+id);
		return ResultUtil.getSuccessMsg();

	}


}