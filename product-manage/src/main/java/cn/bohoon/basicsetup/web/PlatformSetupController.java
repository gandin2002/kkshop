package cn.bohoon.basicsetup.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.domain.SysParamsModel;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamCache;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.UploadService;
import cn.bohoon.util.ResultUtil;

/**
 * 商城配置
 * 
 * @author HJ 2017年12月18日,上午10:57:24
 */
@Controller
public class PlatformSetupController {
	
	@Autowired
	SysParamService sysParamService;

	@Autowired
	UploadService uploadService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


	/**
	 * 商城参数展示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "platform/display", method = RequestMethod.GET)
	public String display(Model model) {
		List<SysParam> sysParamList = sysParamService.findCodeList(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamList", sysParamList);
		return "basicSetup/platform/platform";
	}

	/**
	 * 修改商城参数
	 * 
	 * @param sysParamsModel
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "platform/modify", method = RequestMethod.POST)
	public String modify(SysParamsModel sysParamsModel,HttpServletRequest request) throws Exception {
		
		List<SysParam> list = sysParamsModel.getList();
		sysParamService.saveBatch(list, list.size());
		SysParamCache.getCache().init();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改商城参数");
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 上传图片
	 * 
	 * @param multipartFile
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="platform/uploadImg")
	public String uploadingImg(@RequestParam("file") MultipartFile multipartFile) throws Exception{
		String image = uploadService.handleFileUploadNoDomain(multipartFile, "sys");
		return ResultUtil.getMessage(image);
	}
	
	
	
	/**
	 * 上传文件
	 * 
	 * @param multipartFile
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="platform/uploadFile")
	public String uploadingFile(@RequestParam("file") MultipartFile multipartFile) throws Exception{
		String fileName = uploadService.handleFileUploadFile(multipartFile, "file");
		return ResultUtil.getMessage(fileName);
	}

}
