package cn.bohoon.company.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyDepartment;
import cn.bohoon.company.entity.CompanyMemberLog;
import cn.bohoon.company.service.CompanyDepartmentService;
import cn.bohoon.company.service.CompanyMemberLogService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value="companyMember")
public class CompanyMemberController {
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	CompanyMemberLogService companyMemberLogService;
	
	@Autowired
	CompanyDepartmentService companyDepartmentService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	
	/**
	 * 企业员工列表
	 * 
	 * @param request
	 * @param model
	 * @param companyId
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param principal
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="list",method=RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, String companyId, String userId, String startTime,
			String endTime,@RequestParam(name="principal",required=false,defaultValue="true")Boolean principal,String companyState) throws ParseException {

		Integer pageNo= ServletRequestUtils.getIntParameter(request, "pageNo", 1); 
		Page<UserInfo> userInfoPage =new Page<UserInfo>();
		userInfoPage.setPageNo(pageNo);
		
		List<Object> params = new ArrayList<>();
		String hql = "select u from UserInfo as u,Company as c where u.companyId = c.id";
		
		if(principal!=null){
			if(principal){
				hql +=" and c.userId = u.id ";
			}else{
				hql +=" and c.userId != u.id ";
			}
		}
		if(!StringUtils.isEmpty(companyId)&&!" ".equals(companyId)){
			hql +=" and u.companyId = ? ";
			params.add(companyId);
			model.addAttribute("companyId", companyId);
		}
		if(!StringUtils.isEmpty(userId)){
			hql +=" and( u.id like ? or u.phone like ?)";
			params.add("%"+userId+"%");
			params.add("%"+userId+"%");
			model.addAttribute("userId",userId);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and u.bindingDate >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and u.bindingDate < ? ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        if(!StringUtils.isEmpty(companyState)){
        	hql = hql + " and u.companyState = "+companyState;
        	model.addAttribute("companyState", companyState);
        }
		
		
		userInfoPage = userInfoService.page(userInfoPage,hql,params.toArray());
		Map<UserInfo ,Company> companyMap = new HashMap<>();
		Map<UserInfo,CompanyDepartment> departmentMap = new HashMap<>() ;
		for (UserInfo userInfo : userInfoPage.getResult()) {
			Company company = companyService.get(userInfo.getCompanyId());
			CompanyDepartment companyDepartment= companyDepartmentService.select(" from CompanyDepartment where  id = ? ",userInfo.getDepartmentId());
			if(companyDepartment!=null){
				departmentMap.put(userInfo, companyDepartment);
			}
			companyMap.put(userInfo, company);
		}
		List<Company> listCompany = companyService.list();
		
		model.addAttribute("principal", principal);
		model.addAttribute("departmentMap", departmentMap);
		model.addAttribute("listCompany", listCompany);
		model.addAttribute("companyMap", companyMap);
		model.addAttribute("userInfoPage", userInfoPage);
		return "companyMember/companyMemberList";
	}
	
	@RequestMapping(value="add",method= RequestMethod.GET)
	public String add(@RequestParam(name="id",required=false)String id,Model model){
		
		if(!StringUtils.isEmpty(id)){
			Company company = companyService.select(" from Company where id = ? ", id);
			model.addAttribute("company", company);
		}else{
			List<Company> listCompany = companyService.list();
			model.addAttribute("listCompany", listCompany);
		}
		
		return "companyMember/companyMemberAdd";
	}
	
	@ResponseBody
	@RequestMapping(value="add",method= RequestMethod.POST)
	public String add(String companyId,String userId,Model model,HttpServletRequest request) throws Exception{
		UserInfo userInfo= userInfoService.select(" from UserInfo where id = ? ",userId);
		Company company = companyService.get(companyId);
		if(userInfo!=null){
			if(StringUtils.isEmpty(userInfo.getCompanyId())){
				
				CompanyMemberLog companyMemberLog = new CompanyMemberLog();
				companyMemberLog.setUserId(userInfo.getId());
				companyMemberLog.setCompanyId(companyId);
				Date date = new Date();
				companyMemberLog.setCreateDate(date);
				companyMemberLog.setNote("用户："+userInfo.getId()+"与公司:"+company.getName()+"绑定");
				companyMemberLog.setBinding(true);
				companyMemberLogService.save(companyMemberLog);
				
				
				List<UserInfo> source= userInfoService.list(" from UserInfo where  companyId =? order by rowNo desc ",companyId);
				if(!source.isEmpty()){
					UserInfo target = source.get(0);
					userInfo.setRowNo(target.getRowNo()+1);
				}
				
				userInfo.setBindingDate(date);
				userInfo.setCompanyId(companyId);
				userInfo.setCompanyState(true);
				userInfoService.save(userInfo);
				 //保存日志,HttpServletRequest request
       		    LoginUser userif= UserSession.getLoginUser(request);
				Operator operator = operatorService.findUserByUsername(userif.getUsername());
				operatorLogService.addUserLog(operator, request, "新增企业员工:电话"+userInfo.getPhone());
					
				return ResultUtil.getSuccessMsg();
			}
			return ResultUtil.getError("温馨提醒：该会员已经关联其他企业");
		}
		
		return ResultUtil.getError("温馨提醒：无该会员");
	}
	
	/**
	 * 解除员工
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/dalete", method = RequestMethod.GET)
	public String delete(String id,HttpServletRequest request) throws Exception {
		
		UserInfo userInfo = userInfoService.get(id);
		Company company = companyService.get(userInfo.getCompanyId());
		CompanyMemberLog companyMemberLog = new CompanyMemberLog();
		companyMemberLog.setUserId(userInfo.getId());
		companyMemberLog.setCompanyId(userInfo.getCompanyId());
		companyMemberLog.setCreateDate(new Date());
		companyMemberLog.setNote("用户：" + userInfo.getId() + "与公司:" + company.getName() + "解绑");
		companyMemberLog.setBinding(false);
		companyMemberLogService.save(companyMemberLog);
		
		userInfo.setCompanyState(false);
		userInfo.setCompanyId(null);
		userInfo.setBindingDate(null);
		userInfo.setDepartmentId(null);
		userInfo.setEntryTime(null);
		userInfo.setPersonInCharge(false);
		
		CompanyDepartment companyDepartment= companyDepartmentService.select(" from CompanyDepartment where responsibleId = ?",userInfo.getId());
		if(companyDepartment!=null){
			companyDepartment.setResponsibleId(null);
			companyDepartment.setResponsiblePerson(null);
			companyDepartmentService.save(companyDepartment);
		}
		
		userInfoService.save(userInfo);
		 //保存日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "解除企业员工:电话"+userInfo.getPhone());
		return ResultUtil.getSuccessMsg();
	}
	
	
	@ResponseBody
	@RequestMapping(value="typeahead" ,method = RequestMethod.POST)
	public String typeahead(Integer id  , String phone,String str){
		Page<UserInfo> page = new Page<>();
		String hql =" from UserInfo where 1 = 1";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(id)){
			hql +=" and  id = ? ";
			params.add(id);
		}if(!StringUtils.isEmpty(phone)){
			hql +=" or phone like ? ";
			params.add("%"+phone+"%");
		}
		if(!StringUtils.isEmpty(str)){
			hql +=" and realname like  ? ";
			params.add("%"+str+"%");
		}
		page = userInfoService.page(page, hql,params.toArray());
		List<String> names =new ArrayList<>();
		for (UserInfo userInfo : page.getResult()) {
			names.add(userInfo.getId()+","+userInfo.getRealname()+","+userInfo.getPhone());
		}
		return JsonUtil.toJson(names);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/getCompany",method = RequestMethod.GET)
	public String getUser( String str ,@RequestParam(name="pageNo",defaultValue="1")Integer pageNo){
		Page<Company> pageCompany = new Page<>();
		pageCompany.setPageNo(pageNo);
		String hql =" from Company where 1 = 1  and name like  ?";
		pageCompany = companyService.page(pageCompany, hql,"%"+str+"%");
		return JsonUtil.toJson(pageCompany);
	}
	
	/**
	 * 禁用公司会员
	 * @param arrayId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "editForbid", method = RequestMethod.POST)
	public @ResponseBody String editForbid(@RequestParam(name = "arrayId[]", required = true) String arrayId[],HttpServletRequest request) throws Exception {
		for (String userInfoId : arrayId) {
			if (!companyService.isResp(userInfoId)) {
				userInfoService.execute(" update  UserInfo  set companyState = 0  where id = ? ", userInfoId);
			}
		}
		//保存日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "禁用公司会员:id"+org.apache.commons.lang.StringUtils.join(arrayId,","));
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 与公司解除关系
	 * @param arrayId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="terminate",method=RequestMethod.POST)
	public @ResponseBody String editTerminate(@RequestParam(name = "arrayId[]", required = true) String arrayId[],HttpServletRequest request) throws Exception{
		for (String userId : arrayId) {
			if (!companyService.isResp(userId)) {
				Company company= companyService.getCompanyByUserId(userId);
				companyService.terminate(userId, company);
			}
		}
		//保存日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "与公司解除关系:id"+org.apache.commons.lang.StringUtils.join(arrayId,","));
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 公司启用该员工
	 * @param arrayId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="allowed",method=RequestMethod.POST)
	public @ResponseBody String startUsing(@RequestParam(name = "arrayId[]", required = true) String arrayId[],HttpServletRequest request) throws Exception{
		for (String userInfoId : arrayId) {
			userInfoService.execute(" update  UserInfo  set companyState = 1  where id = ? ", userInfoId);
		}
		//保存日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "公司启用该员工:id"+org.apache.commons.lang.StringUtils.join(arrayId,","));
		return ResultUtil.getSuccessMsg();
	}
	
}
