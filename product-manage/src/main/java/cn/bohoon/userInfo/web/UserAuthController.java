package cn.bohoon.userInfo.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
import cn.bohoon.userInfo.domain.AuthState;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.util.SyncDataUtils;
import cn.bohoon.wx.mp.entity.WXUserInfo;
import cn.bohoon.wx.mp.service.WXUserInfoService;

@Controller
@RequestMapping(value = "userAuth")
public class UserAuthController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserInfoService userInfoService ;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, UserInfo searchModel,Integer display,AuthState idCardAuthStates) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		Page<UserInfo> userInfoPage = new Page<UserInfo>();
		userInfoPage.setPageNo(pageNo);

		String hql = " from UserInfo t where 1=1 ";
		List<Object> params = new ArrayList<>();
		if(idCardAuthStates !=null ){
			hql += " and t.idCardAuthState=?";
			params.add(idCardAuthStates);
		}
		searchModel.setIdCardAuthState(idCardAuthStates);
		model.addAttribute("idCardAuthStates", idCardAuthStates);
		if (!StringUtils.isEmpty(searchModel.getRealname())) {
			hql += " and t.realname like ? ";
			params.add('%' + searchModel.getRealname() + '%');
		}
		
		if (!StringUtils.isEmpty(searchModel.getIdcard())) {
			hql += " and t.idcard like ?";
			params.add('%' + searchModel.getIdcard() + '%');
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql = hql + " and t.createTime >= ?";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and t.createTime < ?";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		
		hql = hql.concat(" order by applicationTime desc");
		

		userInfoPage = userInfoService.page(userInfoPage, hql, params.toArray());

		model.addAttribute("searchModel", searchModel);
		model.addAttribute("userInfoPage", userInfoPage);
		return "userAuth/userAuthList";
	}

	/**
	 * 去详情页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(Model model, String id) {
		UserInfo userInfoModel = userInfoService.get(id);
		model.addAttribute("userInfoModel", userInfoModel);
		return "userAuth/userAuthDetail";
	}

	/**
	 * 去拒绝页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/refuse", method = RequestMethod.GET)
	public String refuseGet(Model model, String id) {
		UserInfo userInfoModel = userInfoService.get(id);
		model.addAttribute("userInfoModel", userInfoModel);
		return "userAuth/userAuthRefuse";
	}

	@Autowired
	MessageNodeNotified messageNodeNotified;
	
	@Autowired
	WXUserInfoService wXUserInfoService;
	/**
	 * 审核拒绝
	 * 
	 * @param model
	 * @param ua
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/refuse", method = RequestMethod.POST)
	@ResponseBody
	public String refusePost(Model model, UserInfo entity,HttpServletRequest request) throws Exception {
		UserInfo userInfoModel = userInfoService.get(entity.getId());
		model.addAttribute("userInfoModel", userInfoModel);
		userInfoModel.setIdCardAuthState(AuthState.AUTH_REFUSE) ;
		userInfoModel.setIdCardAuthDesc(entity.getIdCardAuthDesc());
		userInfoService.save(userInfoModel);
		
		messageNodeNotified.sendMessage(SendType.AUDIT_RESULT, userInfoModel,userInfoModel); //企业认证
		
		WXUserInfo source = wXUserInfoService.select(" from WXUserInfo where miniOpenid = ? ", userInfoModel.getWechatMpOpenid());
		if(!StringUtils.isEmpty(source)){
			logger.info("wxUserInfo========="+source);
			userInfoModel.setWechatUnioid(source.getUnionid());
			userInfoModel.setWechatMpOpenid(source.getOpenid()); 
			userInfoService.save(userInfoModel);
			messageNodeNotified.sendMessage(MessageNodeType.AUDIT_NOTICE,source.getOpenid(),userInfoModel); //发送消息
		}
		 //新增日志,HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "会员审核拒绝:电话"+userInfoModel.getPhone());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 审核通过
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/pass", method = RequestMethod.GET)
	@ResponseBody
	public String pass(Model model, String id,HttpServletRequest request) throws Exception {
		UserInfo userInfoModel = userInfoService.get(id);
		userInfoModel.setIdCardAuthState(AuthState.AUTH_PASS);
		userInfoModel.setIdCardAuthDesc(AuthState.AUTH_PASS.getName());
		userInfoService.save(userInfoModel);
		//新增日志,HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "会员审核通过:电话"+userInfoModel.getPhone());
		
		// --------------------- 同步数据----------------------
		SyncDataUtils.syncSoleDate(id,"/syncData/saveUserOrder", "0");
		// ---------------------- end ----------------------
		
		return ResultUtil.getSuccessMsg();
	}
	/**
	 * 修改
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public 	@ResponseBody String edit(@RequestParam(name="id",required=true) String id,AuthState authState,String idCardAuthDesc,HttpServletRequest request) throws Exception {
		if(authState != null){
			UserInfo userInfo = userInfoService.get(id);
			
			
			if ("AUTH_REFUSE".equals(authState.toString())){
				
				// 认证失败
				userInfo.setIdCardAuthState(authState);
				userInfo.setIdCardAuthDesc(idCardAuthDesc);
				userInfoService.save(userInfo);
			}else{
				userInfo.setIdCardAuthState(authState);
				userInfo.setIdCardAuthDesc(AuthState.AUTH_PASS.getName());
				userInfoService.save(userInfo);
				// --------------------- 同步数据----------------------
				SyncDataUtils.syncSoleDate(id,"/syncData/saveUserOrder", "0");
				// ---------------------- end ----------------------
			}
			//新增日志,HttpServletRequest request
	        LoginUser userif= UserSession.getLoginUser(request);
			Operator operator = operatorService.findUserByUsername(userif.getUsername());
			operatorLogService.addUserLog(operator, request, "会员审核修改");
		}
		return ResultUtil.getSuccessMsg();
	}
}
