package cn.bohoon.order.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.order.domain.OrderType;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderInvoice;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.service.OrderInvoiceService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;

@Controller
@RequestMapping(value = "scoreOrder")
public class ScoreOrderController {

	@Autowired
	OrderService orderService;
	@Autowired
	CompanyService companyService ;
	@Autowired
	OrderInvoiceService orderInvoiceService ;
	@Autowired
	UserInfoService userInfoService ;
	@Autowired
	OrderItemService orderItemService ;
	
	/**
	 * 订单列表
	 *
	 * @param request
	 * @param model
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, Order search) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		Page<Order> page = new Page<Order>();
		page.setPageNo(pageNo);
		String hql = "from Order t where t.orderType =? ";
		List<Object> params = new ArrayList<Object>();
		params.add(OrderType.SCOREEXCHANGE) ;
		if (!StringUtils.isEmpty(search.getOrderState())) {
			hql += " and t.orderState=?";
			params.add(search.getOrderState());
		}
		if (!StringUtils.isEmpty(search.getId())) {
			hql += " and t.id like ?";
			params.add('%' + search.getId() + '%');
		}
		if (!StringUtils.isEmpty(search.getUsername())) {
			hql += " and t.username like ?";
			params.add('%' + search.getUsername() + '%');
		}
		if (!StringUtils.isEmpty(search.getSettleWay())) {
			hql += " and t.settleWay=?";
			params.add(search.getSettleWay());
		}

		if (!StringUtils.isEmpty(startTime)) {
			hql = hql + " and t.createDate >= ?";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and t.createDate < ?";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}

		hql += " order by t.createDate desc ";
		page = orderService.page(page, hql, params.toArray());

		model.addAttribute("pageOrder", page);
		model.addAttribute("searchModel", search);
		return "scoreOrder/orderList";
	}
	
	/**
	 * 详情，编辑页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) throws Exception {
		String orderId = ServletRequestUtils.getStringParameter(request, "orderId");
		Order order = orderService.get(orderId);

		Company company = companyService.get(order.getMemberId());

		Integer invoiceId = order.getInvoiceId();
		if (invoiceId != null) {
			OrderInvoice invoice = orderInvoiceService.get(invoiceId);
			model.addAttribute("invoice", invoice);
		}
		UserInfo user = userInfoService.get(order.getUserId());

		List<OrderItem> orderItemList = orderItemService.list(" from OrderItem where orderId = ? ", order.getId());
		Integer score = 0;
		Integer buyNum = 0;
		for (OrderItem oit : orderItemList) {
			if (null != oit.getScore()) {
				score += oit.getSaleQuantity() * oit.getScore();
				buyNum += oit.getSaleQuantity();
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("item", order);
		model.addAttribute("score", score);
		model.addAttribute("buyNum", buyNum);
		model.addAttribute("company", company);
		return "scoreOrder/orderDetail";
	}
}
