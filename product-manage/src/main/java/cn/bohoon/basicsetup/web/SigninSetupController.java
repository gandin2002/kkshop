package cn.bohoon.basicsetup.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.UploadService;
/**
 * 签到设置
 * @author HJ
 * 2018年1月12日,上午9:51:23
 */
@Controller
@RequestMapping(value="signinSetup")
public class SigninSetupController {

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
	 * @param modelo
	 * @return
	 */
	@RequestMapping(value="display",method=RequestMethod.GET)
	public String display(Model model){
		List<SysParam> sysParamList = sysParamService.findCodeList(SysParamType.SIGNIN_PARAM);
		model.addAttribute("sysParamList", sysParamList);
		return "basicSetup/signSetup/signSetup";
	}
	
	

}
