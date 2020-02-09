package cn.bohoon.order.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.order.entity.AmOrderLog;
import cn.bohoon.order.service.AmOrderLogService;

@Controller
@RequestMapping("amOrderLog")
public class AmOrderLogController {

	@Autowired
	AmOrderLogService amOrderLogService ;
	
	@RequestMapping("orderLogs")
	public String orderLogs(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String amOrderId = ServletRequestUtils.getStringParameter(request, "amOrderId", "");
		Page<AmOrderLog> page = new Page<AmOrderLog>();
		page.setPageNo(pageNo);
		String hql = " from AmOrderLog where amOrderId = ? order by createDate desc ";
		page = amOrderLogService.page(page, hql, amOrderId);
		model.addAttribute("pageOrderLog", page);
		model.addAttribute("amOrderId", amOrderId);
		return "aftermarketOrder/amOrderLog";
	}
}
