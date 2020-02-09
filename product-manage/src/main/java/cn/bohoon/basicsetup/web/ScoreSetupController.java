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
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.UploadService;
import cn.bohoon.util.ResultUtil;
/**
 * 积分赠送设置
 * @author HJ
 * 2018年1月2日,下午1:47:50
 */
@Controller
public class ScoreSetupController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SysParamService sysParamService;
	
	@Autowired
	UploadService uploadService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	/**
	 * 参数展示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="display",method=RequestMethod.GET)
	public String display(Model model){
		List<SysParam> sysParamList = sysParamService.findCodeList(SysParamType.SCORE_PARAM);
		model.addAttribute("sysParamList", sysParamList);
		return "basicSetup/productsetup/productSetup";
	}
	
	/**
	 * 修改参数
	 * 
	 * @param sysParamsModel
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public String modify(SysParamsModel sysParamsModel,HttpServletRequest request) throws Exception {
		List<SysParam> list = sysParamsModel.getList();
		sysParamService.saveBatch(list, list.size());
		logger.info("{}修改产品设置","张三");
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改积分赠送参数");
		return ResultUtil.getSuccessMsg();
	}
	
}
