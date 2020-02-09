package cn.bohoon.order.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.config.Alipay;
import com.alipay.entity.AliPayDo;
import com.wxpay.entity.WechatPay;
import com.wxpay.util.GenerateWxQrcode;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.SettleWay;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderCancel;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderLog;
import cn.bohoon.order.entity.OrderReceipt;
import cn.bohoon.order.entity.OrderRefund;
import cn.bohoon.order.service.OrderCancelService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderLogService;
import cn.bohoon.order.service.OrderReceiptService;
import cn.bohoon.order.service.OrderRefundService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.payment.domain.AlipayVo;
import cn.bohoon.payment.domain.WechatPayVo;
import cn.bohoon.payment.entity.PaymentType;
import cn.bohoon.payment.service.PaymentTypeService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "cancelOrder")
public class CancelOrderController {

	@Autowired
	OrderService orderService;
	@Autowired
	CompanyService companyService ;
	@Autowired
	ProductService productService ;
	@Autowired
	OrderLogService orderLogService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	OrderCancelService orderCancelService ;
	@Autowired
	OrderReceiptService orderReceiptService;
	@Autowired
	OrderRefundService orderRefundService ;
	@Autowired
	PaymentTypeService PaymentTypeService ;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	
	Logger logger = LoggerFactory.getLogger(CancelOrderController.class) ;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, OrderCancel search) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		Page<OrderCancel> page = new Page<OrderCancel>();
		page.setPageNo(pageNo);
		String hql = "from OrderCancel t where 1 =1 ";
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
		page = orderCancelService.page(page, hql, params.toArray());

		model.addAttribute("pageOrder", page);
		model.addAttribute("searchModel", search);
		return "orderCancel/orderCancelList";
	}
	
	
	
	/**
	 * 详情，编辑页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) throws Exception {
		String id = ServletRequestUtils.getStringParameter(request, "id");
		OrderCancel orderCancel = orderCancelService.get(id);
		String orderId = orderCancel.getOrderId() ;
		Order order = orderService.get(orderId) ;
		List<OrderReceipt> list = orderReceiptService.list(" from OrderReceipt where orderId=?",orderId) ;
		if(list.size() > 0) {
			model.addAttribute("orderReceipt", list.get(0));
		}
		model.addAttribute("order", order);
		model.addAttribute("orderCancel", orderCancel);
		return "orderCancel/cancelDetail";
	}
	
	/**
	 * 订单货品清单
	 * @param request
	 * @param model
	 */
	@RequestMapping(value = "orderItems", method = RequestMethod.GET)
	public String orderItems(HttpServletRequest request, Model model) throws Exception {
		String id = ServletRequestUtils.getStringParameter(request, "id");
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		OrderCancel orderCancel = orderCancelService.get(id);
		String orderId = orderCancel.getOrderId() ;
		Page<OrderItem> page = new Page<OrderItem>(5);
		page.setPageNo(pageNo);

		page = orderItemService.page(page, "from OrderItem where orderId=?", orderId);
		Map<OrderItem,Object> productMap = new HashMap<>() ;
		for(OrderItem oi : page.getResult()) {
			Product product = productService.get(oi.getProductId()) ;
			productMap.put(oi,product);
		}
		model.addAttribute("pageOrderItem", page);
		model.addAttribute("productMap", productMap);
		model.addAttribute("orderCancel", orderCancel);
		return "orderCancel/cancelItems";
	}
	
	
	
	/**
	 * 订单货品清单
	 * @param request
	 * @param model
	 */
	@RequestMapping(value = "orderLogs", method = RequestMethod.GET)
	public String orderLogs(HttpServletRequest request, Model model) throws Exception {
		String id = ServletRequestUtils.getStringParameter(request, "id");
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		OrderCancel orderCancel = orderCancelService.get(id);
		Page<OrderLog> page = new Page<OrderLog>(5);
		page.setPageNo(pageNo);
		String hql = " from OrderLog where orderId = ? order by createDate desc ";
		page = orderLogService.page(page, hql, id);
		
		model.addAttribute("pageOrderLog", page);
		model.addAttribute("orderCancel", orderCancel);
		return "orderCancel/cancelOrderLogs";
	}
	
	/**
	 * 审核通过
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/pass",method=RequestMethod.GET)
	@ResponseBody
	public String pass(HttpServletRequest request,String id) throws Exception {
		OrderCancel order = orderCancelService.get(id) ;
		order.setOrderState(OrderState.TREASURER_AUDIT);
		LoginUser loginUser = UserSession.getLoginUser(request);
		String managerName = null != loginUser ? loginUser.getUsername() : "" ;
		orderCancelService.auditPass(order,managerName) ;
		return ResultUtil.getSuccessMsg() ;
	}
	
	
	/**
	 * 财务审核通过
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/reFundPass", method = RequestMethod.GET)
	public String reFundPass(Model model, String id) throws Exception {
		OrderCancel orderCancel = orderCancelService.get(id);
		model.addAttribute("orderCancel", orderCancel) ;
		return "orderCancel/surePassword";
	}
	
	
	/**
	 * 审核通过
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/refund",method=RequestMethod.POST)
	@ResponseBody
	public String refund(HttpServletRequest request,String id) throws Exception {
		String password = ServletRequestUtils.getStringParameter(request, "password", "");
		OrderCancel orderCancel = orderCancelService.get(id) ;
		Order order = orderService.get(orderCancel.getOrderId()) ;
		BigDecimal refundFee = order.getProductFee() ;
		logger.info("售后退款金额 ==================="+refundFee);
		boolean flag = false;
		/*******************************退款************************************/
		if(order.getSettleWay().getName().equals(SettleWay.WECHAT.getName())) {
			PaymentType pay = PaymentTypeService.selectByCode("weixin") ;
			if(pay != null ) {
				if(!password.equals(pay.getReFundPwd())) {
					return ResultUtil.getError("退款密码输入错误！") ;
				}
				WechatPayVo wePayVo = pay.getWxConfigMap() ;
				WechatPay wp = new WechatPay() ;
				wp.setAppId(wePayVo.getAppId());
				wp.setApiKey(wePayVo.getApiKey());
				wp.setMchId(wePayVo.getMchId());
				wp.setUniPayUrl(wePayVo.getUniPayUrl());
				wp.setNotify_url(wePayVo.getNotifyUrl());
				wp.setTransaction_id(order.getTransactionId());
				wp.setRefund_fee(order.getProductFee());
				wp.setOut_trade_no(order.getId());
				wp.setTotal(order.getTotal());
				wp.setRefundUrl(wePayVo.getRefundUrl());
				Map<String, String> result = GenerateWxQrcode.reFund(wp) ;
				if(result.get("result_code").equalsIgnoreCase("SUCCESS")) {
					flag = true ;
				}
			}
		
		}
		/****************************阿里支付退款*****************************************/
		if (order.getSettleWay().getName().equals(SettleWay.ALIPAY.getName())) {
			PaymentType pay = PaymentTypeService.selectByCode("alipay");
			if(!password.equals(pay.getReFundPwd())) {
				return ResultUtil.getError("退款密码输入错误！") ;
			}
			if (pay != null) {
				  AlipayVo alipayVo = pay.alipayVo() ;
				  AliPayDo ado = new AliPayDo() ;
				  ado.setPartner(alipayVo.getPartner());
				  ado.setPrivate_key(alipayVo.getPrivate_key());
				  ado.setPublic_key(alipayVo.getPublic_key());
				  ado.setNotify_url(alipayVo.getNotify_url());
				  ado.setReturn_url(alipayVo.getReturn_url());
				  ado.setOut_trade_no(order.getId());
				  ado.setRefund_fee(order.getProductFee().toEngineeringString());
				  ado.setTradeNo(order.getTransactionId());
				  ado.setTotal_fee(order.getTotal().toString());
				  Map<String, String> result = Alipay.reFund(ado) ;
				if (result.get("result_code").equalsIgnoreCase("SUCCESS")) {
					flag = true;
				}
			}

		}
		
		/****************************信用支付退款*************************************/
		if (order.getSettleWay().equals(SettleWay.CREDIT)) {
			PaymentType pay = PaymentTypeService.selectByCode("credit");
			if(null != pay && !password.equals(pay.getReFundPwd())) {
				return ResultUtil.getError("退款密码输入错误！") ;
			}
			String memberId = order.getMemberId() ;
			Company company = companyService.get(memberId) ;
			if(null != company ) {
				company.setCreditOver(company.getCreditOver().add(refundFee));
				companyService.save(company);
			}
			flag = true ;
			
		}
		
		
		if(flag) {
			orderCancel.setOrderState(OrderState.REFUND_AND_FINISH);
			LoginUser loginUser = UserSession.getLoginUser(request);
			String managerName = null != loginUser ? loginUser.getUsername() : "" ;
			orderCancelService.auditRefund(orderCancel,managerName) ;
			
			OrderRefund orderRefund = new OrderRefund(order,orderCancel) ;
			String orderRefundId = IDUtil.getInstance().getId("RR") ;
			orderRefund.setId(orderRefundId);
			orderRefund.setOperator(managerName);
			orderRefundService.save(orderRefund);
			
		}

 		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "取消订单通过:id"+id);
		return ResultUtil.getSuccessMsg() ;
	}
	
}
