package cn.bohoon.company.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.company.domain.CompanyConcatState;
import cn.bohoon.company.domain.CompanySateEnum;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyCata;
import cn.bohoon.company.entity.CompanyIndustry;
import cn.bohoon.company.entity.License;
import cn.bohoon.company.helper.ContactHelper;
import cn.bohoon.company.service.CompanyCataService;
import cn.bohoon.company.service.CompanyIndustryService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.company.service.LicenseService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.message.aop.MessageNodeNotified;
import cn.bohoon.message.domain.MessageNodeType;
import cn.bohoon.message.domain.SendType;
import cn.bohoon.order.entity.GatheringCondition;
import cn.bohoon.order.service.GatheringConditionService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.util.SyncDataUtils;
import cn.bohoon.wx.mp.entity.WXUserInfo;
import cn.bohoon.wx.mp.service.WXUserInfoService;

/**
 * 企业审核
 * @author HJ
 * 2017年11月9日,下午1:55:41
 */

@Controller
@RequestMapping(value="companyCheck")
public class CompanyCheckController {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	CompanyIndustryService companyIndustryService;
	
	@Autowired
	private CompanyCataService companyCataService;
	@Autowired
    OperatorLogService operatorLogService;

	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public String list(CompanySateEnum  type,Model model,@RequestParam(name="pageNo",defaultValue="1")Integer pageNo,String name,String userId,String startTime ,String endTime,
			@RequestParam(name="cataId",defaultValue="0")Integer cataId) throws ParseException{
		
		Page<Company> pageCompany = new Page<>();
		pageCompany.setPageNo(pageNo);
		
		String sql = " from Company where 1 = 1  ";
		List<Object> params = new ArrayList<>();
		
		if(!StringUtils.isEmpty(name)){
			sql += " and name like ? ";
			params.add('%'+name+'%');
		}
		if (!StringUtils.isEmpty(userId)) {
			sql += " and (userId like '%"+userId+"%' or  phone like '%"+userId+"%'  ) ";
		}
    	if (!StringUtils.isEmpty(startTime)){
    		sql = sql + " and regtime >= ? ";
    		params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
    		model.addAttribute("startTime", startTime);
        }
        if (!StringUtils.isEmpty(endTime)){
        	sql = sql + " and regtime < ? ";
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
		
		if(type!=null){
			sql += " and companySate = ?";
			params.add(type);
			model.addAttribute("type",type);
		}
		
		if (cataId != 0){
			
			sql += " and cataId=?";
			params.add(cataId);
			model.addAttribute("cataId",cataId);
		}
		
		
		
		sql += " order by regtime desc";
		pageCompany = companyService.page(pageCompany,sql,params.toArray());
		Map<Company,UserInfo> userInfoMap = new HashMap<>();
		for (Company company : pageCompany.getResult()) {
			if(null != company.getUserId() ) {
				UserInfo userInfo = userInfoService.get(company.getUserId());
				userInfoMap.put(company, userInfo);
			}
		}
		
		
		// 企业分类
		List<CompanyCata> cataList = companyCataService.categorysSorting();
		model.addAttribute("cataList", cataList);
		
		// 获取企业对应的分类
		Map<Company,String> cataMap = new HashMap<Company,String>();
		for (Company company : pageCompany.getResult()){
			
		for (CompanyCata companyCata : cataList){
			
			if (StringUtils.isEmpty( company.getCataId() )){
				continue;
			}
			
			if (company.getCataId() == companyCata.getId()){
				
				cataMap.put(company, companyCata.getName());
				break;
			}
		 }
		
		
		}
		
		model.addAttribute("cataMap",cataMap);
		model.addAttribute("userInfoMap", userInfoMap);
		model.addAttribute("name", name);
		model.addAttribute("userId",userId);
		model.addAttribute("pageCompany", pageCompany);
		model.addAttribute("type", type);
		return "companyCheck/companyCheckList";
	}
	/**
	 * 详情
	 * @param model
	 * @param id
	 * @return
	 */
	
	@Autowired
	GatheringConditionService gatheringConditionService;
	
	@RequestMapping(value = "info", method = RequestMethod.GET)
	public String info(Model model, String id) {
		Company company = companyService.get(id);
		List<Operator> operatorList = operatorService.list();

		License license = null;
		if (StringUtils.isEmpty(company.getLicenseId())) {
			company.setLicenseId(" ");
			license = licenseService.get(company.getLicenseId());

		} else {
			license = licenseService.get(company.getLicenseId());
		}

		model.addAttribute("operatorList", operatorList);
		model.addAttribute("license", license);
		model.addAttribute("company", company);

		UserInfo userInfo = userInfoService.select(" from UserInfo where id = ? ", company.getUserId());
		model.addAttribute("userInfo", userInfo);

		String daily = ContactHelper.getDailyExpenditures(company.getDailyExpenditures());
		model.addAttribute("daily", daily);

		String purchasing = ContactHelper.getPurchasingCategory(company.getPurchasingCategorys());
		model.addAttribute("purchasing", purchasing);

		// 获取行业
		CompanyIndustry companyIndustry = companyIndustryService.get(company.getIndustryId());
		if (null != companyIndustry) {
			model.addAttribute("industryId", companyIndustry.getName());
		}
		
		//--------------- 付款条款 ------------------
		List<GatheringCondition> acLit = gatheringConditionService.list();
		model.addAttribute("billTimes", acLit);
		//---------------end ---------------------
		
		if (company.getCataId() == 12) {
			return "companyCheck/companyContract";
		} else if (company.getConcatState() != CompanyConcatState.NOTAPLLY) {
			// 说明企业提交合约申请
			return "companyCheck/companyAplly";
		} 
		return "companyCheck/companyCheckInfo";

	}
	
	/**
	 * 拒绝
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="denial",method=RequestMethod.GET)
	public String denial(Model model,String id,CompanySateEnum  type){
		Company company = companyService.get(id);
		UserInfo userInfo = userInfoService.select(" from UserInfo where id = ? ", company.getUserId());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("company", company);
		model.addAttribute("type", type);
		return "companyCheck/companyCheckDenial";
	}
	
	
	@Autowired
	WXUserInfoService wXUserInfoService;
	
	@Autowired
	MessageNodeNotified messageNodeNotified;
	
	/**
	 * 拒绝
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="denial",method=RequestMethod.POST)
	public String denialPOST(Model model,String id,String checkWarn,HttpServletRequest request) throws Exception{
		Company company = companyService.get(id);
		log.info("审核拒绝！{}",company.getName());
		company.setCompanySate(CompanySateEnum.UNPASS);
		company.setCheckWarn(checkWarn);
		
		
		companyService.save(company);
		
		UserInfo user = userInfoService.get(company.getUserId());
		WXUserInfo source = wXUserInfoService.select(" from WXUserInfo where miniOpenid = ? ", user.getWechatOpenid());
		if(source != null){
			user.setWechatUnioid(source.getUnionid());
			user.setWechatMpOpenid(source.getOpenid());
			userInfoService.save(user);
			messageNodeNotified.sendMessage(MessageNodeType.AUDIT_NOTICE,source.getOpenid(),company,user); //发送消息
			messageNodeNotified.sendMessage(MessageNodeType.LOGIN_SUCCESSFULLY_NOTICE,source.getOpenid(),company,user); //发送消息
		}
			 messageNodeNotified.sendMessage(SendType.AUDIT_RESULT, user,company,user); //企业认证
		//----------------------end------------------------------------
		//保存日志,HttpServletRequest request
   		 LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "拒绝企业审核:"+company.getName());
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 通过
	 * @param model
	 * @param id
	 * @param checkWarn
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "pass", method = RequestMethod.GET)
	public String passPost(Model model, String id, String checkWarn, HttpServletRequest request) throws Exception {
		Company company = companyService.get(id);
		log.info("审核通过！{}", company.getName());
		company.setPassTime(new Date());
		company.setCompanySate(CompanySateEnum.PASS);
		companyService.save(company);
		UserInfo user = userInfoService.get(company.getUserId());
		WXUserInfo source = wXUserInfoService.select(" from WXUserInfo where miniOpenid = ? ", user.getWechatOpenid());
		if (source != null) {
			user.setWechatUnioid(source.getUnionid());
			user.setWechatMpOpenid(source.getOpenid());
			userInfoService.save(user);
			messageNodeNotified.sendMessage(MessageNodeType.AUDIT_NOTICE, source.getOpenid(), company, user); // 发送消息
			messageNodeNotified.sendMessage(MessageNodeType.LOGIN_SUCCESSFULLY_NOTICE, source.getOpenid(), company,
					user); // 发送消息
		}
		messageNodeNotified.sendMessage(SendType.AUDIT_RESULT, user, company, user); // 企业认证
		// 保存日志,HttpServletRequest request
		LoginUser userif = UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "通过企业审核:" + company.getName());

		// --------------------- 同步数据----------------------
		SyncDataUtils.syncSoleDate(company.getUserId(),"/syncData/saveOrder","0");
		// ---------------------- end ----------------------
		
		return ResultUtil.getSuccessMsg();

	}
	
	/**
	 * 企业合约申请修改
	 */
	
	
	
	/**
	 * 修改
	 * @param id
	 * @param companySate
	 * @param extensionWorker
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public @ResponseBody String edit(@RequestParam(name = "id", required = true) String id, CompanySateEnum companySate,
			CompanyConcatState concatState, String extensionWorker, String checkWarn, HttpServletRequest request, String nowApplyCredists,Integer applyListTimeId,String applyListTimeName) throws Exception {
		Company company = companyService.get(id);

		company.setCheckWarn(checkWarn);
		if (companySate != null) {
			company.setCompanySate(companySate);
			CompanyConcatState cs;
			if (companySate.equals(CompanySateEnum.PASS)) {
				company.setPassTime(new Date());
				cs = CompanyConcatState.PASS;
			} else {
				cs = CompanyConcatState.UNPASS;
			}
			if (company.getCataId() == 12) {

				// 证明是合约客户
				company.setConcatState(cs);
				company.setNowApplyCredists(nowApplyCredists);
				company.setCredits(new BigDecimal(nowApplyCredists));
				company.setCreditOver(new BigDecimal(nowApplyCredists));
				
				//--------------付款条款----------
				GatheringCondition  gatheringCondition = gatheringConditionService.get(applyListTimeId);
				company.setApplyListTimeId(gatheringCondition.getErpCode());
				company.setApplyListTimeName(gatheringCondition.getConditionName());
				
				
			}
		} else if (null != concatState) {

			company.setConcatState(concatState);
			// 则是企业升级为合约企业修改
			if (concatState.equals(CompanyConcatState.PASS)) {
				company.setPassTime(new Date());
				company.setCataId(12);
				company.setNowApplyCredists(nowApplyCredists);
				company.setCredits(new BigDecimal(nowApplyCredists));
				company.setCreditOver(new BigDecimal(nowApplyCredists));
				
				//--------------付款条款----------
				GatheringCondition  gatheringCondition = gatheringConditionService.get(applyListTimeId);
				company.setApplyListTimeId(gatheringCondition.getErpCode());
				company.setApplyListTimeName(gatheringCondition.getConditionName());
			}
		}
		// 推广人员
		if (!StringUtils.isEmpty(extensionWorker)) {
			// 专管人员第一次默认为推广人员
			Operator operator = operatorService.get(extensionWorker);
			company.setOperatorId(extensionWorker);
			company.setOperatorName(operator.getUsername());
			company.setOperatorPhone(operator.getPhone());
			company.setExtensionWorker(operator.getUsername());
		}
		UserInfo user = userInfoService.get(company.getUserId());
		WXUserInfo source = wXUserInfoService.select(" from WXUserInfo where miniOpenid = ? ", user.getWechatOpenid());
		if (source != null) {
			user.setWechatUnioid(source.getUnionid());
			user.setWechatMpOpenid(source.getOpenid());
			userInfoService.save(user);
			messageNodeNotified.sendMessage(MessageNodeType.AUDIT_NOTICE, source.getOpenid(), company, user); // 发送消息
			messageNodeNotified.sendMessage(MessageNodeType.LOGIN_SUCCESSFULLY_NOTICE, source.getOpenid(), company,
					user); // 发送消息
		}
		if (!StringUtils.isEmpty(user.getEmail())) {
			messageNodeNotified.sendMessage(SendType.AUDIT_RESULT, user, company, user); // 企业认证
		}
		companyService.save(company);
		if(companySate != null && companySate.equals(CompanySateEnum.PASS)){
			// --------------------- 同步数据----------------------
			SyncDataUtils.syncSoleDate(company.getUserId(), "/syncData/saveOrder", "0");
			// ---------------------- end ----------------------
		}
		if(concatState != null  && concatState.equals(CompanySateEnum.PASS)){
			// --------------------- 同步数据----------------------
			SyncDataUtils.syncSoleDate(company.getUserId(), "/syncData/saveOrder", "0");
			// ---------------------- end ----------------------
		}
		// 保存日志,HttpServletRequest request
		LoginUser userif = UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "企业审核修改:" + company.getName());
		return ResultUtil.getSuccessMsg();
	}

}
