package cn.bohoon.message.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.entity.Role;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.RoleService;
import cn.bohoon.main.util.ResultUtil;
import cn.bohoon.message.entity.MessageRemind;
import cn.bohoon.message.entity.MessageRemindInfo;
import cn.bohoon.message.entity.MessageRemindLog;
import cn.bohoon.message.service.MessageRemindInfoService;
import cn.bohoon.message.service.MessageRemindLogService;
import cn.bohoon.message.service.MessageRemindService;
/**
 * 消息提醒
 * @author HJ
 * 2017年12月27日,下午1:48:38
 */
@Controller
@RequestMapping(value="messageRemind")
public class MessageRemindController {
	
	@Autowired
	MessageRemindService messageRemindService;
	
	@Autowired
	MessageRemindInfoService messageRemindInfoService;
	
	@Autowired
	MessageRemindLogService messageRemindLogService;
	
	@Autowired
	OperatorService operatorService;
	
	@Autowired
	RoleService roleService;

    @Autowired
    OperatorLogService operatorLogService;
   
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public String show(Model model){
		List<MessageRemind> list = messageRemindService.list();
		model.addAttribute("list", list);
		return "message/remind/remindList";
	}
	
    @ResponseBody
    @RequestMapping(value = "/modifiedStatus", method = RequestMethod.GET)
    public String modifiedStatus(HttpServletRequest request, Integer id) throws Exception{
    	MessageRemind messageRemind=messageRemindService.get(id);
    	messageRemind.setState(!messageRemind.getState());
    	messageRemindService.save(messageRemind);
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改消息提醒启用状态:"+messageRemind.getTitle());
    	return  ResultUtil.getSuccess();
    }
    
    /**
     * 拿取消息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="getMsg",method=RequestMethod.POST)
    public  @ResponseBody String getMsg(HttpServletRequest request) throws Exception{
    	LoginUser loginUser = UserSession.getLoginUser(request);
    	Operator operator = operatorService.findUserByUsername(loginUser.getUsername());
    	List<Role> roleList = roleService.list(" select r from OperatorRole o, Role r where o.roleId = r.id and o.operatorId = ? ",operator.getId()); //查出拥有角色
    	List<MessageRemindInfo> mrilist = new ArrayList<>();
    	for (Role role : roleList) {
			List<MessageRemindInfo> list = messageRemindInfoService.list(
					"select mi from MessageRemindInfo mi,MessageRemindRole  mr  where mi.messageRemindId = mr.mrId and mr.roleId = ?  and mi.id not in(select msgId from MessageRemindLog  where  operatorId = ? )",
					role.getId(), operator.getId());
    		 mrilist.addAll(list);
    	}
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator1 = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator1, request, "拿取消息");
    	return JsonUtil.toJson(mrilist);
    }
    /**
     * 答应消息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="respond",method=RequestMethod.POST)
    public  @ResponseBody String respond(@RequestParam(name="mriId[]") Integer[] mriId,HttpServletRequest request) throws Exception{
    	LoginUser loginUser = UserSession.getLoginUser(request);
    	Operator operator = operatorService.findUserByUsername(loginUser.getUsername());
    	for (Integer integer : mriId) {
    		MessageRemindInfo messageRemindInfo  = messageRemindInfoService.get(integer);
    		MessageRemindLog messageRemindLog= new MessageRemindLog();
    		messageRemindLog.setMsgId(messageRemindInfo.getId());
    		messageRemindLog.setOperatorId(operator.getId());
    		messageRemindLog.setReadDate(new Date());
    		messageRemindLogService.save(messageRemindLog);
    	}
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator1 = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator1, request, "答应消息");
    	return ResultUtil.getSuccess();
    }

	
}
