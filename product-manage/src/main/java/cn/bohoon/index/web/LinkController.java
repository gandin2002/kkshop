package cn.bohoon.index.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.basicSetup.helper.SysParamCache;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.service.UserService;

@Controller
@RequestMapping("/link")
public class LinkController {

	@Autowired
	UserService userService;
	@Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	@RequestMapping(value = "/orderHandleOrderOrdinary", method = RequestMethod.GET)
	public String orderHandleDetail(HttpServletRequest request,HttpServletResponse response, String id) throws Exception{
		LoginUser user = UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(user.getUsername()) ;
        operatorLogService.addUserLog(operator, request, "会员新增订单："+id);
       
        String hql = " select u from User u   where u.userInfoId = ?" ;
        List<User> list = userService.list(hql, id);
        User u = null ;
        if(list.size() >0 ){
			u = list.get(0) ;
		}
        String pcSite = SysParamCache.getCache().getSysParamValue(SysParamHelper.PC_SITE) ;
        if(!pcSite.contains("http://") && !pcSite.contains("https://") ){
            pcSite="http://"+pcSite;
        }
        System.out.println("redirect:"+pcSite+"/loginByOperator?uid="+u.getId()+"&upass="+u.getPassword());
        return "redirect:"+pcSite+"/loginByOperator?uid="+u.getId()+"&upass="+u.getPassword();
	}
	
	
}
