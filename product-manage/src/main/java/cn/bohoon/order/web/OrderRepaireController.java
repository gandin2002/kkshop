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
import cn.bohoon.order.entity.OrderRepaire;
import cn.bohoon.order.service.AfterMarketOrderService;
import cn.bohoon.order.service.OrderLogService;
import cn.bohoon.order.service.OrderRepaireService;
import cn.bohoon.order.service.OrderService;

@Controller
@RequestMapping(value = "orderRepaire")
public class OrderRepaireController {

	@Autowired
	OrderService orderService;
	@Autowired
	CompanyService companyService;
	@Autowired
	OrderRepaireService orderRepaireService;
	@Autowired
	AfterMarketOrderService afterMarketOrderService ;
	@Autowired
	OrderLogService orderLogService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, OrderRepaire search) throws Exception {
	
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");

		Page<OrderRepaire> page = new Page<OrderRepaire>();
		page.setPageNo(pageNo);
		String hql = "from OrderRepaire t where 1 =1 ";
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

		page = orderRepaireService.page(page, hql, params.toArray());

		model.addAttribute("searchModel", search);
		model.addAttribute("pageOrderRepaire", page);
		return "orderRepaire/orderRepaireList";
	}

	/**
	 * 维修详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
	
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		
		OrderRepaire orderRepaire = orderRepaireService.get(id);
		Order order = orderService.get(orderRepaire.getOrderId());
		//System.out.println("维修详情订单id多少"+orderRepaire.getOrderId()+"id多少"+id);
		Company company = companyService.get(order.getMemberId());

		model.addAttribute("order", order);
		model.addAttribute("company", company);
		model.addAttribute("item", orderRepaire);
		return "orderRepaire/orderRepaire";
	}
	
	/**
	 * 维修完成
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="finishRepair",method = RequestMethod.GET)
	public String finishRepair(HttpServletRequest request, String id) throws Exception {
		OrderRepaire order = orderRepaireService.get(id);

		order.setOrderState(OrderState.WAIT_DELIVERY);
		orderRepaireService.save(order);
		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "将维修订单状态置为维修完成，待发货！";
		OrderCheckState ocs = OrderCheckState.CONFIRM_DELIVERY;
		orderLogService.save(user.getUsername(),order,ocs, note);
		return ResultUtil.getSuccessMsg();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "finishOrder", method = RequestMethod.GET)
	public String finishOrder(HttpServletRequest request, String id) throws Exception {
		OrderRepaire order = orderRepaireService.get(id);

		order.setOrderState(OrderState.TRADE_FINISHED);
		orderRepaireService.save(order);
		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "将维修订单状态置为完成！";
		OrderCheckState ocs = OrderCheckState.CONFIRM_FINISH;
		orderLogService.save(user.getUsername(),order,ocs, note);
		String amOrderId = order.getAmOrder() ;
		if(!StringUtils.isEmpty(amOrderId)) {
			AfterMarketOrder ao = afterMarketOrderService.get(amOrderId) ;
			if( null != ao) {
				ao.setOrderState(OrderState.REPAIRE_SENDED_GOODS);
				afterMarketOrderService.exChangeSendGoods(ao,user.getUsername()) ;
			}
		}
			
		
		return ResultUtil.getSuccessMsg();
	}

	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(OrderRepaire orderRepaire,HttpServletRequest request) throws Exception {
		orderRepaireService.save(orderRepaire);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改维修订单");
		return ResultUtil.getSuccessMsg();
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, String id) throws Exception {
		orderRepaireService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除维修订单:id"+id);
		return ResultUtil.getSuccessMsg();
	}

}