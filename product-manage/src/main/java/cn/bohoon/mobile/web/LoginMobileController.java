package cn.bohoon.mobile.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.util.CipherUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.entity.Permission;
import cn.bohoon.main.system.entity.Resource;
import cn.bohoon.main.system.service.AuthService;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.ResourceService;

/**
 * Created by Administrator on 2017/9/12 0012.
 */
@Controller
@RequestMapping(value = "mobile")
public class LoginMobileController {

    @Autowired
    OperatorService operatorService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    AuthService authService ;

    
    @Autowired
	ResourceService resourceService;


    Logger logger = LoggerFactory.getLogger(getClass()) ;

    /**
     * 登陆页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, Model model) {
        String returnback = ServletRequestUtils.getStringParameter(request, "returnback", "/");
        model.addAttribute("returnback", returnback);
        return "mobile/login";
    }

    /**
     * 提交登陆 form 表单
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        String password = ServletRequestUtils.getStringParameter(request, "password", "");
        Operator operator = operatorService.findUserByUsername(username);
        if (StringUtils.isEmpty(operator) || !operator.getPassword().equals(CipherUtil.md5(password))) {
            model.addAttribute("msg", "用户名或密码不对");
            return "redirect:/mobile/login";
        }

        UserSession.addLoginUser(operator.getLoginUser(), request, response);
        operatorLogService.addUserLog(operator, request, "登录");
        return "redirect:/mobile/index";
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout")
    public String signoff(HttpServletRequest request, HttpServletResponse response) {
        UserSession.removeLoginUser(request, response);
        return "redirect:/login";
    }
    
    
    //主页
    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public String main(Model model, HttpServletRequest request) throws Exception {
    	List<Resource> resourceList = resourceService.indexTopMenu() ;
    	Resource resource = resourceList.size() > 0 ?resourceList.get(0) : null ;
    	if(null != resource) {
    		resource = resourceService.indexLeftMenu(resource) ;
    	}
    	
    	model.addAttribute("resource", resource);
		model.addAttribute("resourceList", resourceList);
        return "mobile/index/index";
    }
    

	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) throws Exception {
		
    	List<Resource> resourceList = resourceService.indexTopMenu() ;
    	Resource resource = resourceList.size() > 0 ?resourceList.get(0) : null ;
    	if(null != resource) {
    		resource = resourceService.indexLeftMenu(resource) ;
    	}
    	model.addAttribute("resource", resource);
		model.addAttribute("resourceList", resourceList);
        return "index";
    }


    @RequestMapping(value = "/index/welcome", method = RequestMethod.GET)
    public String welcome(Model model, HttpServletRequest request) throws Exception {
        return "welcome";
    }


    @RequestMapping(value = "/permissionCode", method = RequestMethod.GET)
    @ResponseBody
    public String operationCode(Model model, HttpServletRequest request) throws Exception {
        LoginUser loginUser = UserSession.getLoginUser(request);
        Operator operator = operatorService.findUserByUsername(loginUser.getUsername());
        List<Permission> pList = authService.listOperatorPermission(operator.getId());
        return JsonUtil.toJson(pList);
    }
    
	@RequestMapping(value = "/resource/subLeft", method = RequestMethod.GET)
	public String getSubmenu(Integer id, Model model) {
		Resource resource = resourceService.get(id) ;
		if(null != resource) {
    		resource = resourceService.indexLeftMenu(resource) ;
    	}
		model.addAttribute("resource", resource);
		return "_left";
	}

}
