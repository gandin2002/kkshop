package cn.bohoon.message.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.message.domain.EmailConfig;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.entity.MessageConfig;
import cn.bohoon.message.entity.MessageEmail;
import cn.bohoon.message.entity.MessageTemplate;
import cn.bohoon.message.service.EmailService;
import cn.bohoon.message.service.MessageConfigService;
import cn.bohoon.message.service.MessageEmailService;
import cn.bohoon.message.service.MessageTemplateService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "messageEmail")
public class MessageEmailController {

	@Autowired
	MessageConfigService messageConfigService;;

	@Autowired
	EmailService emailService;

	@Autowired
	MessageEmailService messageEmailService;

	@Autowired
	MessageTemplateService messageTemplateService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	@RequestMapping(value = "emailTemplates", method = RequestMethod.GET)
	public String emailTemplates(Model model) {
		List<MessageTemplate> list = messageTemplateService.list(" from MessageTemplate where type = ? ",
				MessageType.Email);

		model.addAttribute("list", list);
		return "message/email/emailTemplates";
	}

	@RequestMapping(value = "customEmail", method = RequestMethod.GET)
	public String custom() {
		return "message/email/customEmail";
	}

	@ResponseBody
	@RequestMapping(value = "sendEmail", method = RequestMethod.POST)
	public String sendEmail(String title, String addressee, String content,HttpServletRequest request) throws Exception {
		MessageConfig messageConfig = messageConfigService.getMessageConfig(MessageType.Email);
		EmailConfig emailConfig = messageConfig.emailConfig();
		emailService.initConfig(emailConfig);
		Boolean sendState = emailService.sendHtmlMail(addressee, title, content);
		MessageEmail messageEmail = new MessageEmail();
		messageEmail.setBeFrom(emailConfig.getFrom());
		messageEmail.setContent(content);
		messageEmail.setSendDate(new Date());
		messageEmail.setSendTo(addressee);
		messageEmail.setTitle(title);
		messageEmail.setState(sendState);
		messageEmailService.save(messageEmail);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增邮件消息:id"+messageEmail.getId().toString());
		return ResultUtil.getSuccessMsg();
	}

	@ResponseBody
	@RequestMapping(value = "resend", method = RequestMethod.GET)
	public String resend(int id,HttpServletRequest request) throws Exception {
		MessageEmail messageEmail = messageEmailService.get(id);
		MessageConfig messageConfig = messageConfigService.getMessageConfig(MessageType.Email);
		EmailConfig emailConfig = messageConfig.emailConfig();
		emailService.initConfig(emailConfig);
		Boolean sendState = emailService.sendHtmlMail(messageEmail.getSendTo(), messageEmail.getTitle(),
				messageEmail.getContent());
		messageEmail.setState(sendState);
		messageEmailService.save(messageEmail);
		if (sendState) {
			//保存日志,HttpServletRequest request
	   		LoginUser userif= UserSession.getLoginUser(request);
			Operator operator = operatorService.findUserByUsername(userif.getUsername());
			operatorLogService.addUserLog(operator, request, "发送成功邮件消息:id"+messageEmail.getId().toString());
			return ResultUtil.getSuccessMsg();
		}
		return ResultUtil.getError("发送失败");
	}

	@RequestMapping(value = "sendBox", method = RequestMethod.GET)
	public String sendBox(Model model, HttpServletRequest request, String title, String startTime, String endTime,Boolean state,
			Boolean emailTemplate) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<MessageEmail> messageEmailPage = new Page<>();
		messageEmailPage.setPageNo(pageNo);
		String hql = " from MessageEmail where 1 = 1  ";
		List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(title)) {
			hql += " and title like ? ";
			params.add("%" + title + "%");
		}
		if (!StringUtils.isEmpty(state)) {
			hql += " and state = ? ";
			params.add(state);
			model.addAttribute("state", state);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql = hql + " and sendDate >= ? ";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and sendDate < ? ";
			params.add(DateUtil.getNDayAfter(endTime, 1));
		}
		if (!StringUtils.isEmpty(emailTemplate)) {
			hql = hql + (emailTemplate ? " and sendType is not null" : " and sendType is null");
		}
		hql += "  order by id desc ";
		messageEmailPage = messageEmailService.page(messageEmailPage, hql, params.toArray());
		model.addAttribute("messageEmailPage", messageEmailPage);
		model.addAttribute("title", title);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "message/email/sendBox";
	}

	@RequestMapping(value = "emailConfig", method = RequestMethod.GET)
	public String emailConfigGet(Model model) {
		MessageConfig messageConfig = messageConfigService.getMessageConfig(MessageType.Email);
		model.addAttribute("messageConfig", messageConfig);
		return "message/email/emailConfig";
	}

	@RequestMapping(value = "emailConfig", method = RequestMethod.POST)
	public @ResponseBody String emailConfigPost(Integer id, EmailConfig emailConfig,HttpServletRequest request) throws Exception {
		MessageConfig mc = new MessageConfig();
		mc.setId(id);
		mc.setType(MessageType.Email);
		mc.setConfigJson(JsonUtil.toJson(emailConfig));
		messageConfigService.save(mc);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增邮件消息:id"+id.toString());
		return ResultUtil.getSuccessMsg();
	}

}
