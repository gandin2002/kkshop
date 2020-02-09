package cn.bohoon.company.web;

import java.text.DecimalFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyDepartment;
import cn.bohoon.company.service.CompanyDepartmentService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.entity.UserLevel;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.userInfo.service.UserLevelService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "departmentsCompany")
public class CompanyDepartmentController {

	@Autowired
	CompanyDepartmentService companyDepartmentService;
	@Autowired
	CompanyService companyService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	UserLevelService userLevelService;

	/**
	 * 企业部门列表
	 * 
	 * @param request
	 * @param model
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, String companyId) {
		if (!StringUtils.isEmpty(companyId)) {
			Company company = companyService.select(" from Company where id = ? ", companyId);
			List<CompanyDepartment> list = companyDepartmentService.departmentSorting(company.getId());
			model.addAttribute("list", list);
		}
		model.addAttribute("companyId", companyId);
		return "companyDepartment/companyDepartmentList";
	}

	/**
	 * 去新增企业
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", 0) ;
		String companyId = ServletRequestUtils.getStringParameter(request, "companyId", "") ;
		
		CompanyDepartment dept = companyDepartmentService.get(id);
		String depName = "无";
		int level = 1 ;
		if (null != dept)  {
			if (null != dept.getPid()) {
				CompanyDepartment pDept = companyDepartmentService.getParent(dept.getPid());
				if (null != pDept) {
					depName = pDept.getTitle();
				}
				level = dept.getLevel() +1 ;
			}
			model.addAttribute("companyDept", dept);
		}
		model.addAttribute("pid", id);
		model.addAttribute("level", level);
		model.addAttribute("depName", depName);
		model.addAttribute("companyId",companyId);
		return "companyDepartment/companyDepartmentAdd";
	}

	/**
	 * 新增保存
	 * 
	 * @param request
	 * @param companyDepartment
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(HttpServletRequest request, CompanyDepartment companyDepartment) {
		DecimalFormat nf = new DecimalFormat("000");
		String head = "";
		if (!companyDepartment.getPid().equals(0)) {
			CompanyDepartment pDept = companyDepartmentService.get(companyDepartment.getPid());
			head = pDept.getDeptCode();
		}
		String max = companyDepartmentService.findMaxCode(companyDepartment.getPid(),companyDepartment.getCompanyId());
		String code = "";
		if (StringUtils.isEmpty(max)) {
			if (!StringUtils.isEmpty(head)) {
				code = head + "." + nf.format(1);
			} else {
				code =  nf.format(1);
			}
		} else {
			Integer j = Integer.valueOf(max.substring(max.length() - 3));
			if (!StringUtils.isEmpty(head)) {
				code = head + "." + nf.format(j + 1);
			} else {
				code = nf.format(j + 1);
			}
		}
		companyDepartment.setDeptCode(code);
		companyDepartmentService.save(companyDepartment);
		return ResultUtil.getSuccessMsg();
		 
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		CompanyDepartment department = companyDepartmentService.get(id);
		CompanyDepartment pDept = companyDepartmentService.getParent(department.getPid());
		model.addAttribute("pDept", pDept);
		model.addAttribute("item", department);
		return "companyDepartment/companyDepartmentEdit";
	}

	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(CompanyDepartment department) {
		CompanyDepartment target = companyDepartmentService.get(department.getId());
		target.setPid(department.getPid());
		target.setTitle(department.getTitle());
		target.setState(department.getState());
		companyDepartmentService.save(target);
		return ResultUtil.getSuccessMsg();
	}
	
	@ResponseBody
	@RequestMapping(value = "editDepartmentState", method = RequestMethod.GET)
	public String editDepartmentState(Integer id){
		CompanyDepartment companyDepartment = companyDepartmentService.get(id);
		List<CompanyDepartment> childDepartment = companyDepartmentService.recursionDepartment(companyDepartment);
		
		Boolean state = new Boolean(!companyDepartment.getState());
		for (CompanyDepartment companyDepartment2 : childDepartment) {
			
			List<UserInfo> listUserInfo= userInfoService.list(" from UserInfo where departmentId = ? ", companyDepartment2.getId());
			for (UserInfo userInfo : listUserInfo) {
				userInfo.setCompanyState(state);
			}
			userInfoService.saveBatch(listUserInfo, listUserInfo.size());
			companyDepartment2.setState(state);
		}
		companyDepartmentService.saveBatch(childDepartment,childDepartment.size());
		return ResultUtil.getSuccessMsg();
	}
	
	@ResponseBody
	@RequestMapping(value = "deleteDepartment", method = RequestMethod.GET)
	public String deleteDepartment(Integer id){
		CompanyDepartment companyDepartment = companyDepartmentService.get(id);
		List<CompanyDepartment> childDepartment = companyDepartmentService.recursionDepartment(companyDepartment);
		
		for (CompanyDepartment companyDepartment2 : childDepartment) {
			List<UserInfo> listUserInfo= userInfoService.list(" from UserInfo where departmentId = ? ", companyDepartment2.getId());
			for (UserInfo userInfo : listUserInfo) {
				userInfo.setDepartmentId(null);
				userInfo.setEntryTime(null);
			}
			userInfoService.saveBatch(listUserInfo, listUserInfo.size());
			companyDepartmentService.delete(companyDepartment2.getId());
		}
		return ResultUtil.getSuccessMsg();
		
	}
	
	@ResponseBody
	@RequestMapping(value = "typeahead", method = RequestMethod.POST)
	public String typeahead(String matchInfo, Integer matchCount) {
		Page<Company> companyPage = new Page<>();
		companyPage.setPageSize(matchCount);
		companyPage = companyService.page(companyPage, " from Company where name like ? ", "%" + matchInfo + "%");
		List<String> list = new ArrayList<>();
		for (Company company : companyPage.getResult()) {
			list.add(company.getId() + "," + company.getName());
		}
		return JsonUtil.toJson(list);
	}
	

	
	@RequestMapping(value = "staffList", method = RequestMethod.GET)
	public String staffList(HttpServletRequest request, Model model, String companyId, Integer departmentId, Integer username,String realname, String startTime, String endTime) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<UserInfo> pageUserInfo = new Page<>();
		pageUserInfo.setPageNo(pageNo);

		CompanyDepartment department = companyDepartmentService.get(departmentId);
		List<CompanyDepartment> childDepartment = companyDepartmentService.recursionDepartment(department);
		
		List<String> listStr = new ArrayList<>();
		for (CompanyDepartment department2 : childDepartment) {
			listStr.add(department2.getId().toString());
		}
		String gatherId = org.apache.commons.lang.StringUtils.join(listStr, ',');
		String hql = " from UserInfo where  departmentId in (" + gatherId + ") ";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(username)){
			hql +=" and id = ? ";
			params.add(username);
			model.addAttribute("username", username);
		}
		if(!StringUtils.isEmpty(realname)){
			hql +=" and realname like ?";
			params.add("%"+realname+"%");
			model.addAttribute("realname", realname);
		}
    	if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and entryTime >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and entryTime < ? ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		
		hql += " order by departmentId ASC ";
		
		pageUserInfo = userInfoService.page(pageUserInfo, hql,params.toArray());
		
		model.addAttribute("department", department);
		model.addAttribute("pageUserInfo", pageUserInfo);
		model.addAttribute("childDepartment", childDepartment);
		return "companyDepartment/staff/staffList";
	}

	@RequestMapping(value = "staffAdd", method = RequestMethod.GET)
	public String staffAdd(HttpServletRequest request, Model model, Integer departmentId,Integer userId,String nickname,String startTime,String endTime) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<UserInfo> pageUserInfo = new Page<>();
		pageUserInfo.setPageNo(pageNo);
		CompanyDepartment companyDepartment = companyDepartmentService.get(departmentId);
		
		String hql = " from  UserInfo where companyId =  ? ";
		List<Object> params = new ArrayList<>();
		params.add(companyDepartment.getCompanyId());
		if(!StringUtils.isEmpty(userId)){
			hql +=" and id = ? ";
			params.add(userId);
			model.addAttribute("userId", userId);
			
		}if(!StringUtils.isEmpty(nickname)){
			hql +=" and nickName like ?";
			params.add("%"+nickname+"%");
			model.addAttribute("nickname", nickname);
		}
		if (!StringUtils.isEmpty(startTime)){
    		hql = hql + " and createTime >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	hql = hql + " and createTime < ? ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		
		pageUserInfo = userInfoService.page(pageUserInfo,hql,params.toArray());

		Map<UserInfo, UserLevel> memberLevelMap = new HashMap<>();
		Map<UserInfo, CompanyDepartment> departemntMap = new HashMap<>();
		for (UserInfo userInfo : pageUserInfo.getResult()) {
			UserLevel memberLevel = userLevelService.getUserLevel(userInfo.getExp());
			if(userInfo.getDepartmentId()!=null){
				CompanyDepartment cd = companyDepartmentService.get(userInfo.getDepartmentId());
				departemntMap.put(userInfo, cd);
			}
			memberLevelMap.put(userInfo, memberLevel);
		}
		model.addAttribute("departemntMap", departemntMap);
		model.addAttribute("memberLevelMap", memberLevelMap);
		model.addAttribute("companyDepartment", companyDepartment);
		model.addAttribute("pageUserInfo", pageUserInfo);
		return "companyDepartment/staff/staffAdd";
	}

	@ResponseBody
	@RequestMapping(value = "employee", method = RequestMethod.GET)
	public String Employee(Integer departmentId, String id) {
		UserInfo userInfo = userInfoService.get(id);
		userInfo.setDepartmentId(departmentId);
		userInfo.setEntryTime(new Date());

		userInfoService.save(userInfo);
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 设置企业负责人
	 * 
	 * @param id
	 * @param departmentId
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editResponsible", method = RequestMethod.GET)
	public String editResponsible(Integer id, Integer departmentId, String userId) {
		
		CompanyDepartment companyDepartment = companyDepartmentService.get(departmentId);
		UserInfo userInfo = userInfoService.get(userId);
		
		companyDepartment.setResponsibleId(userInfo.getId());
		companyDepartment.setResponsiblePerson(userInfo.getNickname());
		userInfoService.execute(" update UserInfo set personInCharge=0 where departmentId=?", departmentId) ;
		companyDepartmentService.save(companyDepartment);
		userInfoService.execute(" update UserInfo set personInCharge=1 where id=?", userId) ;
		
		
		return ResultUtil.getSuccessMsg();
	}
	
	@ResponseBody
	@RequestMapping(value="editState",method=RequestMethod.GET)
	public String editState(String id){
		UserInfo userInfo = userInfoService.get(id);
		userInfo.setCompanyState(!userInfo.getCompanyState());
		userInfoService.save(userInfo);
		return ResultUtil.getSuccessMsg();
	}
}
