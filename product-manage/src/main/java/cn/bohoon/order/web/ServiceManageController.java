package cn.bohoon.order.web;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSONObject;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyCata;
import cn.bohoon.company.entity.CompanyIndustry;
import cn.bohoon.company.entity.CompanyLevel;
import cn.bohoon.company.service.CompanyCataService;
import cn.bohoon.company.service.CompanyIndustryService;
import cn.bohoon.company.service.CompanyLevelService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.order.entity.MymainExcelMode;
import cn.bohoon.order.entity.Mymaintenance;
import cn.bohoon.order.service.MymaintenanceService;
import cn.bohoon.serve.entity.Serve;
import cn.bohoon.serve.service.ServeService;
import cn.bohoon.userInfo.domain.LoginUser;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.util.UserSession;

@Controller
public class ServiceManageController {

	@Autowired
	MymaintenanceService mymaintenanceService;
	@Autowired
	CompanyCataService companyCataService; // 企业分类
	@Autowired
	CompanyLevelService companyLevelService; // 企业等级
	@Autowired
	CompanyIndustryService companyIndustryService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	CompanyService companyService;
	@Autowired
	ServeService serveService;
    @Autowired
    OperatorLogService operatorLogService;
   


	@RequestMapping(value = "/service/list")
	public String list(HttpServletRequest request, Model model, Mymaintenance mymaintenance) throws Exception {

		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		String state = ServletRequestUtils.getStringParameter(request, "currentstate", "");
		String company = ServletRequestUtils.getStringParameter(request, "company", ""); // 负责人

		Page<Mymaintenance> page = new Page<Mymaintenance>();
		page.setPageNo(pageNo);
		String hql = "from Mymaintenance t where 1 =1  ";
		//and t.quxiao=2 
		List<Object> params = new ArrayList<>();

		if (!StringUtils.isEmpty(state)) {

			hql += " and t.state=?";
			params.add(Integer.parseInt(state));
		}
		if (!StringUtils.isEmpty(mymaintenance.getId())) {
			hql += " and t.id like ?";
			params.add('%' + mymaintenance.getId() + '%');
		}
		if (!StringUtils.isEmpty(mymaintenance.getUserName())) {
			hql += " and t.userName like ?";
			params.add('%' + mymaintenance.getUserName() + '%');
		}

		if (!StringUtils.isEmpty(mymaintenance.getRealName())) {
			hql += " and t.realName like ?";
			params.add('%' + mymaintenance.getRealName() + '%');
		}

		if (!StringUtils.isEmpty(mymaintenance.getCompany())) {
			hql += " and t.company like ?";
			params.add('%' + mymaintenance.getCompany() + '%');
		}

		if (!StringUtils.isEmpty(startTime)) {
			
			hql = hql + " and t.submitTime >= ?";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and t.submitTime < ?";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}

		hql += " order by t.submitTime desc ";

		page = mymaintenanceService.page(page, hql, params.toArray());

		model.addAttribute("pageOrder", page);
		model.addAttribute("searchModel", mymaintenance);
		List<Mymaintenance> list = mymaintenanceService.list();
		model.addAttribute("list", list);
		return "aftermarketOrder/serviceManage2";
//		return  "aftermarketOrder/serviceManage";
	}
  
	@RequestMapping(value = "/addmyCompany/list")
	public String CompanyList(HttpServletRequest request, Model model, String idName, Company company)
			throws Exception {

		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		String companycataId = ServletRequestUtils.getStringParameter(request, "CompanycataId", "");
		String companyLevel = ServletRequestUtils.getStringParameter(request, "companyLevel", "");
		String IndustryId =  ServletRequestUtils.getStringParameter(request, "IndustryId", "");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String state = ServletRequestUtils.getStringParameter(request, "appliState", "");
		String companyName = ServletRequestUtils.getStringParameter(request, "companyName", "");
		Page<Company> pageCompany = new Page<Company>();
		
		pageCompany.setPageNo(pageNo);
		String hql = "from Company t where  t.state=1 ";
		List<Object> params = new ArrayList<>();
		//System.out.println("各个参数" + startTime + "endTiem参数" + startTime + "实体" + company.getId() + "状态" + state+"appliStated的状态"+company.getAppliState());

		if (!StringUtils.isEmpty(state)) {

			hql += " and t.appliState=?";
			params.add(Integer.parseInt(state));
			model.addAttribute("state",Integer.parseInt(state));
		}
		if (!StringUtils.isEmpty(idName)) {

			hql += " and (t.id like '%" + idName + "%' or t.name like '%" + idName + "%')";

			// params.add('%' +idName + '%');
		}
		/*
		 * if (!StringUtils.isEmpty(company.getId())) { hql +=
		 * " and t.id like ?"; params.add('%' +company.getId() + '%'); }
		 */
		/*
		 * if (!StringUtils.isEmpty(company.getUserName())) { hql +=
		 * " and t.userName like ?"; params.add('%' +
		 * mymaintenance.getUserName() + '%'); }
		 */

		if (!StringUtils.isEmpty(companycataId)) {
			hql += " and t.cataId=? ";
			params.add('%' + companycataId + '%');
		}

		if (!StringUtils.isEmpty(companyLevel)) {
			hql += " and t.level=? ";
			params.add(company.getLevel());
		}
		if (!StringUtils.isEmpty(IndustryId)) {
			hql += " and t.industryId=?";
			params.add(company.getIndustryId());
		}

		if (!StringUtils.isEmpty(startTime)) {
			hql = hql + " and t.serviceTiem >= ?";
			// 把字符串日期传化为日期
			// params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			params.add(sdf.parse(startTime));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and t.serviceTiem< ?";
			// params.add(DateUtil.getNDayAfter(endTime, 1));
			params.add(sdf.parse(endTime));
			model.addAttribute("endTime", endTime);
		}

		if (!StringUtils.isEmpty(companyName)) {
			String hql2 = " from  UserInfo where realname=? or phone like";
			UserInfo userInfo = userInfoService.select(hql2, companyName);

			hql = hql + " and t.userId in ( select id from UserInfo where realname like ? or phone like ?) ";

			params.add('%' + companyName + '%');
			model.addAttribute("companyName", companyName);
		}

		hql += " order by t.serviceTiem desc ";

		List<CompanyLevel> levelList = companyLevelService.list();
		model.addAttribute("levelList", levelList); // 企业等级
		List<CompanyCata> cateList = companyCataService.list();
		model.addAttribute("cateList", cateList); // 企业分类
		List<CompanyIndustry> industryList = companyIndustryService.list();
		model.addAttribute("industryList", industryList);
	

		pageCompany = companyService.page(pageCompany, hql, params.toArray());

		Map<Company, CompanyCata> cataMap = new HashMap<Company, CompanyCata>();
		Map<Company, CompanyLevel> levelMap = new HashMap<Company, CompanyLevel>();
		Map<Company, CompanyIndustry> industryMap = new HashMap<Company, CompanyIndustry>();

		Map<Company, UserInfo> userMap = new HashMap<Company, UserInfo>();

 		for (Company company2 : pageCompany.getResult()) {
				CompanyCata cata = company2.getCompanyCata();
				cataMap.put(company2, cata);
				
				CompanyIndustry industry =company2.getCompanyIndustry();
				industryMap.put(company2, industry);
				
				CompanyLevel level = company2.getCompanyLevel();
				levelMap.put(company2, level);
				
				if (null != company2.getUserId()) {
					UserInfo user = userInfoService.get(company2.getUserId());
					userMap.put(company2, user);
				}
			
		}
		List<Serve> servelist = serveService.list();
		model.addAttribute("servelist", servelist);
		model.addAttribute("userMap", userMap);
		model.addAttribute("cataMap", cataMap);
		model.addAttribute("levelMap", levelMap);
		model.addAttribute("industryMap", industryMap);
		model.addAttribute("pageCompany", pageCompany);
        
		model.addAttribute("company", company);
		return "aftermarketOrder/CompanyService";
	}

	// 编辑
	@RequestMapping("/tosee")
	@ResponseBody
	public String toMydelitail(Model model, HttpRequest request, String id,HttpServletRequest request1) {

		LoginUser loginUser = UserSession.getLoginUser();
		if (null == loginUser) {
			return ResultUtil.getError("登录信息已失效！");
		}
		Mymaintenance mymaintenance = mymaintenanceService.get(id);

		return JSONObject.toJSONString(mymaintenance);
	}

	// 处理完成
	@RequestMapping(value = "/handleafter", method = RequestMethod.POST)
	@ResponseBody
	public String handleafter(Model model, HttpRequest request, HttpServletResponse response, String id, String desc,
			String itemName,HttpServletRequest request1) throws Exception {
		LoginUser loginUser = UserSession.getLoginUser();
		UserInfo userInfo = userInfoService.get(loginUser.getUserId());
		
	/*	if (null == loginUser) {
			return ResultUtil.getError("登录信息已失效！");
		}*/
		Mymaintenance entity = mymaintenanceService.get(id);
		
		if (!StringUtils.isEmpty(desc)) {
			entity.setMaintennanceDesc(desc);
		}
		if (!StringUtils.isEmpty(itemName)) {
			entity.setMaintennanceItem(itemName);
		}
		if (!StringUtils.isEmpty(userInfo.getRealname())) {
			entity.setHandlePeople(userInfo.getRealname());
		}
       entity.setState(1);
      Integer    finichCount=entity.getFinishCount()+1;
       entity.setFinishCount(finichCount);
     Date  finishTime=new Date();
       entity.setFinishTime(finishTime.toString());
		mymaintenanceService.update(entity);
		//保存日志,HttpServletRequest request
		cn.bohoon.main.domain.LoginUser userif= cn.bohoon.main.domain.UserSession.getLoginUser(request1);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request1, "维保处理完成");
		return ResultUtil.getSuccessMsg();
	}

	// 处理详情通过
	@RequestMapping(value = "/handlerCompany", method = RequestMethod.POST)
	@ResponseBody
	public String handleCompany(Model model, HttpRequest request, HttpServletResponse response, String id,
			String afterTime, String realName,HttpServletRequest request1) throws Exception {
		LoginUser loginUser = UserSession.getLoginUser();
		if (null == loginUser) {
			return ResultUtil.getError("登录信息已失效！");
		}

		Company entity = companyService.get(id);
		entity.setValidityTime(afterTime);
		entity.setTechnicaladvisers(realName);

		companyService.update(entity);
		//保存日志,HttpServletRequest request
		cn.bohoon.main.domain.LoginUser userif= cn.bohoon.main.domain.UserSession.getLoginUser(request1);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request1, " 处理企业详情通过");
		return ResultUtil.getSuccessMsg();
	}
	
	@Autowired
	OperatorService operatorService;
	
	@RequestMapping(value="service/serviceManageInfo",method=RequestMethod.GET)
	public String serviceManageInfo(@RequestParam(name="id",required=true)String id,Model model){
		Mymaintenance mymaintenance = mymaintenanceService.get(id);
		model.addAttribute("mymaintenance", mymaintenance);
		return "aftermarketOrder/serviceManageInfo";
	}
	
	@RequestMapping(value="service/serviceManageInfo",method=RequestMethod.POST)
	public @ResponseBody String serviceManageInfo(@RequestParam(name="id",required=true)String id,String handlerResult,String maintennanceItem) throws Exception{
		Mymaintenance mymaintenance = mymaintenanceService.get(id);
		/*if(mymaintenance.getQuxiao()==1){
			mymaintenanceService.delete(mymaintenance);
		}else{*/
		if(mymaintenance.getQuxiao()==1){
			mymaintenance.setQuxiao(3);
		}else{
			mymaintenance.setQuxiao(2);  //上加
		}
		mymaintenance.setHandlerResult(handlerResult);
		mymaintenance.setMaintennanceItem(maintennanceItem);
		mymaintenance.setState(1);
	   Company company= companyService.getCompanyByUserId(mymaintenance.getUserId());
	   company.setFinishCount(company.getFinishCount()+1);
	   companyService.save(company);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		cn.bohoon.main.domain.LoginUser loginUser = cn.bohoon.main.domain.UserSession.getLoginUser(request);
		Operator user = operatorService.findUserByUsername(loginUser.getUsername());
		mymaintenance.setHandlePeople(user.getRealname());
		
		mymaintenance.setFinishTime(DateUtil.formatDate(new Date()));
		
		mymaintenance.setFinishCount(mymaintenance.getFinishCount()+1);
		
		mymaintenanceService.save(mymaintenance);
		
		return ResultUtil.getSuccessMsg();
	}
	
	@RequestMapping(value="addmyCompany/addmyCompanyInfo",method=RequestMethod.GET)
	public  String addmyCompanyInfo(@RequestParam(name="id",required=true)String id,Model model){
		 Company company  = companyService.get(id);
		 List<Operator> operatorList = operatorService.list();
		 model.addAttribute("id",id);
		 model.addAttribute("operatorList", operatorList);
		 model.addAttribute("company", company);
		 return "aftermarketOrder/addmyCompanyInfo";
	}
 
	@RequestMapping(value="addmyCompany/addmyCompanyInfo",method=RequestMethod.POST)
	public @ResponseBody  String addmyCompanyInfo(String id,String technicaladvisers,String startTime){
		
		 Company company  = companyService.get(id);
		 company.setValidityTime(startTime);
		 company.setTechnicaladvisers(technicaladvisers);
	     Operator operator = operatorService.select(" from Operator where realname=?" , technicaladvisers);
	     
		 company.setPassShenHeTime(DateUtil.formatDate(new Date()));
		 company.setAppliState(1);
		 if(!StringUtils.isEmpty(operator.getPhone())){
			 company.setProtectPhone(operator.getPhone());
			 
		 }
			 company.setFinishCount(company.getFinishCount()+1);
		
		
		 companyService.save(company);
		 return  ResultUtil.getSuccessMsg();
	}
	

    @RequestMapping(value = "/addmyCompany/delete", method = RequestMethod.POST)
	public @ResponseBody String delete(@RequestParam(name="cIArray[]",required=true)String cIArray[]){
    	for (String companyId : cIArray) {
    		Company company = companyService.get(companyId); 
    		company.setState(0);
    		company.setAppliState(null);
			companyService.update(company);
		}
    	
    	
		return ResultUtil.getSuccessMsg();
	}
    
    
    
	/**
	 * 新增
	 * 
	 * @param request
	 * @param model
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "addServiceCompany/helpOrder", method = RequestMethod.GET)
    public String helpOrder(HttpServletRequest request,Model model,Company search,String name) throws Exception  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        Page<Company> pageCompany=new Page<Company>(5);
        pageCompany.setPageNo(pageNo);
        String hql = "from Company c where 1 =1 " ;
        List<Object> params = new ArrayList<>();
        if(!StringUtils.isEmpty(name)) {
        	model.addAttribute("name",name) ;
        	
        }
        if(!StringUtils.isEmpty(search.getName())) {
        	hql += " and c.name like ?" ;
        	params.add('%'+search.getName()+'%') ;
        }
        hql += " and c.cataId is not NULL and c.industryId is not NULL and c.level is not NULL and c.userId is not NULL " ;
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
        return "aftermarketOrder/addServiceCompany2";
    }
	
    
    //后台加入维保
	 
	@RequestMapping(value = "addServiceCompany/project", method = RequestMethod.POST)
	@ResponseBody
    public String  confirmadd(HttpServletRequest request,Model model,Company search,@PathParam(value="id") String id) throws Exception  {
		try {
		//	LoginUser loginUser = UserSession.getLoginUser();
			cn.bohoon.main.domain.LoginUser loginUser = cn.bohoon.main.domain.UserSession.getLoginUser(request);
			Operator user = operatorService.findUserByUsername(loginUser.getUsername());
			
		/*	if(null == loginUser) {
				return ResultUtil.getError("登录信息已失效") ;
			}*/
			
			//String userId = loginUser.getUserId();
			Company company= companyService.get(id);
			company.setState(1);
			company.setAppliState(1);
			companyService.update(company);
		} catch (Exception e) {
			e.printStackTrace();
			return   ResultUtil.getError("操作失败");
		
		}
		//保存日志,HttpServletRequest request
		cn.bohoon.main.domain.LoginUser userif= cn.bohoon.main.domain.UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, " 后台加入维保");
		return ResultUtil.getSuccessMsg();
		
    }
	
    
	
	
	/**
	 * 导出货品excel
	 * @param response
	 * @param pid
	 * @throws Exception
	 */
	@RequestMapping(value="downloadExcel",method=RequestMethod.POST)
	public @ResponseBody void downloadExcel(HttpServletResponse response,String[] pid,Integer flag) throws Exception{
		String filename = URLEncoder.encode("博宏维保单", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename=" +filename +System.currentTimeMillis()+ ".xlsx"); // 名字加时间戳
		//ServletOutputStream sos = response.getOutputStream();
		List<MymainExcelMode> pemlist = new ArrayList<>();
		if(ArrayUtils.isEmpty(pid)){ // 写入全部
			List<Mymaintenance> listProduct = mymaintenanceService.list(" from Mymaintenance t where 1=1");
			for (Mymaintenance product : listProduct) {
				MymainExcelMode prodExcelModel =new  MymainExcelMode(); 
				prodExcelModel.setParams(product);
				pemlist.add(prodExcelModel);
			}
		} else {
			for (String string : pid) {
			 // 写入指定
					Mymaintenance product = mymaintenanceService.get(string);
					MymainExcelMode  myM=new MymainExcelMode();
					myM.setParams(product);
					pemlist.add(myM);
				
			}
		}
		ExcelWrite.writeExcel(response.getOutputStream(), pemlist);
		response.getOutputStream().close();
	}
    
	
    @RequestMapping(value = "/Mymaintenan/delete")
	public @ResponseBody String deleteMy(@RequestParam(name="pIdArray[]",required=true) String pIdArray[],HttpServletRequest request) throws Exception{
    	for (String myMainId :  pIdArray) {
    		mymaintenanceService.delete(myMainId);
    	
		}
    	//保存日志,HttpServletRequest request
		cn.bohoon.main.domain.LoginUser userif= cn.bohoon.main.domain.UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, " 导出维保订单");
		return ResultUtil.getSuccessMsg();
	}
    
}
