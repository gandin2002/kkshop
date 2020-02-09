package cn.bohoon.interfaces.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.interfaces.entity.SyncDataLog;
import cn.bohoon.interfaces.service.SyncDataLogService;

@Controller
@RequestMapping("syncDataLog")
public class SyncDataLogController {

	Logger logger = LoggerFactory.getLogger(SyncDataLogController.class) ;

	@Autowired
	SyncDataLogService syncDataLogService ;
	
	/**
	 * 同步日志列表信息
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<SyncDataLog> logPage = new Page<SyncDataLog>();
		logPage.setPageNo(pageNo);
		String hql = "from SyncDataLog t where 1 =1 ";
		List<Object> params = new ArrayList<Object>();

		hql += " order by t.syncTime desc ";
		logPage = syncDataLogService.page(logPage, hql, params.toArray());

		model.addAttribute("logPage", logPage);
		return "syncRelation/sysDataLogList";
	}
	
	
	/**
	 * 同步日志详情
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "show", method = RequestMethod.GET)
	public String detail(Model model,Integer id) {
		SyncDataLog syncLog = syncDataLogService.get(id) ;
		model.addAttribute("syncLog", syncLog);
		return "syncRelation/sysDataLogDetail";
	}
	
	
	
	
}
