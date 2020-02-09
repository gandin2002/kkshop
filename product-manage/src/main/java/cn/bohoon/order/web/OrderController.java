package cn.bohoon.order.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
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
import com.google.zxing.WriterException;
import com.qrcode.GenerateZxing;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.domain.TemplatePrintType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.entity.TemplatePrint;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.basicSetup.service.TemplatePrintService;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.express.entity.Express;
import cn.bohoon.express.entity.Logistics;
import cn.bohoon.express.service.ExpressService;
import cn.bohoon.express.service.LogisticsService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.message.aop.MessageNodeNotified;
import cn.bohoon.message.domain.MessageNodeType;
import cn.bohoon.message.domain.SendType;
import cn.bohoon.message.service.MessageSiteService;
import cn.bohoon.order.domain.CashWay;
import cn.bohoon.order.domain.OrderCheckState;
import cn.bohoon.order.domain.OrderDetailExcel;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.OrderType;
import cn.bohoon.order.domain.PayType;
import cn.bohoon.order.domain.SettleWay;
import cn.bohoon.order.domain.TranType;
import cn.bohoon.order.entity.GatheringCondition;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderCancel;
import cn.bohoon.order.entity.OrderCode;
import cn.bohoon.order.entity.OrderInvoice;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderLog;
import cn.bohoon.order.entity.OrderReceipt;
import cn.bohoon.order.entity.SendGoods;
import cn.bohoon.order.entity.ShoppingCart;
import cn.bohoon.order.service.GatheringConditionService;
import cn.bohoon.order.service.OrderCancelService;
import cn.bohoon.order.service.OrderCodeService;
import cn.bohoon.order.service.OrderInvoiceService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderLogService;
import cn.bohoon.order.service.OrderReceiptService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.order.service.SendGoodsService;
import cn.bohoon.page.service.PageNavigationService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.quotation.domain.QuotationState;
import cn.bohoon.quotation.entity.Quotation;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.quotation.service.QuotationItemService;
import cn.bohoon.quotation.service.QuotationService;
import cn.bohoon.stock.entity.WareHouse;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.userInfo.domain.ExpType;
import cn.bohoon.userInfo.domain.ScoreType;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.ExpLogService;
import cn.bohoon.userInfo.service.ScoreLogService;
import cn.bohoon.userInfo.service.ShippingInfoService;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.IDUtil;
//import cn.bohoon.main.system.entity.MallConfig;
//import cn.bohoon.main.system.service.MallConfigService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.util.SessionUtils;
import cn.bohoon.util.SyncDataUtils;
import cn.bohoon.util.VelocityHtmlUtils;
import cn.bohoon.wx.mp.entity.WXUserInfo;
import cn.bohoon.wx.mp.service.WXUserInfoService;

@Controller
@RequestMapping(value = "order")
public class OrderController {

	@Autowired
	SkuService skuService ;
	@Autowired
	OrderService orderService;
	@Autowired
	CompanyService companyService;
	@Autowired
	OrderLogService orderLogService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	SendGoodsService sendGoodsService;
	@Autowired
	OrderCancelService orderCancelService;
	@Autowired
	OrderReceiptService orderReceiptService;
	@Autowired
	OrderInvoiceService orderInvoiceService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ProductService productService;
	@Autowired
	TemplatePrintService templatePrintService;
	@Autowired
	OrderCodeService orderCodeService;
	@Autowired
	LogisticsService logisticsService;
	@Autowired
	ExpressService expressService;
	@Autowired
	WareHouseService wareHouseService;
	@Autowired
	PageNavigationService pageNavigationService;
	@Autowired
	ShippingInfoService shippingInfoService;
	@Autowired
    QuotationService quotationService;
    @Autowired
    QuotationItemService quotationItemService;
    @Autowired
	MessageSiteService messageSiteService ;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
    @Autowired
    ScoreLogService scoreLogService;
    @Autowired
    SysParamService sysParamService;
	@Autowired
	ExpLogService expLogService;
	String SKUMAP_SESSION = "add-session-sku-map" ;
	
	
	Logger logger = LoggerFactory.getLogger(OrderController.class) ;

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
		params.add(OrderType.SHOPPING) ;
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

		hql += " and t.orderState!=? order by t.createDate desc ";
		params.add(OrderState.WAIT_CONFIRM_SALES);
		page = orderService.page(page, hql, params.toArray());

		model.addAttribute("pageOrder", page);
		model.addAttribute("searchModel", search);
		return "order/orderList";
	}

	/**
	 * 去新增订单
	 *
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toAdd",method=RequestMethod.GET)
	public String toAdd(HttpServletRequest request, Model model) {
		cleanSkuMap();
		return "order/addOrder" ;
	}
	
	
	/**
	 * 选择SKU 信息
	 *
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="selectSku",method=RequestMethod.GET)
	public String selectSku(HttpServletRequest request, Model model) {
	    
		String cid = ServletRequestUtils.getStringParameter(request, "cid","") ;
		String name = ServletRequestUtils.getStringParameter(request, "name","") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String queryString = " select sku from Sku sku , Product p where p.id=sku.productId " ;
		queryString += " and p.flag = 1 and sku.flag=1 and sku.status=1 " ;
		Page<Sku> skuPage = new Page<Sku>();
		skuPage.setPageNo(pageNo);
		List<Object> params = new ArrayList<Object>();
		if(!StringUtils.isEmpty(cid)) {
			queryString += " and p.categoryId=?" ;
			params.add(cid) ;
			model.addAttribute("cid", cid) ;
		}
		if(!StringUtils.isEmpty(name)) {
			queryString += " and p.name like ? " ;
			params.add('%'+name+'%');
			model.addAttribute("name", name) ;
		}
		skuPage = skuService.page(skuPage, queryString, params.toArray());
		model.addAttribute("skuPage", skuPage);
		
		return "order/selectSku" ;
	}
	
	
	/**
	 * 添加购物车
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "addSkuCart", method = RequestMethod.GET)
	public String addSku(HttpServletRequest request ,Model model) throws Exception {
		Integer skuId = ServletRequestUtils.getIntParameter(request, "skuId", 0) ;
		Integer quantity = ServletRequestUtils.getIntParameter(request, "quantity", 0) ;
		Map<Integer,Integer> cartMap = (Map<Integer, Integer>) SessionUtils.getSession(SKUMAP_SESSION) ;
		if(StringUtils.isEmpty(cartMap)) {
			cartMap = new HashMap<Integer,Integer>() ;
		}
		
		if(cartMap.containsKey(skuId)) {
			quantity  = cartMap.get(skuId)+quantity ;
		} 
		cartMap.put(skuId, quantity) ;
		SessionUtils.addSession(SKUMAP_SESSION, cartMap);
		
		initCarts(cartMap,model) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "添加购物车");
		return "order/addSkuCart";
	}
	
	/**
	 * 删除购物车
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "delSkuCart", method = RequestMethod.GET)
	public String delSkuCart(HttpServletRequest request ,Model model) throws Exception {
		Integer skuId = ServletRequestUtils.getIntParameter(request, "skuId", 0) ;
		Map<Integer,Integer> cartMap = (Map<Integer, Integer>) SessionUtils.getSession(SKUMAP_SESSION) ;
		if(StringUtils.isEmpty(cartMap)) {
			cartMap = new HashMap<Integer,Integer>() ;
		}
		
		if(cartMap.containsKey(skuId)) {
			cartMap.remove(skuId) ;
		} 
		
		initCarts(cartMap,model) ;
		SessionUtils.addSession(SKUMAP_SESSION, cartMap);
		return "order/addSkuCart";
	}
	
	/**
	 * 删除购物车
	 * 
	 * @param request
	 * @param skuId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "delAllSkuCart", method = RequestMethod.GET)
	public String delAllSkuCart(HttpServletRequest request ,Model model) throws Exception {
		cleanSkuMap();
		Map<Integer,Integer> cartMap = new HashMap<Integer,Integer>() ;
		initCarts(cartMap,model) ;
		return "order/addSkuCart";
	}
	
	/**
	 * 清空购物
	 * 
	 */
	public void cleanSkuMap(){
		Map<Integer,Integer> cartMap = new HashMap<Integer,Integer>() ;
		SessionUtils.addSession(SKUMAP_SESSION, cartMap);
	}
	/**
	 * 初始化cart
	 * 
	 * @param cartMap
	 * @param model
	 */
	public void initCarts(Map<Integer,Integer> cartMap ,Model model) {
		if(!StringUtils.isEmpty(cartMap) && !cartMap.isEmpty()) {
			Map<Integer,Sku> cartSku = new HashMap<Integer,Sku>() ;
			List<Integer> skuIdKeys = new ArrayList<Integer>(cartMap.keySet()) ;
			for(Integer id : skuIdKeys)  {
				cartSku.put(id, skuService.get(id)) ;
			}
			model.addAttribute("cartSkus", cartSku.values());
			model.addAttribute("cartMap", cartMap);
		}
		String hql = " from Company where userId is not NULL and companySate='PASS' " ;
		//选择公司
		List<Company> companys = companyService.list(hql) ;
		model.addAttribute("companys", companys);
	}
	
	@Autowired
	GatheringConditionService gatheringConditionService;
	/**
	 * 后台代下单
	 * 
	 * @param request
	 * @param order
	 * @param skuId
	 * @param quantity
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("addNew")
	@ResponseBody
	public String addNew(HttpServletRequest request ,Order order,Integer[] skuId,Integer[] quantity) throws Exception {
		String shippingInfoId = ServletRequestUtils.getStringParameter(request, "shippingInfoId", "") ;
		if(StringUtils.isEmpty(shippingInfoId) || StringUtils.isEmpty(skuId) || skuId.length ==0 ) {
			return ResultUtil.getError("请将内容填写完整！") ;
		}
		ShippingInfo shippingInfo = shippingInfoService.get(shippingInfoId);
		String userId = !StringUtils.isEmpty(shippingInfo.getUserId()) ? shippingInfo.getUserId(): "" ;
		Company company = companyService.get(order.getMemberId()) ;
		UserInfo user = userInfoService.get(userId);
		if(null == user ) {
			user = userInfoService.get(company.getUserId()) ;
		}
		//----------------付款条款--------------
		 if(StringUtils.isEmpty(company.getApplyListTimeId())){
				 order.setGatheringConditionId(company.getApplyListTimeId());
		 }
		
		order.setUsername(user.getNickname());
		order.setUserId(user.getId());
		order.setCompany(company.getName());
		order.setTranType(TranType.EXPRESS);
//		String id = IDUtil.getInstance().getIdByDb(orderService, Order.class, "SC","id");
		String id = IDUtil.getInstance().getOrderIdByDb(orderService, Order.class) ;
		order.setId(id);
		order.setShippingInfo(JsonUtil.toJson(shippingInfo));
		order.setReceiver(shippingInfo.getReceiver());
		Date cancelDate = DateUtil.getNDayAfter(order.getCreateDate(), 5) ;
		order.setCancelDate(cancelDate);
		List<ShoppingCart> carts = new ArrayList<>() ;
		int len = skuId.length ;
		int totalNum = 0 ;
		for(int i=0;i<len;i++){
			ShoppingCart sc = new ShoppingCart(userId, skuId[i], quantity[i]) ;
			carts.add(sc) ;
			totalNum += quantity[i] ;
		}
		order.setTotalNum(totalNum); 
		orderService.createOrder(order, carts);
		SessionUtils.addSession(SKUMAP_SESSION, null);
		
		try {
			LoginUser operator = UserSession.getLoginUser(request);
			String note = operator.getUsername() + "代客户下单！";
			OrderCheckState ocs = OrderCheckState.CONFIRM_SUBMIT;
			orderLogService.save(operator.getUsername(), order, ocs, note);
		} catch (Exception e) {
			logger.info("写操作日志异常==============="+e.getMessage());
			logger.error("写操作日志异常==============="+e.getMessage());
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "后台代下单");
		return ResultUtil.getSuccessMsg(); 
	}
	
	
	/**
	 * 后台代询价
	 * 
	 * @param request
	 * @param order
	 * @param skuId
	 * @param quantity
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("addQuotation")
	@ResponseBody
	public String addQuotation(HttpServletRequest request ,Order order,Integer[] skuId,Integer[] quantity) throws Exception {
		String shippingInfoId = ServletRequestUtils.getStringParameter(request, "shippingInfoId", "") ;
		if(StringUtils.isEmpty(shippingInfoId) || StringUtils.isEmpty(skuId) || skuId.length ==0 ) {
			return ResultUtil.getError("请将内容填写完整！") ;
		}
		Company company = companyService.get(order.getMemberId()) ;
		String userId = company.getUserId() ; 
		UserInfo user = userInfoService.get(userId);
		if(null == user ) {
			user = userInfoService.get(company.getUserId()) ;
		}
		Quotation quotation = new Quotation();
    	quotation.setId(IDUtil.getInstance().getIdByDb(quotationService, Quotation.class, "BJ", "id"));
    	quotation.setUserInfoId(userId);
    	quotation.setQuotationState(QuotationState.WATI_QUOTATION);
    	quotation.setValidDate(DateUtil.getNDayAfter(new Date(), 1));
    	quotation.setCreateTime(new Date());
    	quotation.setCompanyId(company.getId());
    	quotation.setCompanyName(null != company? company.getName() : "" );
    	quotation.setProductSkuNum(skuId.length);
    	
    	BigDecimal  quotationSkuPrice = new BigDecimal(0.00);
    	
    	List<QuotationItem> itemList = new ArrayList<>();
    	for(int i=0,j=skuId.length;i<j;i++){
    		if(StringUtils.isEmpty(skuId[i])){
    			continue;
    		}
    		Sku sku = skuService.get(skuId[i]);
    		if(StringUtils.isEmpty(sku)){
    			continue;
    		}
    		QuotationItem quotationItem = new QuotationItem();
    		quotationItem.setQuotationId(quotation.getId());
    		quotationItem.setUserInfoId(quotation.getUserInfoId());
    		quotationItem.setQuotationState(QuotationState.WATI_QUOTATION);
    		quotationItem.setValidDate(DateUtil.getNDayAfter(new Date(), 1));
    		quotationItem.setSkuId(sku.getId());
    		quotationItem.setProductId(sku.getProductId()) ;
    		quotationItem.setQuantity(quantity[i]);
    		quotationItem.setQuotationSkuPrice(sku.getSkuPrice());
    		quotationItem.setQuotationPrice(quotationItem.getQuotationSkuPrice());
    		
    		quotationSkuPrice = quotationSkuPrice.add(quotationItem.getQuotationSkuPrice());
    		
    		itemList.add(quotationItem);
    	}
    	
    	quotation.setQuotationSkuPrice(quotationSkuPrice);
    	quotation.setQuotationPrice(quotationSkuPrice);
    	
    	quotationService.saveQuotation(quotation, itemList);
    	
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "后台代询价");
		return ResultUtil.getSuccessMsg(); 
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

		Integer invoiceId = order.getInvoiceId();
		if (invoiceId != null) {
			OrderInvoice invoice = orderInvoiceService.get(invoiceId);
			model.addAttribute("invoice", invoice);
		}
		UserInfo user = userInfoService.get(order.getUserId());

		List<OrderItem> orderItemList = orderItemService.list(" from OrderItem where orderId = ? ", order.getId());
		Integer score = 0;
		for (OrderItem oit : orderItemList) {
			if (null != oit.getScore()) {
				score += oit.getoAmountWithTax().intValue() * oit.getScore()/100;
			}
		}
		
		if(!StringUtils.isEmpty(order.getMemberId())){
			Company company = companyService.get(order.getMemberId());
			model.addAttribute("company", company);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("item", order);
		model.addAttribute("score", score);
		return "order/orderEdit";
	}
	
	@Autowired
	WXUserInfoService wXUserInfoService;
	
	@Autowired
	MessageNodeNotified messageNodeNotified;
	

	/**
	 * 确认订单
	 * 
	 * @param request
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "makeSure", method = RequestMethod.POST)
	public String makeSure(HttpServletRequest request, Order order) throws Exception {
		String orderId = order.getId();
		
		
		Order entity = orderService.get(orderId);
		
		entity.setOrderState(order.getOrderState());
		if (!StringUtils.isEmpty(order.getNote())) {
			entity.setNote(order.getNote());
		}
		if (!StringUtils.isEmpty(order.getConsignmentDate())) {
			String consignmentDate = order.getConsignmentDate();
			
			String  consignmentDateNum = consignmentDate.replace("-", "");
			String requirementDateNum = entity.getRequirementDate().replace("-","");
			
			orderItemService.execute(" update OrderItem set consignmentDate = "+consignmentDateNum+" ,RequirementDate = "+requirementDateNum+" where orderId =  '"+orderId+"'");
			entity.setConsignmentDate(consignmentDate);
			
		}
		orderService.save(entity);
		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "确认订单！";
		OrderCheckState ocs = OrderCheckState.CONFIRM_PASS;
		orderLogService.save(user.getUsername(), order, ocs, note);
		
		OrderLog orderLog = new OrderLog() ;
		orderLog.setUsername(user.getUsername());
		orderLog.setOrderCheckState(ocs);
		orderLog.setNote(note);
		orderLog.setCreateDate(new Date());
		
		UserInfo UserInfo = userInfoService.get(entity.getUserId());
		WXUserInfo source = wXUserInfoService.select(" from WXUserInfo where miniOpenid = ? ", UserInfo.getWechatOpenid());
		if(source != null){
			UserInfo.setWechatUnioid(source.getUnionid());
			UserInfo.setWechatMpOpenid(source.getOpenid());
			userInfoService.save(UserInfo);
			messageNodeNotified.sendMessage(MessageNodeType.ORDER_ORDER_REVIEW,source.getOpenid(),entity,UserInfo,orderLog); //发送消息
		}
		
		
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "确认订单");
	
		
		
		return ResultUtil.getSuccessMsg();
		
		
	}

	/**
	 * 保存日志信息
	 * 
	 * @param request
	 * @param model
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveNote", method = RequestMethod.POST)
	@ResponseBody
	public String saveNote(HttpServletRequest request, Model model, Order order) throws Exception {
		Order entity = orderService.get(order.getId());
		if (!StringUtils.isEmpty(order.getNote())) {
			entity.setNote(order.getNote());
		}
		if (!StringUtils.isEmpty(order.getConsignmentDate())) {
			entity.setConsignmentDate(order.getConsignmentDate());
		}
		if (!StringUtils.isEmpty(order.getNote()) || !StringUtils.isEmpty(order.getConsignmentDate())) {
			orderService.save(entity);
		}
		List<OrderItem> orderItems = orderItemService.list(" from OrderItem where orderId=?",order.getId()) ;
 
		UserInfo userInfo = userInfoService.get(order.getUserId());
		JSONObject jsonObject  = new JSONObject();
		jsonObject.put("msgCode", "恭喜您，订单修改成功！");
		jsonObject.put("msghead", "订单发货成功！");
		messageNodeNotified.sendMessage(SendType.ORDER_SHIPMENTS,userInfo, userInfo,orderItems,order,jsonObject);
		
		
		WXUserInfo source = wXUserInfoService.select(" from WXUserInfo where miniOpenid = ? ", userInfo.getWechatMpOpenid());
		if(!StringUtils.isEmpty(source)){
			logger.info("wxUserInfo========="+source);
			userInfo.setWechatUnioid(source.getUnionid());
			userInfo.setWechatMpOpenid(source.getOpenid());
			userInfoService.save(userInfo);
			messageNodeNotified.sendMessage(MessageNodeType.ORDER_DELIVER_GOODS,source.getOpenid(),userInfo,order); //发送消息
		}
		
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 人工调价
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "changePrice", method = RequestMethod.GET)
	public String changePriceGet(HttpServletRequest request, Model model) throws Exception {
		String id = ServletRequestUtils.getStringParameter(request, "id");
		Order order = orderService.get(id);
		model.addAttribute("item", order);
		return "order/changePrice";
	}

	/**
	 * 人工调价 保存
	 * 
	 * @param request
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "changePrice", method = RequestMethod.POST)
	@ResponseBody
	public String changePricePost(HttpServletRequest request, Order order) throws Exception {
		LoginUser user = UserSession.getLoginUser(request);

		Order entity = orderService.get(order.getId());

		String note = user.getUsername() + "对订单调价，调价前金额：" + entity.getTotal() + "，调价后金额：" + order.getTotal();

		entity.setProductFee(order.getProductFee());
		entity.setPromotionsReduction(order.getPromotionsReduction());
		entity.setCouponReduction(order.getCouponReduction());
		entity.setDeliverFee(order.getDeliverFee());
		entity.setOtherReduction(order.getOtherReduction());
		entity.setTotal(order.getTotal());
		entity.setNote(order.getNote());
		orderService.save(entity);

		OrderCheckState ocs = OrderCheckState.CONFIRM_PASS;
		orderLogService.save(user.getUsername(), order, ocs, note);
		
		List<OrderItem> orderItems = orderItemService.list(" from OrderItem where orderId=?",order.getId()) ;
 
		UserInfo userInfo = userInfoService.get(order.getUserId());
		JSONObject jsonObject  = new JSONObject();
		jsonObject.put("msgCode", "恭喜您，订单修改成功！");
		jsonObject.put("msghead", "订单发货成功！");
		messageNodeNotified.sendMessage(SendType.ORDER_SHIPMENTS,userInfo, userInfo,orderItems,order,jsonObject);

		WXUserInfo source = wXUserInfoService.select(" from WXUserInfo where miniOpenid = ? ", userInfo.getWechatMpOpenid());
		if(!StringUtils.isEmpty(source)){
			logger.info("wxUserInfo========="+source);
			userInfo.setWechatUnioid(source.getUnionid());
			userInfo.setWechatMpOpenid(source.getOpenid());
			userInfoService.save(userInfo);
			messageNodeNotified.sendMessage(MessageNodeType.ORDER_DELIVER_GOODS,source.getOpenid(),userInfo,order); //发送消息
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "人工调价");
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 后台协助企业付款
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "payOrderByCredits", method = RequestMethod.POST)
	public String payOrderByCredits(HttpServletRequest request, String id) throws Exception {
		Order order = orderService.get(id);
		if (!order.getSettleWay().equals(SettleWay.CREDIT)) {
			return ResultUtil.getError("该订单不是信用支付！");
		}
		Company company = companyService.get(order.getMemberId());
		if (null == company) {
			return ResultUtil.getError("企业信息不存在！");
		}
		if (company.getCreditOver().compareTo(order.getTotal()) == -1) {
			return ResultUtil.getError("信用余额不足！");
		}
		company.setCreditOver(company.getCreditOver().subtract(order.getTotal()));
		
		UserInfo userInfo = userInfoService.get(order.getUserId());
		
		if ( !(userInfo.getId().equals(company.getUserId())) ){
			
			if (userInfo.getRemainingCredit().intValue() < order.getTotal().intValue()){
				return ResultUtil.getError("用户信用余额不足,请联系管理员充值!"); 
			}
			
			  userInfo.setRemainingCredit(userInfo.getRemainingCredit().subtract(order.getTotal()));
		}
		
		order.setPayDate(new Date());
		order.setPayState(true);
		order.setSettleWay(SettleWay.CREDIT);
		order.setOrderState(OrderState.WAIT_DELIVERY);
		
		orderService.creditPay(order, company);

		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "协助买家通过信用支付订单！";
		OrderCheckState ocs = OrderCheckState.CONFIRM_BUYER_PAY;
		orderLogService.save(user.getUsername(), order, ocs, note);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "后台协助企业付款");
		
		// --------------------- 同步数据----------------------
		SyncDataUtils.syncSoleDate(company.getUserId(),"/syncData/saveOrder",order.getId());
		// ---------------------- end ----------------------
		
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 取消订单
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "cancelOrder", method = RequestMethod.GET)
	public String cancelOrder(HttpServletRequest request, String id) throws Exception {
		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "将订单状态置为取消！";
		Order order = orderService.get(id);
		orderService.cancelOrder(order, user.getUsername(), note);
		if (null != order.getPayDate() && order.getPayState()) {
			OrderCancel oc = new OrderCancel(order);
			String ocId = IDUtil.getInstance().getIdByDb(orderCancelService, OrderCancel.class, "OC", "id");
			oc.setId(ocId);
			orderCancelService.save(oc, user.getUsername(), note);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "后台取消订单");
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 订单完成
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */

	@ResponseBody
	@RequestMapping(value = "finishOrder", method = RequestMethod.GET)
	public String finishOrder(HttpServletRequest request, String id) throws Exception {
		Order order = orderService.get(id);

		order.setOrderState(OrderState.TRADE_FINISHED);
		
		OrderReceipt or = new OrderReceipt();
		or.setId(IDUtil.getInstance().getId("OR"));
		or.setUserInfoId(order.getUserId());
		or.setCompanyId(order.getMemberId());
		or.setPayType(PayType.ORDER);
		or.setOrderId(order.getId());
		or.setSettleWay(order.getSettleWay());
		or.setPayway(order.getPayway());
		or.setStatus(true);
		or.setCashWay(CashWay._DEFAULT);
		or.setOrderFee(order.getTotal());
		or.setOtherReduction(order.getOtherReduction());
		or.setCashFee(order.getTotal());
		or.setCouponReduction(order.getCouponReduction());
		or.setPromotionsReduction(order.getPromotionsReduction());
		or.setPayDate(order.getPayDate());
		or.setGiveScore(order.getTotal().intValue());
		
		orderService.finish(order, or);
		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "将订单状态置为完成！";
		OrderCheckState ocs = OrderCheckState.CONFIRM_FINISH;
		orderLogService.save(user.getUsername(), order, ocs, note);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "后台完成订单");
		
		List<OrderItem> orderItemList = orderItemService.list(" from OrderItem where orderId = ? ", order.getId());
		Integer score = 0;
		for (OrderItem oit : orderItemList) {
			if (null != oit.getScore()) {
				score += oit.getoAmountWithTax().intValue() * oit.getScore()/100;
			}
		}
		UserInfo userInfo=userInfoService.select("from UserInfo where id="+order.getUserId());
		
		//自己获取积分
		scoreLogService.updateScore(userInfo.getId(), score,ScoreType.BUY_GOODS);
		//判断自己是不是企业负责人如果不是的那么找到企业负责人
		boolean flag=companyService.isResp(userInfo.getId());
		SysParam sysParam=sysParamService.findParam("SHOP_RETURN_SCORE", SysParamType.SCORE_PARAM);
		if(flag){
		//给推荐人积分
		if(!StringUtils.isEmpty(userInfo.getCommendFriendId())){
			BigDecimal fee=	order.getProductFee();
			BigDecimal bili =new BigDecimal(sysParam.getValue());
			int Score=fee.multiply(bili).intValue();
			Integer comFid=userInfo.getCommendFriendId();
			scoreLogService.updateScore(comFid+"",Score,ScoreType.COMMEND_PLACE_AN_ORDER);
		}
		}else{
			//如果自己不是企业负责人那么给企业负责人的推荐人返积分
			Company company=userInfo.getCompany();
			UserInfo RespUserInfo=company.getUserInfo();//找到企业负责人
			if(!StringUtils.isEmpty(RespUserInfo.getCommendFriendId())){
				Integer CommUserId=	RespUserInfo.getCommendFriendId();
				BigDecimal fee=	order.getProductFee();
				BigDecimal bili =new BigDecimal(sysParam.getValue());
				int Score=fee.multiply(bili).intValue();
				scoreLogService.updateScore(CommUserId+"",Score,ScoreType.COMMEND_PLACE_AN_ORDER);
			}
		}
		
		
			
		// 订单完成后，将当前订单金额添加到该用户的消费额
		UserInfo user2 = userInfoService.get(order.getUserId());
		user2.setExpenditure(user2.getExpenditure().add(order.getTotal()));
		userInfoService.update(user2);
		
		
		// 订单完成后，给该用户添加订单金额等价的经验值
		expLogService.updateExp(user2.getId(),  ExpType.ORDER, (int)order.getTotal().doubleValue());
		
		
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 企业订单记录
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "company", method = RequestMethod.GET)
	public String companyOrder(HttpServletRequest request, Model model, String id) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String memberId = ServletRequestUtils.getStringParameter(request, "memberId", "");
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		String hql = " from Order t where 1=1";
		Page<Order> page = new Page<Order>(5);
		page.setPageNo(pageNo);
		List<Object> params = new ArrayList<Object>();
		if (memberId != null) {
			hql += " and t.memberId=?";
			params.add(memberId);
		}
		if (!StringUtils.isEmpty(id)) {
			hql = hql + " and t.id = ? ";
			params.add(id);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql = hql + " and t.createDate >= ?";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and t.createDate <= ?";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		hql += " order by t.createDate desc ";
		page = orderService.page(page, hql, params.toArray());
		model.addAttribute("id", id);
		model.addAttribute("pageOrder", page);
		model.addAttribute("memberId", memberId);
		return "order/companyOrder";
	}

	/**
	 * 账房 >收支查询 >订单明细
	 */
	@RequestMapping(value = "reveExpenList", method = RequestMethod.GET)
	public String reveExpenListGet(HttpServletRequest request, Model model) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);

		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		String company = ServletRequestUtils.getStringParameter(request, "company", "");
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");

		Page<Order> page = new Page<Order>();
		page.setPageNo(pageNo);
		StringBuilder hql = new StringBuilder("from Order t where 1 =1");
		List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(id)) {
			hql.append(" and t.id =?");
			params.add(id);
			model.addAttribute("id", id);
		}
		if (!StringUtils.isEmpty(company)) {
			hql.append(" and t.company like ?");
			params.add('%' + company + '%');
			model.addAttribute("company", company);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql.append(" and t.payDate >= ?");
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql.append(" and t.payDate < ?");
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}

		hql.append(" order by t.createDate desc");
		page = orderService.page(page, hql.toString(), params.toArray());

		model.addAttribute("pageOrder", page);
		return "order/orderReveExpenList";
	}

	/**
	 * 打印订单
	 * 
	 * @param id
	 * @param templatePrintType
	 * @param model
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	@RequestMapping(value = "printOrder", method = RequestMethod.GET)
	public String printOrder(String id, TemplatePrintType templatePrintType, Model model)
			throws WriterException, IOException {

		Order order = orderService.get(id);
		OrderCode orderCode = orderCodeService.select(" from OrderCode where orderId = ? ", order.getId());
		if (orderCode == null) { // 如果为空 生成 订单二维码 与条形码
			orderCode = new OrderCode();
			String bar = GenerateZxing.barEnCodeBase64(order.getId());
			String qr = GenerateZxing.qrEnCodeBase64(order.getId());

			orderCode.setOrderId(order.getId());
			orderCode.setBarCode(bar);
			orderCode.setQrCode(qr);

			orderCodeService.save(orderCode);
		}
		List<OrderItem> orderItemList = orderItemService.list(" from OrderItem where orderId = ? ", order.getId());
		Map<OrderItem, Product> productMap = new HashMap<>();
		Integer total = 0;
		Set<Integer> setId = new HashSet<>();
		for (OrderItem orderItem : orderItemList) {

			if (!setId.contains(orderItem.getProductId())) {
				setId.add(orderItem.getProductId());
			}

			Product product = productService.get(orderItem.getProductId());
			total += orderItem.getQuantity();
			productMap.put(orderItem, product);
		}

		BigDecimal discounts = new BigDecimal(0);
		discounts.add(order.getPromotionsReduction());
		discounts.add(order.getCouponReduction());
		discounts.add(order.getCouponReduction());
		BigDecimal productTotal = order.getTotal().subtract(order.getDeliverFee());
		TemplatePrint templatePrint = templatePrintService.select(" from TemplatePrint where templatePrintType = ? ",
				templatePrintType);

		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);

		model.addAttribute("orderCode", orderCode);
		model.addAttribute("kind", setId.size());
		model.addAttribute("productTotal", productTotal);
		model.addAttribute("discounts", discounts);
		model.addAttribute("total", total);
		model.addAttribute("order", order);
		model.addAttribute("productMap", productMap);
		model.addAttribute("orderItemList", orderItemList);
		model.addAttribute("templatePrint", templatePrint);
		return "order/orderPrint";
	}

	/**
	 * 打印快递
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "printExpress", method = RequestMethod.GET)
	public String printExpress(String id, Model model) throws Exception {

		Logistics logistics = logisticsService.select(" from Logistics where defaultState = ?", true);
		Express express = expressService.select(" from Express where logisticsid = ? ", logistics.getId());
		List<SendGoods> sendGoodsList = sendGoodsService.list(" from SendGoods where orderId = ? ", id) ;
		SendGoods sendGoods = sendGoodsList.size() >0 ? sendGoodsList.get(0) : null ;
		List<OrderItem> orderItemList = orderItemService.list(" from OrderItem where orderId = ? ", id);
		OrderCode orderCode = orderCodeService.select(" from OrderCode where orderId = ? ", id);
		if (orderCode == null) { // 为空 生成 订单二维码 与条形码
			orderCode = new OrderCode();
			String bar = GenerateZxing.barEnCodeBase64(id);
			String qr = GenerateZxing.qrEnCodeBase64(id);

			orderCode.setOrderId(id);
			orderCode.setBarCode(bar);
			orderCode.setQrCode(qr);

			orderCodeService.save(orderCode);
		}
		WareHouse wareHouse = wareHouseService.get(sendGoods.getSendId());

		Integer sum = 0; // 商品总数
		List<String> productNameList = new ArrayList<>(); // 商品名称
		List<String> productcodeList = new ArrayList<>(); // 商品编码
		Set<Integer> setId = new HashSet<>();
		for (OrderItem orderItem : orderItemList) {

			if (!setId.contains(orderItem.getProductId())) { // 重复不取
				setId.add(orderItem.getProductId());
				Product product = productService.get(orderItem.getProductId());

				productNameList.add(orderItem.getProductName());
				productcodeList.add(product.getCode());
			}

			sum += orderItem.getQuantity();
		}

		String site = wareHouse.getProvince() + "," + wareHouse.getCity() + "," + wareHouse.getCounty() + ","
				+ wareHouse.getAddress();

		VelocityContext velocity = new VelocityContext(); // 如model 一样传参
		velocity.put("site", site);
		velocity.put("delivery", wareHouse);
		velocity.put("sendGoods", sendGoods);
		velocity.put("orderCode", orderCode);
		velocity.put("orderId", id);
		velocity.put("productcodeList", org.apache.commons.lang.StringUtils.join(productcodeList, ','));
		velocity.put("productNameList", org.apache.commons.lang.StringUtils.join(productNameList, ','));
		velocity.put("sendGoods", sendGoods);
		velocity.put("sum", sum);
		velocity.put("consigneeCompany", orderService.get(id).getCompany());
		String expressHtml = VelocityHtmlUtils.vm(express.getExpressTemplate(), velocity);// 解析html

		model.addAttribute("express", express);
		model.addAttribute("expressHtml", expressHtml);

		return "order/expressPrint";
	}
	
	
	@RequestMapping(value = "exportExcel", method = RequestMethod.POST)
	public @ResponseBody void exportExcel(String[] oId, HttpServletResponse response,HttpServletRequest request) throws Exception {
			if(ArrayUtils.isEmpty(oId)){
			return ;
		}
		List<OrderDetailExcel> list = new ArrayList<>();
		for (String item : oId) {
			OrderDetailExcel orderDetailExcel = new OrderDetailExcel();
			Order order = orderService.get(item);
			orderDetailExcel.setOrderId(order.getId());
			orderDetailExcel.setOrderState(order.getOrderState().getName());
			orderDetailExcel.setOrderTotalAmount(order.getTotal());
			orderDetailExcel.setCompanyName(order.getCompany());
			orderDetailExcel.setPayTime(order.getPayTimeString());
			orderDetailExcel.setFreightFee(order.getDeliverFee());
			orderDetailExcel.setGoodsTotalAmount(order.getProductFee());
			orderDetailExcel.setUserInfoId(order.getUsername());
			orderDetailExcel.setPreferentialReduction(order.getCouponReduction());
			list.add(orderDetailExcel);
		}
		
	    String filename = URLEncoder.encode("订单明细", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename="+filename+System.currentTimeMillis()+".xlsx"); //名字加时间戳
		ServletOutputStream sos =  response.getOutputStream();
		ExcelWrite.writeExcel(sos, list); //写入 Servlet 输出流中 
		sos.close();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "后台导出订单");
	}
	
	
}