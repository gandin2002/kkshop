package cn.bohoon.syn.userInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.main.util.ResultUtil;
import cn.bohoon.userInfo.service.UserInfoService;

@Controller
public class SynUserInfoController {

	@Autowired
	UserInfoService userInfoService;
	
	@RequestMapping(value="synUserInfo",method=RequestMethod.GET)
	public String synUserInfo(){
		userInfoService.synUserInfo();
		return ResultUtil.getSuccess();
	}
	
}
