package cn.bohoon.order.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderBarter;
import cn.bohoon.order.entity.OrderLog;
import cn.bohoon.order.entity.OrderRepaire;
import cn.bohoon.order.service.OrderBarterService;
import cn.bohoon.order.service.OrderLogService;
import cn.bohoon.order.service.OrderRepaireService;
import cn.bohoon.order.service.OrderService;

@Controller
@RequestMapping(value = "orderLog")
public class OrderLogController {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderLogService orderLogService;
	@Autowired
	OrderBarterService orderBarterService;
	@Autowired
	OrderRepaireService orderRepaireService;

	/**
	 * 购买订单日志
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderLogs", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String orderId = ServletRequestUtils.getStringParameter(request, "id", "");
		Page<OrderLog> page = new Page<OrderLog>(5);
		page.setPageNo(pageNo);
		String hql = " from OrderLog where orderId = ? order by createDate desc ";
		page = orderLogService.page(page, hql, orderId);
		model.addAttribute("pageOrderLog", page);

		Order order = orderService.get(orderId);
		model.addAttribute("item", order);

		return "orderLog/orderLogs";
	}

	/**
	 * 换货单日志
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderLogs2", method = RequestMethod.GET)
	public String list2(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String orderId = ServletRequestUtils.getStringParameter(request, "id", "");
		Page<OrderLog> page = new Page<OrderLog>(5);
		page.setPageNo(pageNo);
		String hql = " from OrderLog where orderId = ? order by createDate desc ";
		page = orderLogService.page(page, hql, orderId);
		model.addAttribute("pageOrderLog", page);

		OrderBarter order = orderBarterService.get(orderId);
		model.addAttribute("item", order);

		return "orderLog/orderLogs2";
	}
	
	
	/**
	 * 维修单日志
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderLogs3", method = RequestMethod.GET)
	public String list3(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		Page<OrderLog> page = new Page<OrderLog>(5);
		page.setPageNo(pageNo);
		String hql = " from OrderLog where orderId = ? order by createDate desc ";
		page = orderLogService.page(page, hql, id);
		model.addAttribute("pageOrderLog", page);

		OrderRepaire orderRepaire = orderRepaireService.get(id) ;
		model.addAttribute("orderRepaire", orderRepaire);

		return "orderLog/orderLogs3";
	}

	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		return "orderLog/orderLogAdd";
	}

	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, OrderLog orderLog) {
		orderLogService.save(orderLog);
		return ResultUtil.getSuccessMsg();
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		OrderLog orderLog = orderLogService.get(id);
		model.addAttribute("item", orderLog);
		return "orderLog/orderLogEdit";
	}

	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(OrderLog orderLog) {
		orderLogService.save(orderLog);
		return ResultUtil.getSuccessMsg();
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, Integer id) {
		orderLogService.delete(id);
		return ResultUtil.getSuccessMsg();
	}

}