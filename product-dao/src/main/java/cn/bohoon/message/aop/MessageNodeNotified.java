package cn.bohoon.message.aop;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.bohoon.framework.util.RequestUtil;
import cn.bohoon.message.domain.EmailConfig;
import cn.bohoon.message.domain.MessageContentType;
import cn.bohoon.message.domain.MessageNodeType;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.domain.SendType;
import cn.bohoon.message.entity.MessageConfig;
import cn.bohoon.message.entity.MessageLog;
import cn.bohoon.message.entity.MessageSite;
import cn.bohoon.message.entity.MessageTemplate;
import cn.bohoon.message.service.EmailService;
import cn.bohoon.message.service.MessageConfigService;
import cn.bohoon.message.service.MessageLogService;
import cn.bohoon.message.service.MessageSiteService;
import cn.bohoon.message.service.MessageTemplateService;
import cn.bohoon.message.service.SmsService;
import cn.bohoon.page.entity.SmsLog;
import cn.bohoon.page.service.SmsLogService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ValidateFillingUtil;
import cn.bohoon.util.VelocityHtmlUtils;
import cn.bohoon.wx.mp.entity.WxMsgTemplate;
import cn.bohoon.wx.mp.service.WxMsgTemplatService;

@Component
public class MessageNodeNotified {
	private Logger logger = LoggerFactory.getLogger(MessageNodeNotified.class);

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	WxMsgTemplatService wxMsgTemplatService;

	@Autowired
	MessageLogService messageLogService;

	@Autowired
	MessageTemplateService MessageTemplateService;

	@Autowired
	EmailService emailService;

	@Autowired
	MessageConfigService messageConfigService;

	@Autowired
	SmsService smsService;

	@Autowired
	SmsLogService smsLogService;
	
	@Autowired
	MessageSiteService messageSiteService;

	/**
	 * 发送微信消息
	 * 
	 * @param messageNodeType
	 * @param mpOpenid
	 * @param obejct
	 */
	public void sendMessage(MessageNodeType messageNodeType, String mpOpenid, Object... obejct) {
		List<WxMsgTemplate> wxMsgTemplate = wxMsgTemplatService
				.list(" from  WxMsgTemplate where messageNodeType = ? and state = 1 ", messageNodeType);
		for (WxMsgTemplate wxMsgTemplate2 : wxMsgTemplate) {
			String templatId = wxMsgTemplate2.getTemplate_id();
			VelocityContext velocity = ValidateFillingUtil.fillingDate(obejct);
			Map<String, String> map = wxMsgTemplatService.velocityReplace(templatId, velocity);

			Boolean state = wxMsgTemplatService.sendMsg(templatId, mpOpenid, map);

			if (state) {
				wxMsgTemplate2.setSucceedNum(wxMsgTemplate2.getSucceedNum() + 1L);
			} else {
				wxMsgTemplate2.setSucceedNum(wxMsgTemplate2.getFailureNum() + 1L);
			}

			MessageLog messageLog = new MessageLog();
			messageLog.setContent(JSON.toJSONString(map));
			messageLog.setSendTime(new Date());
			messageLog.setSendStatus(state);
			messageLog.setUserNumber(mpOpenid);
			messageLog.setMsgType("模板消息");
			messageLog.settMsgType(messageNodeType.getName());
			messageLog.setTemplateId(templatId);
			messageLogService.save(messageLog);
			wxMsgTemplatService.save(wxMsgTemplate2);
		}

		logger.info("----------------------消息发送成功---------------------");
	}

	
	public void sendMessage(SendType sendType,MessageType messageType, UserInfo userInfo, Object... obejct){
		List<MessageTemplate> mtlist = MessageTemplateService.findTemplate(sendType,messageType);
		this.sendMessage(mtlist, userInfo, obejct);
	}
	
	public void sendMessage(SendType sendType, UserInfo userInfo, Object... obejct) {
		List<MessageTemplate> mtlist = MessageTemplateService.findTemplate(sendType);
		this.sendMessage(mtlist, userInfo, obejct);
	}
	
	public void sendMessage(List<MessageTemplate> mtlist, UserInfo userInfo, Object... obejct) {
		for (int i = 0; i < mtlist.size(); i++) {
			Boolean state = new Boolean(false);
			MessageTemplate messageTemplate = mtlist.get(i);
			MessageType type = messageTemplate.getType(); 

			VelocityContext velocity = ValidateFillingUtil.fillingDate(obejct);
			// 解析模板
			if(messageTemplate.getContent()==null){
				return ;
			}
			String content = VelocityHtmlUtils.vm(messageTemplate.getContent(), velocity);
			
			logger.info("--------------------"+content+"--------------------");
			
			String to = "";
			
			if (MessageType.Email.equals(type)) { // 邮箱
				if(StringUtils.isEmpty(userInfo.getEmail())){
					continue;
				}
				to = userInfo.getEmail();
				state = sendEmail(to, messageTemplate.getTitle(), content);
			}else if (MessageType.Sms.equals(type)) { // 短信
				if(StringUtils.isEmpty(userInfo.getPhone())){
					continue;
				}
				to = userInfo.getPhone();
				state = smsSend(userInfo.getPhone(), content);
			}else if(MessageType.Site.equals(type)){//站内
				
				MessageSite msgEntity = new MessageSite() ;
				msgEntity.setBeFrom("system");
				msgEntity.setContent(content);
				msgEntity.setMessageContentType(MessageContentType.SYSINFO);
				msgEntity.setSendDate(new Date());
				msgEntity.setSendTo(userInfo.getId());
				msgEntity.setSendType(messageTemplate.getSendType());
				msgEntity.setTitle(messageTemplate.getTitle());
				msgEntity.setUserType(true);
				
				messageSiteService.save(msgEntity); 
			}

			if (state) {
				messageTemplate.setSucceedNum(messageTemplate.getSucceedNum() + 1L);
			} else {
				messageTemplate.setSucceedNum(messageTemplate.getFailureNum() + 1L);
			}

			MessageLog messageLog = new MessageLog();

			messageLog.setContent(content);
			messageLog.setSendTime(new Date());
			messageLog.setSendStatus(state);
			messageLog.setUserNumber(to);
			messageLog.setMsgType("模板消息");
			messageLog.settMsgType(type.getName());
			messageLog.setTemplateId(messageTemplate.getId().toString());
			messageLogService.save(messageLog);
			
			MessageTemplateService.save(messageTemplate);
		}
	}

	public boolean smsSend(String to, String content) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String ipAdress = RequestUtil.getRemoteAddr(request);
		boolean cannot = smsLogService.canSendSms(ipAdress);
		if (cannot) {
			logger.info("----------------------短信消息发送过于频繁---------------------");
			return false;
		}
		String result = smsService.smsSend(to, content);
		JSONObject json = JSON.parseObject(result);
		if (json.containsKey("Code") && !"0".equals(json.get("Code"))) {
			logger.info("----------------------消息发送失败---------------------");
			return false;
		} else {
			SmsLog sl = new SmsLog(ipAdress, to, content);
			smsLogService.save(sl);
		}
		return true;
	}

	public boolean sendEmail(String to, String title, String content) {
		MessageConfig messageConfig = messageConfigService.getMessageConfig(MessageType.Email);
		EmailConfig emailConfig = messageConfig.emailConfig();
		emailService.initConfig(emailConfig);
		return emailService.sendHtmlMail(to, title, content);
	}
}