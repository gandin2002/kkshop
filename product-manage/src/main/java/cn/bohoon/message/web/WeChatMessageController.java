package cn.bohoon.message.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.message.domain.MessageNodeType;
import cn.bohoon.message.entity.MessageLog;
import cn.bohoon.message.service.MessageLogService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.wx.mp.entity.WxMsgTemplate;
import cn.bohoon.wx.mp.entity.WxMsgTemplateItem;
import cn.bohoon.wx.mp.service.WxMsgTemplatService;
import cn.bohoon.wx.mp.service.WxMsgTemplateItemService;

@Controller
@RequestMapping(value = "weChatMessage")
public class WeChatMessageController {

	@Autowired
	WxMsgTemplatService wxMsgTemplatService;
	@Autowired
	WxMsgTemplateItemService wxMsgTemplateItemService;
	@Autowired
	UserInfoService userInfoService;


    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	
	
	
	
	@Autowired
	MessageLogService messageLogService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model) {
		List<WxMsgTemplate> wxMsgTemplatList = wxMsgTemplatService.list(" from WxMsgTemplate");
		model.addAttribute("wxMsgTemplatList", wxMsgTemplatList);
		return "message/weChat/weChatList";
	}

	@RequestMapping(value = "custom", method = RequestMethod.GET)
	public String custom() {
		return "message/weChat/weChatCustom";
	}

	@RequestMapping(value = "sendBox", method = RequestMethod.GET)
	public String sendBox(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, String templateId,
			Boolean sendStatus, String msgType, @RequestParam(name = "endTime", defaultValue = "") String endTime,
			@RequestParam(name = "startTime", defaultValue = "") String startTime, Model model) throws ParseException {

		Page<MessageLog> page = new Page<MessageLog>(10);
		page.setPageNo(pageNo);

		String hql = " from  MessageLog where 1 = 1 ";

		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(templateId)) {
			hql += " and templateId = ? ";
			params.add(templateId);
			model.addAttribute("templateId", templateId);
		}
		if (!StringUtils.isEmpty(sendStatus)) {
			hql += " and sendStatus = ? ";
			params.add(sendStatus);
			model.addAttribute("sendStatus", sendStatus);
		}
		if (!StringUtils.isEmpty(msgType)) {
			hql = " and msgType = ? ";
			params.add(msgType);
			model.addAttribute("msgType", msgType);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql += " and sendTime >= ? ";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql += " and sendTime < ? ";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		page = messageLogService.page(page, hql, params.toArray());
		
		List<MessageLog> list=page.getResult();
		Map<MessageLog,UserInfo> userInfoMap  =new HashMap<MessageLog,UserInfo>();
		for (MessageLog messageLog : list) {
			UserInfo userInfo = userInfoService.select(" from UserInfo where  wechatMpOpenid = ? ", messageLog.getUserNumber());
			userInfoMap.put(messageLog, userInfo);
		}
		
	
		model.addAttribute("userInfoMap", userInfoMap);
		model.addAttribute("items", page);
		
		return "message/weChat/weChatSendBox";
	}

	@RequestMapping(value = "config", method = RequestMethod.GET)
	public String config(){
		return "message/weChat/weChatConfig";
	}

	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String detail(@RequestParam(name = "id", required = true) Integer id,Model model) {
		MessageLog messageLog = messageLogService.get(id);
		UserInfo userInfo = userInfoService.select(" from UserInfo where  wechatMpOpenid = ? ", messageLog.getUserNumber());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("messageLog", messageLog);
		return "message/weChat/weChatDetail";
	}

	@ResponseBody
	@RequestMapping(value = "editTemplat", method = RequestMethod.POST)
	public String editTemplat(@RequestParam(name = "template_id", required = true) String template_id,
			@RequestParam(name = "color") String[] colors, @RequestParam(name = "value") String[] values,
			@RequestParam(name = "name") String[] names,
			@RequestParam(name = "state", defaultValue = "false") Boolean state,
			@RequestParam(name = "msgType", defaultValue = "NOT_BINDING") String msgType,HttpServletRequest request) throws Exception {

		WxMsgTemplate wxMsgTemplate = wxMsgTemplatService.get(template_id);
		for (int i = 0; i < names.length; i++) {

			String name = names.length >= i ? names[i] : "";
			String color = colors.length >= i ? colors[i] : "";
			String value = values.length >= i ? values[i] : "";

			WxMsgTemplateItem wxMsgTemplateItem = wxMsgTemplateItemService.findTemplateItem(template_id, name);
			if (wxMsgTemplateItem == null) {
				wxMsgTemplateItem = new WxMsgTemplateItem();
				wxMsgTemplateItem.setTemplateId(template_id);
				wxMsgTemplateItem.setName(name);
			}
			wxMsgTemplateItem.setColor(color);
			wxMsgTemplateItem.setValue(value);
			wxMsgTemplateItemService.save(wxMsgTemplateItem);
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = simpleDateFormat.format(new Date());
		wxMsgTemplate.setSetupDate(time);
		wxMsgTemplate.setState(state);

		MessageNodeType messageNodeType = Enum.valueOf(MessageNodeType.class, msgType);
		wxMsgTemplate.setMessageNodeType(messageNodeType);

		wxMsgTemplatService.save(wxMsgTemplate);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增微信消息:"+wxMsgTemplate.getTemplate_id());
		return ResultUtil.getSuccessMsg();
	}

	@RequestMapping(value = "templateDetail", method = RequestMethod.GET)
	public String templateDetail(@RequestParam(name = "template_id", required = true) String template_id, Model model) {
		WxMsgTemplate wxMsgTemplate = wxMsgTemplatService.get(template_id);
		List<String> jsonArray = JSONArray.parseArray(wxMsgTemplate.getArraycnt(), String.class);
		Map<String, WxMsgTemplateItem> templateItemMap = new HashMap<String, WxMsgTemplateItem>();
		for (String string : jsonArray) {
			WxMsgTemplateItem wxMsgTemplateItem = wxMsgTemplateItemService.findTemplateItem(template_id, string);
			templateItemMap.put(string, wxMsgTemplateItem);
		}
		model.addAttribute("templateItemMap", templateItemMap);
		model.addAttribute("jsonArray", jsonArray);
		model.addAttribute("wxMsgTemplate", wxMsgTemplate);
		return "message/weChat/weTemplateDetail";
	}
	
	@ResponseBody
	@RequestMapping(value="deleteMessageLog",method=RequestMethod.POST)
	public String deleteMessageLog(@RequestParam(name="idArray[]")Integer idArray[],HttpServletRequest request) throws Exception{
		for (Integer integer : idArray) {
			messageLogService.delete(integer);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "批量删除微信消息:id"+org.apache.commons.lang.StringUtils.join(idArray,","));
		return ResultUtil.getSuccessMsg();
	}
}
