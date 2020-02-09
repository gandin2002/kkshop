package cn.bohoon.message.web;

import java.text.ParseException;
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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.message.domain.MessageContentType;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.domain.SendType;
import cn.bohoon.message.entity.MessageSite;
import cn.bohoon.message.entity.MessageTemplate;
import cn.bohoon.message.service.MessageSiteService;
import cn.bohoon.message.service.MessageTemplateService;

@Controller
@RequestMapping(value = "messageSite")
public class MessageSiteController {

	@Autowired
	MessageTemplateService messageTemplateService;

	@Autowired
	MessageSiteService messageSiteService;

	@Autowired
	CompanyService companyService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	@RequestMapping(value = "siteTemplates", method = RequestMethod.GET)
	public String siteTemplates(Model model) {
		List<MessageTemplate> list = messageTemplateService.list(" from MessageTemplate where type = ? ",
				MessageType.Site);
		model.addAttribute("list", list);
		return "message/site/siteTemplates";
	}

	@RequestMapping(value = "custom", method = RequestMethod.GET)
	public String custom() {
		return "message/site/siteCustom";
	}

	/**
	 * 发送消息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "sendMsg", method = RequestMethod.POST)
	public String sendMsg() throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		String userId = ServletRequestUtils.getStringParameter(request, "userId", "") ;
		String title = ServletRequestUtils.getStringParameter(request, "title", "") ;
		String companyId = ServletRequestUtils.getStringParameter(request, "companyId", "") ;
		String content = ServletRequestUtils.getStringParameter(request, "content", "") ;
		Boolean userType = ServletRequestUtils.getBooleanParameter(request, "userType", true) ;
		String messageContentType = ServletRequestUtils.getStringParameter(request, "messageContentType", "") ;
		MessageContentType mt = Enum.valueOf(MessageContentType.class, messageContentType) ;
		LoginUser loginUser = UserSession.getLoginUser(request);
		
		MessageSite messageSite = new MessageSite();
		messageSite.setUserType(userType);
		messageSite.setSendTo(userId.toString());
		if (!userType) {
			messageSite.setSendTo(companyId);
		}
		messageSite.setTitle(title);
		messageSite.setSendType(SendType.SEND_MESSAGE);
		messageSite.setContent(content);
		messageSite.setSendDate(new Date());
		messageSite.setBeFrom(loginUser.getUsername());
		messageSite.setMessageContentType(mt);
		messageSiteService.save(messageSite);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "发送保存站内消息:"+messageSite.getId().toString());
		return ResultUtil.getSuccessMsg();
	}

	@RequestMapping(value = "sendBox", method = RequestMethod.GET)
	public String sendBox(Model model, HttpServletRequest request, Boolean msgType, String title, String startTime,
			String endTime) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<MessageSite> messageSitePage = new Page<>();
		messageSitePage.setPageNo(pageNo);
		List<Object> params = new ArrayList<>();
		String hql = " from MessageSite where 1 = 1";
		if (msgType != null) {
			if (msgType) {
				hql += " and sendType is  null";
			} else {
				hql += " and sendType is not null";
			}
		}
		if (!StringUtils.isEmpty(title)) {
			hql += " and title like ? ";
			params.add( "%"+title+"%");
			model.addAttribute("title",title);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql = hql + " and sendDate >= ? ";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and sendDate < ? ";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}

		hql += "  order by id desc ";
		messageSitePage = messageSiteService.page(messageSitePage, hql, params.toArray());
		Map<MessageSite, Company> companyMap = new HashMap<>();
		for (MessageSite messageSite : messageSitePage.getResult()) {
			if (!messageSite.getUserType()) {
				Company company = companyService.get(messageSite.getSendTo());
				companyMap.put(messageSite, company);
			}
		}
		model.addAttribute("companyMap", companyMap);
		model.addAttribute("messageSitePage", messageSitePage);
		return "message/site/siteSendBox";
	}

	@RequestMapping(value = "siteDetail", method = RequestMethod.GET)
	public String interiorDetail(Integer id, Model model) {
		MessageSite messageSite = messageSiteService.get(id);
		model.addAttribute("messageSite", messageSite);
		return "message/site/siteDetail";
	}

}
