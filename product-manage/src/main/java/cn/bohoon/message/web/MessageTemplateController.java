package cn.bohoon.message.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.util.ResultUtil;
import cn.bohoon.message.entity.MessageTemplate;
import cn.bohoon.message.service.MessageTemplateService;

@Controller
@RequestMapping(value = "messageTemplate")
public class MessageTemplateController {
	
	@Autowired
	MessageTemplateService messageTemplateService;

	@RequestMapping(method = RequestMethod.GET)
	public String templateModify(Integer id, Model model) {
		MessageTemplate messageTemplate = messageTemplateService.get(id);
		model.addAttribute("messageTemplate", messageTemplate);
		String type= messageTemplate.getType().name();
		model.addAttribute("toUrl","/message"+type+"/"+StringUtils.uncapitalize(type)+"Templates");
		return "message/messageTemplate";
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public String templateModify(MessageTemplate messageTemplate) {
		MessageTemplate target = messageTemplateService.get(messageTemplate.getId());
		target.setContent(messageTemplate.getContent());
		target.setTitle(messageTemplate.getTitle());
		target.setState(messageTemplate.getState());
		messageTemplateService.save(target);
		return ResultUtil.getSuccessMsg();
	}
	/*
	 * 
	@RequestMapping(value = "templateModify", method = RequestMethod.GET)
	public String templateModif(Integer id, Model model) {
		MessageTemplate messageTemplate = messageTemplateService.get(id);
		model.addAttribute("messageTemplate", messageTemplate);
		model.addAttribute("url", "smsMessage");
		return "message/template";
	}

	@ResponseBody
	@RequestMapping(value = "templateModify", method = RequestMethod.POST)
	public String templateModify(MessageTemplate messageTemplate) {
		MessageTemplate target = messageTemplateService.get(messageTemplate.getId());
		target.setContent(messageTemplate.getContent());
		target.setTitle(messageTemplate.getTitle());
		target.setState(messageTemplate.getState());
		messageTemplateService.save(target);
		return ResultUtil.getSuccessMsg();
	}
	 */
}
