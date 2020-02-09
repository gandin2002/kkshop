package cn.bohoon.order.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
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

import com.alibaba.fastjson.JSONObject;
import com.alipay.config.Alipay;
import com.alipay.entity.AliPayDo;
import com.wxpay.entity.WechatPay;
import com.wxpay.util.GenerateWxQrcode;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.message.aop.MessageNodeNotified;
import cn.bohoon.message.cache.MsgTemplateCache;
import cn.bohoon.message.domain.MessageContentType;
import cn.bohoon.message.domain.SendType;
import cn.bohoon.message.entity.MessageSite;
import cn.bohoon.message.service.MessageSiteService;
import cn.bohoon.order.domain.OperateIdentity;
import cn.bohoon.order.domain.OrderActivity;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.ReOrderType;
import cn.bohoon.order.domain.SettleWay;
import cn.bohoon.order.entity.AfterMarketOrder;
import cn.bohoon.order.entity.AmOrderItem;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderBarter;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderRefund;
import cn.bohoon.order.entity.OrderRepaire;
import cn.bohoon.order.service.AfterMarketOrderService;
import cn.bohoon.order.service.AmOrderItemService;
import cn.bohoon.order.service.AmOrderLogService;
import cn.bohoon.order.service.OrderBarterService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderRefundService;
import cn.bohoon.order.service.OrderRepaireService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.payment.domain.AlipayVo;
import cn.bohoon.payment.domain.WechatPayVo;
import cn.bohoon.payment.entity.PaymentType;
import cn.bohoon.payment.service.PaymentTypeService;
import cn.bohoon.product.entity.SkuWare;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareService;
import cn.bohoon.stock.entity.WareHouse;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.userInfo.domain.ScoreType;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.ScoreLogService;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ConvertUtils;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.util.VelocityHtmlUtils;

@Controller
@RequestMapping(value = "afterMarketOrder")
public class AfterMarketOrderController {
	
	@Autowired
	OrderService orderService;
	@Autowired
	SkuWareService skuWareService ;
	@Autowired
	CompanyService companyService ;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	WareHouseService wareHouseService ;
	@Autowired
	OrderBarterService orderBarterService;
	@Autowired
	OrderRepaireService orderRepaireService;
	@Autowired
	AmOrderLogService amOrderLogService;
	@Autowired
	AmOrderItemService amOrderItemService;
	@Autowired
	PaymentTypeService PaymentTypeService;
	@Autowired
	OrderRefundService orderRefundService ;
	@Autowired
	AfterMarketOrderService afterMarketOrderService;
	@Autowired
	MessageSiteService messageSiteService ;
	
	@Autowired
	UserInfoService 	userInfoService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
    @Autowired
    ScoreLogService scoreLogService;
    @Autowired
    SysParamService sysParamService;
	Logger logger = LoggerFactory.getLogger(AfterMarketOrderController.class) ;

	/**
	 * 售后订单列表
	 * 
	 * @param request
	 * @param model
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model, AfterMarketOrder search) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		Page<AfterMarketOrder> page = new Page<AfterMarketOrder>();
		page.setPageNo(pageNo);
		String hql = "from AfterMarketOrder t where 1 =1 ";
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
		if (!StringUtils.isEmpty(search.getCompany())) {
			hql += " and t.company like ?";
			params.add('%' + search.getCompany() + '%');
		}
		if (!StringUtils.isEmpty(search.getReOrderType())) {
			hql += " and t.reOrderType=?";
			params.add(search.getReOrderType());
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
		page = afterMarketOrderService.page(page, hql, params.toArray());

		
		model.addAttribute("pageOrder", page);
		model.addAttribute("searchModel", search);
		return "aftermarketOrder/aftermarketOrderList";
	}

	/**
	 * 帮助申请售后信息
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/makeApply", method = RequestMethod.GET)
	public String makeApply(Model model, String id) {
		Order order = orderService.get(id);
		List<OrderItem> orderItemList = orderItemService.list(" from OrderItem where orderId = ? ", order.getId());
		model.addAttribute("order", order);
		model.addAttribute("orderItemList", orderItemList);
		return "aftermarketOrder/markApply";
	}
	
	@Autowired
	MessageNodeNotified messageNodeNotified;

	/**
	 * 提交售后信息
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/makeApply", method = RequestMethod.POST)
	@ResponseBody
	public String makeApplyPost(HttpServletRequest request, AfterMarketOrder aOrder, String itemId) throws Exception {

		if (null == itemId || "".equals(itemId)) {
			return ResultUtil.getError("请选择需要售后的货品！");
		}

		LoginUser loginUser = UserSession.getLoginUser(request);

		Order order = orderService.get(aOrder.getOrderId());
		aOrder.setUserId(order.getUserId());
		aOrder.setUsername(order.getUsername());
		aOrder.setMemberId(order.getMemberId());
		aOrder.setMemberErpCode(order.getMemberErpCode());
		aOrder.setCompany(order.getCompany());

		String id = IDUtil.getInstance().getIdByDb(afterMarketOrderService, AfterMarketOrder.class, "AM", "id");
		aOrder.setId(id);

		aOrder.setOrderState(OrderState.WAIT_RETURN_GOODS);
		if (aOrder.getReOrderType().equals(ReOrderType.REFUND)) {
			aOrder.setOrderState(OrderState.TREASURER_AUDIT);
		}

		afterMarketOrderService.createOrder(aOrder, itemId);
		String note = "后台管理员协助申请售后服务！";
		amOrderLogService.save(loginUser.getUsername(), aOrder, OrderActivity.APPLY_SERVICE, OperateIdentity.MANAGER, note);
		
		UserInfo userInfo = userInfoService.get(aOrder.getUserId());
		JSONObject jt= new JSONObject();
		jt.put("msghead", "售后进度通知！");
		jt.put("msgCode","恭喜您，售后订单已创建成功！");
		messageNodeNotified.sendMessage(SendType.AFTER_SERVICE,userInfo, userInfo,aOrder,order, jt);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增售后订单信息:id"+aOrder.getId());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 显示售后信息
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String show(Model model, String id) {
		AfterMarketOrder amOrder = afterMarketOrderService.get(id);
		model.addAttribute("amOrder", amOrder);
		return "aftermarketOrder/aftermarketOrderDetail";
	}

	/**
	 * 审核拒绝
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/refuse", method = RequestMethod.GET)
	@ResponseBody
	public String refuse(HttpServletRequest request, String id) throws Exception {
		AfterMarketOrder amOrder = afterMarketOrderService.get(id);
		amOrder.setOrderState(OrderState.REFUSE_AUDIT);

		LoginUser loginUser = UserSession.getLoginUser(request);
		String managerName = null != loginUser ? loginUser.getUsername() : "";
		afterMarketOrderService.auditRefuse(amOrder, managerName);
		
		JSONObject jt= new JSONObject();
		jt.put("msghead", "售后进度通知！");
		jt.put("msgCode","不好意思，售后订单被拒绝！");
		UserInfo userInfo = userInfoService.get(amOrder.getUserId());
		messageNodeNotified.sendMessage(SendType.AFTER_SERVICE,userInfo, userInfo,amOrder,jt);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "售后订单信息审核拒绝:id"+id);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 审核通过
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pass", method = RequestMethod.GET)
	@ResponseBody
	public String pass(HttpServletRequest request, String id) throws Exception {
		AfterMarketOrder amOrder = afterMarketOrderService.get(id);
		amOrder.setOrderState(OrderState.WAIT_RETURN_GOODS);
		if (amOrder.getReOrderType().equals(ReOrderType.REFUND)) {
			amOrder.setOrderState(OrderState.TREASURER_AUDIT);
		}
		LoginUser loginUser = UserSession.getLoginUser(request);
		String managerName = null != loginUser ? loginUser.getUsername() : "";
		afterMarketOrderService.auditPass(amOrder, managerName);
		UserInfo userInfo = userInfoService.get(amOrder.getUserId());
		JSONObject jt= new JSONObject();
		jt.put("msghead", "售后进度通知！");
		jt.put("msgCode","恭喜您，售后订单审核通过！");
		messageNodeNotified.sendMessage(SendType.AFTER_SERVICE,userInfo, userInfo,amOrder, jt );
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "售后订单信息审核通过:id"+id);
		
		/**
		 * 自己退积分
		 */
		List<OrderItem> orderItemList = orderItemService.list(" from OrderItem where orderId = ? ", amOrder.getOrderId());
		Integer downScore = 0;
		for (OrderItem oit : orderItemList) {
			if (null != oit.getScore()) {
				downScore += oit.getQuantity() * oit.getScore();
			}
		}
		scoreLogService.updateScore(userInfo.getId(),downScore,ScoreType.REFUND_OR_SALES);
		/**
		 * 推荐人退积分
		 */
		if(downScore>0){
		if(!StringUtils.isEmpty(userInfo.getCommendFriendId())){
		BigDecimal refundFee=amOrder.getReFundFee();
		SysParam sysparam=sysParamService.findParam("SHOP_RETURN_SCORE",SysParamType.SCORE_PARAM);
		BigDecimal sysValue= new BigDecimal(sysparam.getValue());
		int score=refundFee.multiply(sysValue).intValue();
		UserInfo commendUser=userInfoService.select("from UserInfo where commendFriendId="+userInfo.getId());
		scoreLogService.updateScore(commendUser.getId(), score, ScoreType.COMMEND_REFUND_OR_SALES);
		}
		}
		
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 财务审核通过
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/financePass", method = RequestMethod.GET)
	@ResponseBody
	public String financePass(HttpServletRequest request, String id) throws Exception {
		AfterMarketOrder amOrder = afterMarketOrderService.get(id);
		amOrder.setOrderState(OrderState.BEING_REFUND);
		LoginUser loginUser = UserSession.getLoginUser(request);
		String managerName = null != loginUser ? loginUser.getUsername() : "";
		afterMarketOrderService.financePass(amOrder, managerName);
 
		UserInfo userInfo = userInfoService.get(amOrder.getUserId());
		JSONObject jt= new JSONObject();
		jt.put("msghead", "售后进度通知！");
		jt.put("msgCode","恭喜您，售后订单审核通过！");
		messageNodeNotified.sendMessage(SendType.AFTER_SERVICE,userInfo, userInfo,amOrder,jt );
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "售后订单信息财务审核通过:id"+id);
		return ResultUtil.getSuccessMsg();
	}
	
	
	/**
	 * 财务审核通过
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/reFund", method = RequestMethod.GET)
	public String reFund(Model model, String id) throws Exception {
		AfterMarketOrder amOrder = afterMarketOrderService.get(id);
		model.addAttribute("amOrder", amOrder) ;
		return "aftermarketOrder/surePassword";
	}
	
	/**
	 * 退款 售后完成
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/beingRefund", method = RequestMethod.POST)
	@ResponseBody
	public String beingRefund(HttpServletRequest request, String id) throws Exception {
		String password = ServletRequestUtils.getStringParameter(request, "password", "");
		AfterMarketOrder amOrder = afterMarketOrderService.get(id);
		Order order = orderService.get(amOrder.getOrderId());
		BigDecimal refundFee = amOrder.getReFundFee() ;
		logger.info("售后退款金额 ==================="+refundFee);
		boolean flag = false;
		/*******************************退款************************************/
		if (order.getSettleWay().getName().equals(SettleWay.WECHAT.getName())) {
			PaymentType pay = PaymentTypeService.selectByCode("weixin");
			if(!password.equals(pay.getReFundPwd())) {
				return ResultUtil.getError("退款密码输入错误！") ;
			}
			if (pay != null) {
				WechatPayVo wePayVo = pay.getWxConfigMap();
				WechatPay wp = new WechatPay();
				wp.setAppId(wePayVo.getAppId());
				wp.setApiKey(wePayVo.getApiKey());
				wp.setMchId(wePayVo.getMchId());
				wp.setUniPayUrl(wePayVo.getUniPayUrl());
				wp.setNotify_url(wePayVo.getNotifyUrl());
				wp.setTransaction_id(order.getTransactionId());
				wp.setOut_trade_no(order.getId());
				wp.setTotal(order.getTotal());
				wp.setRefund_fee(refundFee);
				wp.setRefundUrl(wePayVo.getRefundUrl());
				Map<String, String> result = GenerateWxQrcode.reFund(wp);
				if (result.get("result_code").equalsIgnoreCase("SUCCESS")) {
					flag = true;
				}
			}

		}
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
				  ado.setRefund_fee(refundFee.toEngineeringString());
				  ado.setTradeNo(order.getTransactionId());
				  ado.setTotal_fee(order.getTotal().toString());
				  Map<String, String> result = Alipay.reFund(ado) ;
				if (result.get("result_code").equalsIgnoreCase("SUCCESS")) {
					flag = true;
				}
			}

		}
		if (order.getSettleWay().equals(SettleWay.CREDIT)) {
			PaymentType pay = PaymentTypeService.selectByCode("credit");
			if(null != pay && !password.equals(pay.getReFundPwd())) {
				return ResultUtil.getError("退款密码输入错误！") ;
			}
			String memberId = order.getMemberId() ;
			Company company = companyService.get(memberId) ;
			company.setCreditOver(company.getCreditOver().add(refundFee));
			companyService.save(company);
			flag = true ;
			
		}
		if (flag) {
			amOrder.setOrderState(OrderState.REFUND_FINISH);
			LoginUser loginUser = UserSession.getLoginUser(request);
			String managerName = null != loginUser ? loginUser.getUsername() : "";
			afterMarketOrderService.beingRefund(amOrder, managerName);
			OrderRefund orderRefund = new OrderRefund(order,amOrder) ;
			String orderRefundId = IDUtil.getInstance().getId("RR") ;
			orderRefund.setId(orderRefundId);
			orderRefund.setOperator(managerName);
			orderRefundService.save(orderRefund);
			
		}
		 
		UserInfo userInfo = userInfoService.get(amOrder.getUserId());
		JSONObject jt= new JSONObject();
		jt.put("msghead", "售后进度通知！");
		jt.put("msgCode","恭喜您，售后订单退款成功！");
		messageNodeNotified.sendMessage(SendType.AFTER_SERVICE,userInfo, userInfo,amOrder,jt);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "退款售后完成:id"+id);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 退货 收货动作
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/reviceGoods", method = RequestMethod.GET)
	@ResponseBody
	public String reviceGoods(HttpServletRequest request, String id) throws Exception {
		AfterMarketOrder amOrder = afterMarketOrderService.get(id);
		Integer wareHouseId = amOrder.getWareHouseId() ;
		// 退货退款
		if (amOrder.getReOrderType().equals(ReOrderType.REFUNDANDGOODS)) {
			amOrder.setOrderState(OrderState.TREASURER_AUDIT);
			String queryString = " from AmOrderItem oit where  oit.amOrderId=?";
			List<AmOrderItem> aois = amOrderItemService.list(queryString, amOrder.getId());
			if(null != wareHouseId) {
				for(AmOrderItem aoi : aois) {
					Integer skuId = ConvertUtils.parseInteger(aoi.getSkuId()) ;
					String skuWareHql = " from SkuWare where skuId=? and wareHouseId=?" ;
					SkuWare skuWare = skuWareService.select(skuWareHql, skuId,wareHouseId) ;
					if(null == skuWare) {
						WareHouse wareHouse = wareHouseService.get(wareHouseId) ;
						skuWare = new  SkuWare();
						skuWare.setSkuId(skuId);
						skuWare.setWareHouseId(wareHouseId);
						skuWare.setWareHouseName(null != wareHouse ? wareHouse.getCorporateName(): "");
						skuWare.setQuantity(aoi.getQuantity());
						
					} else {
						Integer qt = aoi.getQuantity()+ skuWare.getQuantity() ;
						skuWare.setQuantity(qt);
					}
					skuWareService.save(skuWare);
					String jpaSql = " update Sku set inventory=inventory+? where id=?" ;
					skuWareService.execute(jpaSql, aoi.getQuantity(),skuId) ;
				}
			}
		}
		// 换货
		if (amOrder.getReOrderType().equals(ReOrderType.CHANGEGOODS)) {
			amOrder.setOrderState(OrderState.EXCHANGE_GOODS);
			// 生成换货单信息
			String queryString = " from AmOrderItem oit where  oit.amOrderId=?";
			List<AmOrderItem> aois = amOrderItemService.list(queryString, amOrder.getId());
			Order order = orderService.get(amOrder.getOrderId());
			OrderBarter orderBarter = new OrderBarter(order);
			orderBarter.setAmOrder(amOrder.getId());
			String oid = IDUtil.getInstance().getIdByDb(orderBarterService, OrderBarter.class, "OB", "id");
			orderBarter.setId(oid);
			orderBarterService.createBarterOrder(orderBarter, aois);
			if(null != wareHouseId) {
				for(AmOrderItem aoi : aois) {
					Integer skuId = ConvertUtils.parseInteger(aoi.getSkuId()) ;
					String skuWareHql = " from SkuWare where skuId=? and wareHouseId=?" ;
					SkuWare skuWare = skuWareService.select(skuWareHql, skuId,wareHouseId) ;
					if(null == skuWare) {
						WareHouse wareHouse = wareHouseService.get(wareHouseId) ;
						skuWare = new  SkuWare();
						skuWare.setSkuId(skuId);
						skuWare.setWareHouseId(wareHouseId);
						skuWare.setWareHouseName(null != wareHouse ? wareHouse.getCorporateName(): "");
						skuWare.setQuantity(aoi.getQuantity());
						
					} else {
						Integer qt = aoi.getQuantity()+ skuWare.getQuantity() ;
						skuWare.setQuantity(qt);
					}
					skuWareService.save(skuWare);
					String jpaSql = " update Sku set inventory=inventory+? where id=?" ;
					skuWareService.execute(jpaSql, aoi.getQuantity(),skuId) ;
				}
			}
		}
		// 维修
		if (amOrder.getReOrderType().equals(ReOrderType.IN_REPAIR)) {
			amOrder.setOrderState(OrderState.WAIT_REAPIRING);
			String queryString = " from AmOrderItem oit where  oit.amOrderId=?";
			List<AmOrderItem> aois = amOrderItemService.list(queryString, amOrder.getId());
			Order order = orderService.get(amOrder.getOrderId());
			OrderRepaire orderRepaire = new OrderRepaire(order);
			orderRepaire.setAmOrder(amOrder.getId());
			String oid = IDUtil.getInstance().getIdByDb(orderRepaireService, OrderBarter.class, "WX", "id");
			orderRepaire.setId(oid);
			orderRepaireService.createRepaireOrder(orderRepaire, aois);
		}
		LoginUser loginUser = UserSession.getLoginUser(request);
		String managerName = null != loginUser ? loginUser.getUsername() : "";
		afterMarketOrderService.reviceGoods(amOrder, managerName);
		
		UserInfo userInfo = userInfoService.get(amOrder.getUserId());
		JSONObject jt= new JSONObject();
		jt.put("msghead", "售后进度通知！");
		jt.put("msgCode","恭喜您，售后订单审核通过！");
		messageNodeNotified.sendMessage(SendType.AFTER_SERVICE,userInfo, userInfo,amOrder, jt );
		return ResultUtil.getSuccessMsg();
	}

}
