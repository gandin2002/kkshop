package cn.bohoon.message.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.domain.SmsConfig;
import cn.bohoon.message.entity.MessageConfig;
import cn.bohoon.message.entity.MessageTemplate;
import cn.bohoon.message.service.MessageConfigService;
import cn.bohoon.message.service.MessageSmsService;
import cn.bohoon.message.service.MessageTemplateService;

@Controller
@RequestMapping(value = "messageSms")
public class MessageSmsController {

	@Autowired
	MessageTemplateService messageTemplateService;
	
	@Autowired
	MessageSmsService messageSmsService;

	@Autowired
	MessageConfigService messageConfigService;
	@Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	@RequestMapping(value = "smsTemplates", method = RequestMethod.GET)
	public String list(Model model) {
		List<MessageTemplate> list = messageTemplateService.list(" from MessageTemplate where type = ? ",
				MessageType.Sms);
		model.addAttribute("list", list);
		return "message/sms/smsTemplates";
	}

	@RequestMapping(value = "sendBox", method = RequestMethod.GET)
	public String sendBox() {
		return "message/sms/smsSendBox";
	}

	@RequestMapping(value = "smsConfig", method = RequestMethod.GET)
	public String smsConfig(Model model) {
		MessageConfig messageConfig = messageConfigService.getMessageConfig(MessageType.Sms);
		model.addAttribute("messageConfig", messageConfig);
		return "message/sms/smsConfig";
	}
	
	@ResponseBody
	@RequestMapping(value = "smsConfig", method = RequestMethod.POST)
	public String emailConfigPost(Integer id, SmsConfig smsConfig,HttpServletRequest request) throws Exception {
		MessageConfig mc = new MessageConfig();
		mc.setId(id);
		mc.setType(MessageType.Sms);
		mc.setConfigJson(JsonUtil.toJson(smsConfig));
		messageConfigService.save(mc);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "存储短信消息:"+mc.getId().toString());
		return ResultUtil.getSuccessMsg();
	}

	@RequestMapping(value = "smsDetail", method = RequestMethod.GET)
	public String smsDetail() {
		return "message/sms/smsDetail";
	}
}
