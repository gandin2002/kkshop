package cn.bohoon.basicsetup.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.domain.SysParamsModel;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.OrderSetupService;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
/**
 * 订单相关设置
 * @author HJ
 * 2017年11月13日,上午11:37:58
 */
@Controller
@RequestMapping(value = "orderSetup")
public class OrderSetupController {

	Logger log =  LoggerFactory.getLogger(OrderSetupController.class);

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	@Autowired
	OrderSetupService orderSetupService;
	
	@Autowired
	SysParamService sysParamService;

 
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		List<SysParam> sysParamList = sysParamService.findCodeList(SysParamType.ORDER_PARAM);
		model.addAttribute("sysParamList", sysParamList);
		return "basicSetup/orderSetup/orderSetupList";
	}

	/**
	 * 订单设置修改
	 * 
	 * @param sysParamsModel
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(SysParamsModel sysParamsModel,HttpServletRequest request) throws Exception {
		List<SysParam> list = sysParamsModel.getList();
		sysParamService.saveBatch(list, list.size());
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "订单设置修改");
		return ResultUtil.getSuccessMsg();
	}
}