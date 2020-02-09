package cn.bohoon.company.web;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.bohoon.company.domain.BillTime;
import cn.bohoon.company.domain.CompanyModel;
import cn.bohoon.company.domain.CompanySateEnum;
import cn.bohoon.company.domain.CreditType;
import cn.bohoon.company.entity.City;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyCata;
import cn.bohoon.company.entity.CompanyIndustry;
import cn.bohoon.company.entity.CompanyLevel;
import cn.bohoon.company.entity.License;
import cn.bohoon.company.helper.CompanyExcelModel;
import cn.bohoon.company.service.CityService;
import cn.bohoon.company.service.CompanyCataService;
import cn.bohoon.company.service.CompanyIndustryService;
import cn.bohoon.company.service.CompanyLevelService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.company.service.LicenseService;
import cn.bohoon.excel.util.ExcelCompany;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.order.entity.GatheringCondition;
import cn.bohoon.order.service.GatheringConditionService;
import cn.bohoon.userInfo.domain.AuthState;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.userInfo.service.UserService;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "company")
public class CompanyController {
	
	@Autowired
	OperatorService operatorService;
	
    @Autowired
    CompanyService companyService;
    
    @Autowired
    CompanyCataService companyCataService;
    
    @Autowired
    CompanyLevelService companyLevelService;
    
    @Autowired
    CompanyIndustryService companyIndustryService;
    
    @Autowired
    UserInfoService userInfoService;
    
    @Autowired
    LicenseService licenseService;
    @Autowired
    OperatorLogService operatorLogService;
    

    
	@Autowired
	CityService cityService;
	@Autowired
	ThreadPoolTaskExecutor simpleExecutor ;
	@Autowired
	UserService	userService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, Company search,
		String coreditSort, String creditOverSort, String registerDateSort, String periodSort) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		Integer cataId = ServletRequestUtils.getIntParameter(request, "cataId", 0); // 企业分类
		Integer levels = ServletRequestUtils.getIntParameter(request, "levels", 0); // 企业等级
		String userId = ServletRequestUtils.getStringParameter(request, "userId", "");// 负责人id
		Page<Company> pageCompany = new Page<Company>();
		pageCompany.setPageNo(pageNo);
	
		String hql = "from Company c where 1 =1 ";
		List<Object> params = new ArrayList<>();
		List<CompanyLevel> levelList = companyLevelService.list();
		model.addAttribute("levelList", levelList);

		List<CompanyCata> cataList = companyCataService.list();
		model.addAttribute("cataList", cataList);

		if (levels != 0) {
			hql += " and c.level = ?";
			params.add(levels);
			model.addAttribute("levels", levels);
		}
		if (cataId != 0) {
			hql += " and c.cataId =?";
			params.add(cataId);
		}
		if (!StringUtils.isEmpty(search.getName())) {
			hql += " and( c.name like ? or c.id like ? )";
			params.add('%' + search.getName() + '%');
			params.add('%' + search.getName() + '%');
		}
		if (!StringUtils.isEmpty(userId)) {
			hql = hql + " and c.userId = ?";
			params.add(userId);
			model.addAttribute("userId", userId);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql = hql + " and c.regtime >= ?";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and c.regtime < ?";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		
		if(!StringUtils.isEmpty(registerDateSort)){
			hql += " order by c.regtime  " +registerDateSort;
			model.addAttribute("registerDateSort", registerDateSort);
		}else if(!StringUtils.isEmpty(coreditSort)){
			hql += " order by  c.credits " +coreditSort;
			model.addAttribute("coreditSort", coreditSort);
			
		}else if(!StringUtils.isEmpty(creditOverSort)){
			hql += " order by c.creditOver "+creditOverSort;
			model.addAttribute("creditOverSort", creditOverSort);
		}else if(!StringUtils.isEmpty(periodSort)){
			hql += " order by c.period " + periodSort;
			model.addAttribute("periodSort", periodSort);
		}else{
			
			hql = hql.concat("order by c.regtime DESC");
		}
		
		
 
		pageCompany = companyService.page(pageCompany, hql, params.toArray());
		Map<Company, CompanyCata> cataMap = new HashMap<Company, CompanyCata>();
		Map<Company, CompanyLevel> levelMap = new HashMap<Company, CompanyLevel>();
		Map<Company, CompanyIndustry> industryMap = new HashMap<Company, CompanyIndustry>();
		Map<Company, UserInfo> userMap = new HashMap<Company, UserInfo>();

		for (Company company : pageCompany.getResult()) {
			if(company.getCataId() != null){
				CompanyCata cata = companyCataService.get(company.getCataId());
				cataMap.put(company, cata);
			}
			if(company.getIndustryId() != null){
				CompanyIndustry industry = companyIndustryService.get(company.getIndustryId());
				industryMap.put(company, industry);
			}
			if(company.getLevel() != null){
				CompanyLevel level = companyLevelService.get(company.getLevel());
				levelMap.put(company, level);
			}
			if (null != company.getUserId()) {
				UserInfo user = userInfoService.get(company.getUserId());
				userMap.put(company, user);
			}
		}
		model.addAttribute("search", search);
		model.addAttribute("userMap", userMap);
		model.addAttribute("cataMap", cataMap);
		model.addAttribute("levelMap", levelMap);
		model.addAttribute("industryMap", industryMap);
		model.addAttribute("pageCompany", pageCompany);
		return "company/companyList";
	}

    @ResponseBody
    @RequestMapping(value = "sync")
    public String sync() {
    	simpleExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				companyService.getPartnerFromChi();
			}
		});
        
        return ResultUtil.getSuccessMsg();
    }

  
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
    	List<CompanyCata> catalist= companyCataService.list();
    	model.addAttribute("catalist",catalist);
    	
    	List<CompanyLevel> levelList = companyLevelService.list();
    	model.addAttribute("levelList",levelList);
    	
    	List<CompanyIndustry> industryList = companyIndustryService.list();
    	model.addAttribute("industryList",industryList);
    	
    	List<Operator> operatorList = operatorService.list();
    	model.addAttribute("operatorList",operatorList);
    	
    	List<UserInfo> userlist = userInfoService.list();
    	model.addAttribute("userlist",userlist);
    	List<City> listCity = cityService.list(" from City where type = ?  ", 2);
		// 申请到账时间
		BillTime[] billTimes = BillTime.values();
		model.addAttribute("billTimes", billTimes);
    	model.addAttribute("listCity", listCity);
    	model.addAttribute("code",IDUtil.getMemberId());
    	return "company/companyAdd";
    }
    
  //当输入会员号时   出现下拉列表显示信息
    @ResponseBody
	@RequestMapping(value="typeahead" ,method = RequestMethod.POST)
	public String typeahead(String id  , String phone,String str){
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
    
    
 // 添加企业
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, CompanyModel companyModel, Model model) throws Exception {

		UserInfo userInfo = userInfoService.get(companyModel.getUserinfo().getId());

		Company company = companyModel.getCompany();
		City province = cityService.findCictBycode(company.getProvinceCode());// 省
		if (province != null){
		company.setProvince(province.getName());
		City city = cityService.findCictBycode(company.getCityCode());// 市
		if (city != null) {
			company.setCity(city.getName());

			City county = cityService.findCictBycode(company.getCountyCode());// 区
			if (county != null) {
				company.setCounty(county.getName());
			}

		}
		}

		if (userInfo == null) {
			return ResultUtil.getError("没有该用户请重新选择！");
		}

		List<Company> us = companyService.list("from Company where userId= ?", userInfo.getId());
		if (!us.isEmpty()) {
			return ResultUtil.getError("会员号已与其他企业关联！");
		}

		License license = licenseService.get(companyModel.getLicense().getRegistration());
		if (license != null) {
			return ResultUtil.getError("营业执照 已被注册！");
		}

		companyModel.setUserinfo(userInfo);
		companyService.saveCompany(companyModel);
		
		
		//新增日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增企业："+companyModel.getCompany().getName());

		return ResultUtil.getSuccessMsg();
	}
	
	@Autowired
	GatheringConditionService gatheringConditionService;
    
	// 企业信息编辑
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		Company company = companyService.get(id);

		List<CompanyCata> catalist = companyCataService.list();
		model.addAttribute("catalist", catalist);

		List<CompanyLevel> levelList = companyLevelService.list();
		model.addAttribute("levelList", levelList);

		List<CompanyIndustry> industryList = companyIndustryService.list();
		model.addAttribute("industryList", industryList);

		List<UserInfo> userlist = userInfoService.list();
		model.addAttribute("userlist", userlist);

		List<City> listProvince = cityService.list(" from City where type = ?  ", 2);
		model.addAttribute("listProvince", listProvince);

		List<Operator> operatorList = operatorService.list();

		if (company.getCityCode() != null) {
			List<City> listCity = cityService.list(" from City where parentId = ?  and type = ?  ",
					company.getProvinceCode(), 3);
			model.addAttribute("listCity", listCity);

			if (company.getCountyCode() != null) {
				List<City> listArea = cityService.list(" from City where parentId = ?  and  type = ?  ",
						company.getCityCode(), 4);
				model.addAttribute("listArea", listArea);
			}
		}
		//--------------- 付款条款 ------------------
		List<GatheringCondition> acLit = gatheringConditionService.list();
		model.addAttribute("billTimes", acLit);
		//---------------end ---------------------
		model.addAttribute("operatorList", operatorList);
		model.addAttribute("item", company);
		return "company/companyEdit";
	}

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(Company company,HttpServletRequest request) throws Exception {
		
		City province = cityService.findCictBycode(company.getProvinceCode());// 省
		company.setProvince(province.getName());
		City city = cityService.findCictBycode(company.getCityCode());// 市
		if (city != null) {
			company.setCity(city.getName());

			City county = cityService.findCictBycode(company.getCountyCode());// 区
			if (county != null) {
				company.setCounty(county.getName());
			}

		}
        companyService.update(company);
    	//新增日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改企业："+company.getName());
        return ResultUtil.getSuccessMsg();
    }
	
	//负责人信息编辑
	@RequestMapping(value = "userinfoEdit", method = RequestMethod.GET)
    public String editUserinfoGet(HttpServletRequest request,Model model) {
		String companyId=ServletRequestUtils.getStringParameter(request, "companyId","");
		Company company = companyService.get(companyId) ;
		UserInfo userinfo=userInfoService.select(" from UserInfo where id = ? ", company.getUserId());
        model.addAttribute("item",userinfo);
        model.addAttribute("companyId",companyId);
        return "company/companyUserInfoEdit";

    }

	@ResponseBody
    @RequestMapping(value = "userinfoEdit", method = RequestMethod.POST)
    public String editUserinfoPost(UserInfo userinfo,HttpServletRequest request) throws Exception{
		UserInfo userEntity = userInfoService.get(userinfo.getId()) ;
		userEntity.setCompanyId(userinfo.getCompanyId());
		userEntity.setRealname(userinfo.getRealname());
		userEntity.setNickname(userinfo.getNickname());
		Company company = companyService.get(userEntity.getCompanyId());
		//找到对应公司
		UserInfo oldUser = userInfoService.get(company.getUserId());
		oldUser.setCompanyId("");
		company.setUserId(userEntity.getId());

		String Hql ="from User where userInfoId =? ";
		User olduser=userService.select(Hql, oldUser.getId());
		olduser.setCompanyId("");
		userService.update(olduser);//添加登录信息
		
		User newuser=userService.select(Hql, userinfo.getId());
		newuser.setCompanyId(company.getId());
		UserInfo userinfo1=userInfoService.get(newuser.getUserInfoId());
		userinfo1.setCompanyId(company.getId());
		userInfoService.update(userEntity);
		userInfoService.update(oldUser);
		userInfoService.update(userinfo1);
		userService.update(newuser);
		companyService.update(company);
		
		//新增日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改企业负责人："+company.getName());
		//找到之前的企业负责人info
		//置
        return ResultUtil.getSuccessMsg();
    }
	
	/**
	 * 待下单
	 * 
	 * @param request
	 * @param model
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "helpOrder", method = RequestMethod.GET)
    public String helpOrder(HttpServletRequest request,Model model,Company search) throws Exception  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        Page<Company> pageCompany=new Page<Company>(5);
        pageCompany.setPageNo(pageNo);
        String hql = "from Company c where 1 =1 " ;
        List<Object> params = new ArrayList<>();
       
        if(!StringUtils.isEmpty(search.getName())) {
        	hql += " and c.name like ?" ;
        	params.add('%'+search.getName()+'%') ;
        }
//        hql += " and c.cataId is not NULL and c.industryId is not NULL and c.level is not NULL and c.userId is not NULL " ;
        hql += " order by c.regtime desc " ;
        pageCompany=companyService.page(pageCompany, hql, params.toArray());
        Map<Company,CompanyCata> cataMap = new HashMap<Company,CompanyCata>() ;
        Map<Company,CompanyLevel> levelMap = new HashMap<Company,CompanyLevel>() ;
        Map<Company,CompanyIndustry> industryMap = new HashMap<Company,CompanyIndustry>() ;
        Map<Company,UserInfo> userMap = new HashMap<Company,UserInfo>();
        
        for(Company company:pageCompany.getResult() ) {

        	CompanyCata cata = companyCataService.get(company.getCataId()) ;
        	cataMap.put(company, cata) ;
        	CompanyIndustry industry = companyIndustryService.get(company.getIndustryId()) ;
        	industryMap.put(company, industry) ;
        	CompanyLevel level = companyLevelService.get(company.getLevel()) ;
        	levelMap.put(company, level) ;
        	UserInfo user = userInfoService.get(company.getUserId());
        	userMap.put(company, user);
        }
        model.addAttribute("search",search);
        model.addAttribute("userMap",userMap);
        model.addAttribute("cataMap", cataMap);
        model.addAttribute("levelMap", levelMap);
        model.addAttribute("industryMap", industryMap);
        model.addAttribute("pageCompany", pageCompany);
        return "company/helpOrderCompany";
    }
	
	
	/**
	 * 去编辑营业执照信息
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "licenseEdit", method = RequestMethod.GET)
    public String editLicenseGet(HttpServletRequest request,Model model) {
		String companyid=ServletRequestUtils.getStringParameter(request, "companyid","");
		Company company = companyService.get(companyid);
		String licenseId= company.getLicenseId();
		if(!StringUtils.isEmpty(licenseId)){
			License license=licenseService.get(licenseId);
			model.addAttribute("item",license);
		}
        model.addAttribute("company",company);
        return "company/companyLicenseEdit";
    }

	/**
	 * 保存营业执照信息
	 * 
	 * @param license
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "licenseEdit", method = RequestMethod.POST)
    public String editLicensePost(License license,HttpServletRequest request) throws Exception {
		License Source = licenseService.select(" from License where id = ? ",license.getRegistration());
		if(Source != null && !Source.getCompanyid().equals(license.getCompanyid())){
			return ResultUtil.getError("营业执照已存在!");
		}
        licenseService.saveLiceseUpdateCompany(license);
      //新增日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改企业id："+license.getCompanyid()+"营业执照信息");
        
        return ResultUtil.getSuccessMsg();
    }
	
	/**
	 * 删除公司
	 * @param cIArray 企业ID[]
	 * @return
	 * @throws Exception 
	 */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody String delete(@RequestParam(name="cIArray[]",required=true)String cIArray[],HttpServletRequest request) throws Exception{
    	for (String companyId : cIArray) {
			companyService.deleteAll(companyId);
			 
		}
    	//新增日志,HttpServletRequest request
		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除企业ID："+org.apache.commons.lang.StringUtils.join(cIArray,","));
		return ResultUtil.getSuccessMsg();
	}
    

    @RequestMapping(value="editCredit",method=RequestMethod.GET)
    public String editCredit(@RequestParam(name="idArry[]",required=true)String idArry[],Model model){
    	model.addAttribute("idArry",org.apache.commons.lang.StringUtils.join(idArry,','));
    	return "company/companyEditCredit";
    }
	
    /**
     * 修改信用额度
     * @param idArry 企业[]
     * @return
     * @throws Exception 
     */
    @RequestMapping(value="editCredit",method=RequestMethod.POST)
    public @ResponseBody String editCredit(String[] idArry,BigDecimal credit,CreditType type,HttpServletRequest request) throws Exception{
    	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
    	for (String companyId : idArry) {
		String mString  = (String) companyService.updateCredit(companyId,credit,type,operator.getRealname());
		if (!StringUtils.isEmpty(mString)) {
			return ResultUtil.getError(mString);
		}
    	}
    	//新增日志,HttpServletRequest request    	
    			
    			operatorLogService.addUserLog(operator, request, "修改信用额度企业id："+org.apache.commons.lang.StringUtils.join(idArry,","));
		       return  ResultUtil.getSuccessMsg();
    }
   
    @RequestMapping(value="updateCompanyId",method=RequestMethod.GET)
    public void addQYusr(){
    List<Company> companyList=	companyService.list();
    	for (Company company : companyList) {
    		if(!StringUtils.isEmpty(company.getUserId())){
    			User user=	userService.select("from User where userInfoId=?",company.getUserId());
    				user.setCompanyId(company.getId());
    				userService.update(user);
    		}
		}
    	
    }
    
    
	/**
	 * 企业导入
	 */
	@RequestMapping(value="excelImport",method=RequestMethod.POST)
	public @ResponseBody String importExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		List<CompanyExcelModel> list = ExcelCompany.getExcel(file.getInputStream());
		
		for (CompanyExcelModel companyExcelModel : list){
			
			String name = companyExcelModel.getName();
			String phone = companyExcelModel.getPhone();
			
			Company company = companyService.getIsFlag("name", name);
			
			// 则说明企业名称已存在
			if (null != company){
				
				continue;
			}
			// 手机号码的重复性判断
			UserInfo userInfo = userInfoService.select("from UserInfo where phone=?", phone);
			if (null != userInfo){
				continue;
			}
			userInfo = new UserInfo();
			userInfo.setRealname(companyExcelModel.getRealName());
			userInfo.setNickname(companyExcelModel.getRealName());
			userInfo.setCompanyAuthState(AuthState.NO_AUTHED);
			userInfo.setIdCardAuthState(AuthState.AUTH_PASS);
			userInfo.setIdCardAuthDesc("认证通过");
			userInfo.setApplicationTime(new Date());
			userInfo.setPhone(phone);
			userInfoService.save(userInfo);
			
			company = new Company();
			company.setName(name);
			
			company.setRegtime(new Date());
			company.setCompanySate(CompanySateEnum.PASS);
			company.setPassTime(new Date());
			company.setCheckWarn("审核通过");
			company.setUserId(userInfo.getId());
			company.setPhone(userInfo.getPhone());
			String maxId = companyService.select("select max(code) from Company ", String.class) ;
			String code = "";
			if(!StringUtils.isEmpty(maxId)) {
				if(NumberUtils.isNumber(maxId)){
					code = Integer.parseInt(maxId)+1+"";
				}
			}else{
				code = "100001";
			}
			company.setCode(code);
	    	company.setId(IDUtil.getMemberId());
	    	company.setCataId(14);  // 企业认证，默认为 '普通客户'
			companyService.save(company);
			
			
			
			// 在保存用户表
			User user = new User();
			user.setMobile(userInfo.getPhone());
			user.setPassword(userInfoService.encodePwd());
			user.setUserInfoId(userInfo.getId());
			user.setCompanyId(company.getId());
			userService.save(user);
			userInfo.setCompanyId(company.getId());
			userInfoService.update(userInfo);
			
		}
		return ResultUtil.getSuccessMsg();
	}
    
    
    
}


