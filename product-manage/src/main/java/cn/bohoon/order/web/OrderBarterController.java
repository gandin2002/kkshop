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
import org.springframework.web.bind.annotation.ResponseBody;

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
import cn.bohoon.order.domain.OrderCheckState;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.entity.AfterMarketOrder;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderBarter;
import cn.bohoon.order.service.AfterMarketOrderService;
import cn.bohoon.order.service.OrderBarterService;
import cn.bohoon.order.service.OrderLogService;
import cn.bohoon.order.service.OrderService;

/**
 * 换货单管理
 * @author dujianqiao
 *
 */
@Controller
@RequestMapping(value = "orderBarter")
public class OrderBarterController {

	@Autowired
	OrderService orderService;
	@Autowired
	CompanyService companyService;
	@Autowired
	OrderBarterService orderBarterService;
	@Autowired
	AfterMarketOrderService afterMarketOrderService ;
	@Autowired
	OrderLogService orderLogService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	/**
	 * 换货单列表信息
	 * 
	 * @param request
	 * @param model
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, OrderBarter search) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");

		Page<OrderBarter> page = new Page<OrderBarter>();
		page.setPageNo(pageNo);
		String hql = "from OrderBarter t where 1 =1 ";
		List<Object> params = new ArrayList<>();
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
		if (!StringUtils.isEmpty(search.getReceiver())) {
			hql += " and t.receiver like ?";
			params.add('%' + search.getReceiver() + '%');
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

		page = orderBarterService.page(page, hql, params.toArray());

		model.addAttribute("searchModel", search);
		model.addAttribute("pageOrderBarter", page);
		return "orderBarter/orderBarterList";
	}

	/**
	 * 换货单编辑
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		OrderBarter orderBarter = orderBarterService.get(id);
		Order order = orderService.get(orderBarter.getOrderId());

		Company company = companyService.get(order.getMemberId());

		model.addAttribute("order", order);
		model.addAttribute("company", company);
		model.addAttribute("item", orderBarter);
		return "orderBarter/orderBarter";
	}

	@ResponseBody
	@RequestMapping(value = "finishOrder", method = RequestMethod.GET)
	public String finishOrder(HttpServletRequest request, String id) throws Exception {
		OrderBarter order = orderBarterService.get(id);

		order.setOrderState(OrderState.TRADE_FINISHED);
		orderBarterService.save(order);
		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "将换货订单状态置为完成！";
		OrderCheckState ocs = OrderCheckState.CONFIRM_FINISH;
		orderLogService.save(user.getUsername(),order,ocs, note);
		String amOrderId = order.getAmOrder() ;
		if(!StringUtils.isEmpty(amOrderId)) {
			AfterMarketOrder ao = afterMarketOrderService.get(amOrderId) ;
			if( null != ao) {
				ao.setOrderState(OrderState.EXCHANGE_SENDED_GOODS);
				afterMarketOrderService.exChangeSendGoods(ao,user.getUsername()) ;
			}
		}
			
		
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 编辑发货单信息
	 *
	 * @param orderBarter
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(OrderBarter orderBarter,HttpServletRequest request) throws Exception {
		OrderBarter entity = orderBarterService.get(orderBarter.getId()) ;
		entity.setNote(orderBarter.getNote());
		orderBarterService.save(entity);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "编辑换货单信息:"+entity.getId());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 删除发货单信息
	 *
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, String id) throws Exception {
		orderBarterService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除换货单信息:id"+id);
		return ResultUtil.getSuccessMsg();
	}

}