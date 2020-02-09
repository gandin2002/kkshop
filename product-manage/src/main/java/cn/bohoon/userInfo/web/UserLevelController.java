package cn.bohoon.userInfo.web;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.userInfo.entity.UserLevel;
import cn.bohoon.userInfo.service.UserLevelService;

@Controller
@RequestMapping(value = "userLevel")
public class UserLevelController {

    @Autowired
    UserLevelService userLevelService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);

        Page<UserLevel> pageUserLevel=new Page<UserLevel>(10);
        pageUserLevel.setPageNo(pageNo);


        pageUserLevel=userLevelService.page(pageUserLevel, "from UserLevel");

        model.addAttribute("pageUserLevel", pageUserLevel);
        return "userLevel/userLevelList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "userLevel/userLevelAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,UserLevel userLevel) throws Exception  {
        userLevelService.save(userLevel);
        //新增日志HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增会员等级:"+userLevel.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        UserLevel userLevel=userLevelService.get(id);
        model.addAttribute("item",userLevel);
        return "userLevel/userLevelEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(UserLevel userLevel,HttpServletRequest request) throws Exception {
        userLevelService.save(userLevel);
        //新增日志HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改会员等级:"+userLevel.getName());
        return ResultUtil.getSuccessMsg();
    }

	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        userLevelService.delete(id);
        //新增日志HttpServletRequest request
        LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除会员等级id:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}