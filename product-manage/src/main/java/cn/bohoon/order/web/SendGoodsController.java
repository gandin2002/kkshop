package cn.bohoon.order.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import com.qrcode.GenerateZxing;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.domain.TemplatePrintType;
import cn.bohoon.basicSetup.entity.TemplatePrint;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.basicSetup.service.TemplatePrintService;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.express.entity.Logistics;
import cn.bohoon.express.service.LogisticsService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.util.ResultUtil;
import cn.bohoon.message.aop.MessageNodeNotified;
import cn.bohoon.message.domain.MessageNodeType;
import cn.bohoon.message.domain.SendType;
import cn.bohoon.message.service.MessageSiteService;
import cn.bohoon.order.domain.OrderCheckState;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.OrderType;
import cn.bohoon.order.domain.SendGoodInfo;
import cn.bohoon.order.domain.SendGoodsExcel;
import cn.bohoon.order.domain.SendGoodsItmeExcel;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderBarter;
import cn.bohoon.order.entity.OrderCode;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderRepaire;
import cn.bohoon.order.entity.SendGoods;
import cn.bohoon.order.service.OrderBarterService;
import cn.bohoon.order.service.OrderCodeService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderLogService;
import cn.bohoon.order.service.OrderRepaireService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.order.service.SendGoodsService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.stock.entity.WareHouse;
import cn.bohoon.stock.entity.WarehLocation;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.stock.service.WarehLocationService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ConvertUtils;
import cn.bohoon.wx.mp.entity.WXUserInfo;
import cn.bohoon.wx.mp.service.WXUserInfoService;

@Controller
@RequestMapping(value = "sendGoods")
public class SendGoodsController {

	@Autowired
	SkuService skuService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderBarterService orderBarterService;
	@Autowired
	OrderRepaireService orderRepaireService;
	@Autowired
	OrderLogService orderLogService;
	@Autowired
	LogisticsService logisticsService;
	@Autowired
	WareHouseService wareHouseService; // 发货仓库
	@Autowired
	SendGoodsService sendGoodsService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ProductService productService;
	@Autowired
	MessageSiteService messageSiteService;
	@Autowired
	WarehLocationService warehLocationService;

	@Autowired
	WXUserInfoService wXUserInfoService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 发货单列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws ParseException, ServletRequestBindingException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String id = ServletRequestUtils.getStringParameter(request, "id", "").trim();
		String revicer = ServletRequestUtils.getStringParameter(request, "revicer", "");
		String rePhone = ServletRequestUtils.getStringParameter(request, "rePhone", "").trim();
		// String transCompany = ServletRequestUtils.getStringParameter(request,
		// "transCompany","").trim();
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		String senderCompany = ServletRequestUtils.getStringParameter(request, "senderCompany", "").trim();

		String createEndTime = ServletRequestUtils.getStringParameter(request, "createEndTime", "");
		String createStartTime = ServletRequestUtils.getStringParameter(request, "createStartTime", "").trim();

		
		
		StringBuilder hql = new StringBuilder("from SendGoods s where 1 = 1");
		List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(id)) {
			hql.append(" and s.id = ?");
			params.add(id);
			model.addAttribute("id", id);
		}
		if (!StringUtils.isEmpty(revicer)) {
			hql.append(" and s.revicer like ?");
			params.add('%' + revicer + '%');
			model.addAttribute("revicer", revicer);
		}
		if (!StringUtils.isEmpty(rePhone)) {
			hql.append(" and s.rePhone like ?");
			params.add('%' + rePhone + '%');
			model.addAttribute("rePhone", rePhone);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql.append(" and s.sendTime >= ?");
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql.append(" and s.sendTime < ?");
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		if(!StringUtils.isEmpty(createStartTime)){
			hql.append(" and s.createTime >= ?");
			params.add(DateUtil.switchStringToDate(createStartTime, "yy-MM-dd"));
			model.addAttribute("createStartTime", createStartTime);
		}
		if(!StringUtils.isEmpty(createEndTime)){
			hql.append(" and s.createTime < ?");
			params.add(DateUtil.getNDayAfter(createEndTime, 1));
			model.addAttribute("createEndTime", createEndTime);
		}
		
		if (!StringUtils.isEmpty(senderCompany)) {
			hql.append(" and s.senderCompany=? ");
			params.add(senderCompany);
			model.addAttribute("senderCompany", senderCompany);
		}

		Page<SendGoods> pageSendGoods = new Page<SendGoods>();
		pageSendGoods.setPageNo(pageNo);
		hql.append(" order by s.sendTime desc");
		List<SendGoods> sendGoodsList = new ArrayList<SendGoods>();
		sendGoodsList = sendGoodsService.list(hql.toString(), params.toArray());
		pageSendGoods = sendGoodsService.page(pageSendGoods, hql.toString(), params.toArray());
		sendGoodsList = sendGoodsService.list(hql.toString(), params.toArray());
		String hql2 = " from  WareHouse where 1=1";
		List<WareHouse> wareHouseListAll = wareHouseService.list(hql2);
		BigDecimal Numfee = new BigDecimal(0.00);
		if (!StringUtils.isEmpty(sendGoodsList)) {
			for (SendGoods sd : sendGoodsList) {
				Order order = orderService.get(sd.getOrderId());
				if (!StringUtils.isEmpty(order)) {
					Numfee = Numfee.add(order.getProductFee());
				}
			}
		}
		Map<SendGoods, Order> orderMap = new HashMap<>();
		if (orderMap != null) {
			for (SendGoods sendGoods : pageSendGoods.getResult()) {
				orderMap.put(sendGoods, orderService.get(sendGoods.getOrderId()));
			}
		}
		Map<SendGoods, WarehLocation> WarMap = new HashMap<>();
		if (WarMap != null) {
			for (SendGoods sendGoods : pageSendGoods.getResult()) {
				WarMap.put(sendGoods, warehLocationService.get(sendGoods.getCurrSendNum()));
			}
		}

		model.addAttribute("WarMap", WarMap);
		model.addAttribute("wareHouseListAll", wareHouseListAll);
		model.addAttribute("pageSendGoods", pageSendGoods);
		model.addAttribute("orderMap", orderMap);
		model.addAttribute("Numfee", Numfee);
		return "sendGoods/sendGoodsList";
	}

	/**
	 * 购买订单 去发货页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		String orderId = ServletRequestUtils.getStringParameter(request, "id", "");
		List<SendGoods> sgs = sendGoodsService.list(" from SendGoods where orderId=?", orderId);
		init(model, sgs, orderId, "order");
		Order order = orderService.get(orderId);
		model.addAttribute("order", order);
		return "sendGoods/sendGoodsAdd";
	}

	/**
	 * 换货单去发货页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add2", method = RequestMethod.GET)
	public String add2Get(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		OrderBarter obModel = orderBarterService.get(id);
		String orderId = obModel.getAmOrder();
		List<SendGoods> sgs = sendGoodsService.list(" from SendGoods where orderId=?", id);

		init(model, sgs, orderId, "aftermarktOrder");
		model.addAttribute("obModel", obModel);
		Order order = orderService.get(obModel.getOrderId());
		model.addAttribute("order", order);

		return "sendGoods/sendGoodsAdd2";
	}

	/**
	 * 维修单 去发货页面.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add3", method = RequestMethod.GET)
	public String add3Get(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		OrderRepaire orderRepaire = orderRepaireService.get(id);
		String orderId = orderRepaire.getAmOrder();
		List<SendGoods> sgs = sendGoodsService.list(" from SendGoods where orderId=?", id);

		init(model, sgs, orderId, "aftermarktOrder");
		model.addAttribute("orderRepaire", orderRepaire);
		Order order = orderService.get(orderRepaire.getOrderId());
		model.addAttribute("order", order);

		return "sendGoods/sendGoodsAdd3";
	}

	/**
	 * 发货初始化
	 * 
	 * @param model
	 * @param sgs
	 * @param orderId
	 * @param flag
	 */
	public void init(Model model, List<SendGoods> sgs, String orderId, String flag) {
		List<OrderItem> itemList = orderItemService.list(" from OrderItem where orderId=?", orderId);

		String hql = " from Logistics s where 1 = 1 and status=?";
		List<Object> params = new ArrayList<Object>();
		params.add(1);
		List<Logistics> logistics = logisticsService.list(hql, params.toArray());

		Map<Integer, Integer> alMap = new HashMap<>();
		for (SendGoods sg : sgs) {
			JSONArray array = JsonUtil.parse(sg.getSendItem(), JSONArray.class);
			if (null != array && array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					String jsonStr = array.getJSONObject(i).toJSONString();
					SendGoodInfo sdi = JsonUtil.parse(jsonStr, SendGoodInfo.class);
					Integer v = 0;
					if (alMap.containsKey(sdi.getId())) {
						v = alMap.get(sdi.getId());
					}
					alMap.put(sdi.getId(), v + sdi.getNum());
				}
			}
		}
		Map<Integer, Integer> caMap = new HashMap<>();
		Map<OrderItem, Sku> itemSkuMap = new HashMap<OrderItem, Sku>();
		for (OrderItem oi : itemList) {
			if (alMap.containsKey(oi.getId())) {
				caMap.put(oi.getId(), oi.getSaleQuantity() - alMap.get(oi.getId()));
			}
			Integer skuId = ConvertUtils.parseInteger(oi.getSkuId());
			Sku sku = skuService.get(skuId);
			itemSkuMap.put(oi, sku);
		}
		
		Logistics logistics1	=logisticsService.select("from Logistics where defaultState=?",true);
		model.addAttribute("logistics1", logistics1);
		
		List<WareHouse> wareHouses = wareHouseService.list("from WareHouse where shipAddress = 1");
		WareHouse wareHouse	=wareHouseService.select("from WareHouse where isDefault =?",true );
		model.addAttribute("deliverys1", wareHouse);
		model.addAttribute("caMap", caMap);
		model.addAttribute("alMap", alMap);
		model.addAttribute("itemSkuMap", itemSkuMap);
		model.addAttribute("currentTime", DateUtil.formatDate(new Date()));
		model.addAttribute("orderId", orderId);
		model.addAttribute("itemList", itemList);
		model.addAttribute("deliverys", wareHouses);
		model.addAttribute("logistics", logistics);
	}

	/**
	 * 购买订单 发货提交
	 * 
	 * @param request
	 * @param sendGoods
	 * @param itemId
	 * @param sendNum
	 * @param quantity
	 * @param sendedNum
	 * @return
	 * @throws Exception
	 */

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	MessageNodeNotified messageNodeNotified;

	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, SendGoods sendGoods, String itemId, String sendNum,
			String quantity, String sendedNum) throws Exception {
		System.out.println("----" + sendGoods.getTransCompanycode());
		String orderId = sendGoods.getOrderId();
		Order order = orderService.get(orderId);
		
		String result = "";
		if (OrderType.SHOPPING.equals(order.getOrderType())) {
			result = sendGoodsService.saveKq(sendGoods, itemId, sendNum, quantity, sendedNum);
		} else {
			result = sendGoodsService.saveScoreGoodsKq(sendGoods, itemId, sendNum, quantity, sendedNum);
		}
		if (!"".equals(result)) {
			return result;
		}

		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "发货！收件人：" + sendGoods.getRevicer() + "，物流：" + sendGoods.getTransCompany()
				+ "；单号：" + sendGoods.getTransNum();
		OrderCheckState ocs = OrderCheckState.CONFIRM_DELIVERY;
		orderLogService.save(user.getUsername(), order, ocs, note);

		String hql = " from OrderItem where storageOut=0 and orderId=?";
		List<OrderItem> notSendItems = orderItemService.list(hql, sendGoods.getOrderId());
		if (notSendItems.size() == 0) {// 发货完成，状态改为待收货
			order.setOrderState(OrderState.WAIT_REVICE);
			orderService.save(order);
		}

		UserInfo userInfo = userInfoService.get(order.getUserId());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msgCode", "恭喜您，订单已发货！");
		jsonObject.put("msghead", "订单发货成功！");
		messageNodeNotified.sendMessage(SendType.ORDER_SHIPMENTS, userInfo, userInfo, order, jsonObject);

		WXUserInfo source = wXUserInfoService.select(" from WXUserInfo where miniOpenid = ? ",
				userInfo.getWechatMpOpenid());
		if (!StringUtils.isEmpty(source)) {
			logger.info("wxUserInfo=========" + source);
			userInfo.setWechatUnioid(source.getUnionid());
			userInfo.setWechatMpOpenid(source.getOpenid());
			userInfoService.save(userInfo);
			messageNodeNotified.sendMessage(MessageNodeType.ORDER_DELIVER_GOODS, source.getOpenid(), userInfo, order); // 发送消息
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增发货单");
		return ResultUtil.getSuccess();
	}

	/**
	 * 换货 发货提交
	 * 
	 * @param request
	 * @param sendGoods
	 * @param itemId
	 * @param sendNum
	 * @param quantity
	 * @param sendedNum
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "add2", method = RequestMethod.POST)
	public String add2Post(HttpServletRequest request, SendGoods sendGoods, String itemId, String sendNum,
			String quantity, String sendedNum) throws Exception {

		String result = sendGoodsService.saveKq(sendGoods, itemId, sendNum, quantity, sendedNum);
		if (!"".equals(result)) {
			return result;
		}
		OrderBarter obModel = orderBarterService.get(sendGoods.getOrderId());
		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "发货！收件人：" + sendGoods.getRevicer() + "，物流：" + sendGoods.getTransCompany()
				+ "；单号：" + sendGoods.getTransNum();
		OrderCheckState ocs = OrderCheckState.CONFIRM_DELIVERY;
		orderLogService.save(user.getUsername(), obModel, ocs, note);

		String hql = " from OrderItem where storageOut=0 and orderId=?";
		List<OrderItem> notSendItems = orderItemService.list(hql, sendGoods.getOrderId());
		if (notSendItems.size() == 0) {// 发货完成，状态改为待收货
			obModel.setOrderState(OrderState.WAIT_REVICE);
			orderBarterService.save(obModel);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增换货发货单");
		return ResultUtil.getSuccess();
	}

	/**
	 * 维修 发货提交
	 * 
	 * @param request
	 * @param sendGoods
	 * @param itemId
	 * @param sendNum
	 * @param quantity
	 * @param sendedNum
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "add3", method = RequestMethod.POST)
	public String add3Post(HttpServletRequest request, SendGoods sendGoods, String itemId, String sendNum,
			String quantity, String sendedNum) throws Exception {

		String result = sendGoodsService.save(sendGoods, itemId, sendNum, quantity, sendedNum);
		if (!"".equals(result)) {
			return result;
		}
		OrderRepaire orderRepaire = orderRepaireService.get(sendGoods.getOrderId());
		LoginUser user = UserSession.getLoginUser(request);
		String note = user.getUsername() + "发货！收件人：" + sendGoods.getRevicer() + "，物流：" + sendGoods.getTransCompany()
				+ "；单号：" + sendGoods.getTransNum();
		OrderCheckState ocs = OrderCheckState.CONFIRM_DELIVERY;
		orderLogService.save(user.getUsername(), orderRepaire, ocs, note);

		String hql = " from OrderItem where storageOut=0 and orderId=?";
		List<OrderItem> notSendItems = orderItemService.list(hql, sendGoods.getOrderId());
		if (notSendItems.size() == 0) {// 发货完成，状态改为待收货
			orderRepaire.setOrderState(OrderState.WAIT_REVICE);
			orderRepaireService.save(orderRepaire);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增维修发货单");
		return ResultUtil.getSuccess();
	}

	/**
	 * 编辑物流单信息页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		String orderId = ServletRequestUtils.getStringParameter(request, "orderId", "");

		List<OrderItem> itemList = orderItemService.list(" from OrderItem where orderId=?", orderId);
		List<Logistics> logistics = logisticsService.list();

		List<SendGoods> sgs = sendGoodsService.list(" from SendGoods where orderId=?", orderId);
		SendGoods item = sgs.size() > 0 ? sgs.get(0) : null;
		if (!"".equals(id)) {
			item = sendGoodsService.get(id);
		}
		Map<Integer, Integer> alMap = new HashMap<>();
		if (null != item) {
			JSONArray array = JsonUtil.parse(item.getSendItem(), JSONArray.class);
			if (null != array && array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					String jsonStr = array.getJSONObject(i).toJSONString();
					SendGoodInfo sdi = JsonUtil.parse(jsonStr, SendGoodInfo.class);
					Integer v = 0;
					if (alMap.containsKey(sdi.getId())) {
						v = alMap.get(sdi.getId());
					}
					alMap.put(sdi.getId(), v + sdi.getNum());
				}
			}
		}
		List<WareHouse> deliverys = wareHouseService.list();
		model.addAttribute("sgs", sgs);
		model.addAttribute("item", item);
		model.addAttribute("alMap", alMap);
		model.addAttribute("orderId", orderId);
		model.addAttribute("itemList", itemList);
		model.addAttribute("deliverys", deliverys);
		model.addAttribute("logistics", logistics);
		return "sendGoods/sendGoodsEdit";
	}

	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(SendGoods model,HttpServletRequest request) throws Exception {
		SendGoods entity = sendGoodsService.get(model.getId());
		entity.setRevicer(model.getRevicer());
		entity.setTransCompany(model.getTransCompany());
		entity.setTransCompanycode(model.getTransCompanycode());
		entity.setTransNum(model.getTransNum());
		entity.setReArea(model.getReArea());
		entity.setReAdress(model.getReAdress());
		entity.setRePhone(model.getRePhone());
		entity.setSendTime(model.getSendTime());
		entity.setNote(model.getNote());
		entity.setSenderCompany(model.getSenderCompany());
		entity.setSendAdress(model.getSendAdress());
		entity.setSendId(model.getSendId());
		sendGoodsService.save(entity);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改发货单:id"+entity.getId());
		return ResultUtil.getSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, String id) throws Exception {
		sendGoodsService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除发货单:id"+id);
		return ResultUtil.getSuccess();
	}

	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String detailGet(HttpServletRequest request, Model model, String id) {
		SendGoods sendGoods = sendGoodsService.get(id);
		Order order = orderService.get(sendGoods.getOrderId());

		List<OrderItem> itemList = orderItemService.list("from OrderItem o where o.orderId = ? order by o.id asc",
				sendGoods.getOrderId());
		Map<OrderItem,Sku> map =  new HashMap<OrderItem,Sku>();
			for (OrderItem orderItem : itemList) {
				Sku sku=skuService.select("from Sku where id="+orderItem.getSkuId());
				map.put(orderItem, sku);
			}
		Map<Integer, Integer> itemSendMap = sendGoods.getSendItemMap();

		OrderItem orderItem = new OrderItem();
		
		for (Iterator<OrderItem> iterator = itemList.iterator(); iterator.hasNext();) {
			orderItem = iterator.next();
			if (!itemSendMap.containsKey(orderItem.getId())) {
				iterator.remove();
			}

		}
		int QuantityNum = 0;
		for (OrderItem items : itemList) {
			QuantityNum += items.getQuantity();

		}
		int NOnunber = 0;
		for (Map.Entry<Integer, Integer> entry : itemSendMap.entrySet()) {
			NOnunber += entry.getValue();
		}
		Set ss = new HashSet<>();
		for (OrderItem itemList1 : itemList) {
			ss.add(orderItem.getProductId());
		}

		int productNumber = ss.size();
		List<String> categoryNameList = new ArrayList<String>();
		List<String> productList = new ArrayList<String>();
		
		for (OrderItem OrderItem : itemList) {
			Product product = productService.get(OrderItem.getProductId());
			categoryNameList.add(product.getCategoryName());
			productList.add(product.getBrandName());

		}
		
		
		/**
		 * for (OrderItem OrderItem : itemList) {
			Product product = productService.get(OrderItem.getProductId());
			if(!StringUtils.isEmpty(product)){
				
				Category category=	categoryService.select("from Category where id="+product.getCategoryId());
				
				categoryNameList.add(category.getName());
				productList.add(product.getBrandName());
			}
		}
		 */
		WarehLocation warehLocation = warehLocationService.get(sendGoods.getCurrSendNum());
		model.addAttribute("map", map);
		model.addAttribute("productNumber", productNumber);
		model.addAttribute("categoryNameList", categoryNameList);
		model.addAttribute("warehLocation", warehLocation);
		model.addAttribute("productList", productList);
		model.addAttribute("NOnunber", NOnunber);
		model.addAttribute("QuantityNum", QuantityNum);
		model.addAttribute("item", sendGoods);
		model.addAttribute("itemOrder", order);
		model.addAttribute("itemList", itemList);
		model.addAttribute("itemSendMap", itemSendMap);
		model.addAttribute("sendGoods", sendGoods);
		return "sendGoods/sendGoodsDetail";
	}

	/** 物流信息 */
	@RequestMapping(value = "logistics", method = RequestMethod.GET)
	public String logisticsGet(HttpServletRequest request, Model model, String transNum) {

		return "sendGoods/logistics";
	}

	public Integer parseInteger(String val) {
		int result = 0;
		try {
			result = Integer.parseInt(val);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	/**
	 * 导出发货单列表
	 * 
	 * @param rId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "exportExcel", method = RequestMethod.POST)
	public @ResponseBody void exportExcel(@RequestParam(name="exportState",required=true)Boolean exportState,String[] pid, HttpServletResponse response,HttpServletRequest request) throws Exception {
		List<SendGoods> sendGoodsList =  new ArrayList<>();
		if(!exportState){
			String id = ServletRequestUtils.getStringParameter(request, "id", "").trim();
			String revicer = ServletRequestUtils.getStringParameter(request, "revicer", "");
			String rePhone = ServletRequestUtils.getStringParameter(request, "rePhone", "").trim();
			// String transCompany = ServletRequestUtils.getStringParameter(request,
			// "transCompany","").trim();
			String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
			String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
			String senderCompany = ServletRequestUtils.getStringParameter(request, "senderCompany", "").trim();

			String createEndTime = ServletRequestUtils.getStringParameter(request, "createEndTime", "");
			String createStartTime = ServletRequestUtils.getStringParameter(request, "createStartTime", "").trim();
			
			
			
			StringBuilder hql = new StringBuilder("from SendGoods s where 1 = 1");
			List<Object> params = new ArrayList<>();
			if (!StringUtils.isEmpty(id)) {
				hql.append(" and s.id = ?");
				params.add(id);
			}
			if (!StringUtils.isEmpty(revicer)) {
				hql.append(" and s.revicer like ?");
				params.add('%' + revicer + '%');
			}
			if (!StringUtils.isEmpty(rePhone)) {
				hql.append(" and s.rePhone like ?");
				params.add('%' + rePhone + '%');
			}
			if (!StringUtils.isEmpty(startTime)) {
				hql.append(" and s.affirmTime >= ?");
				params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			}
			if (!StringUtils.isEmpty(endTime)) {
				hql.append(" and s.affirmTime < ?");
				params.add(DateUtil.getNDayAfter(endTime, 1));
			}
			if(!StringUtils.isEmpty(createStartTime)){
				hql.append(" and s.createTime >= ?");
				params.add(DateUtil.switchStringToDate(createStartTime, "yy-MM-dd"));
			}
			if(!StringUtils.isEmpty(createEndTime)){
				hql.append(" and s.createTime < ?");
				params.add(DateUtil.getNDayAfter(createEndTime, 1));
			}
			
			if (!StringUtils.isEmpty(senderCompany)) {
				hql.append(" and s.senderCompany=? ");
				params.add(senderCompany);
			}
			hql.append(" order by s.sendTime desc");
			sendGoodsList = sendGoodsService.list(hql.toString(), params.toArray());
		}else{
			for (String id : pid) {
				SendGoods sendGoods  = sendGoodsService.get(id);
				sendGoodsList.add(sendGoods);
			}
		}
		List<SendGoodsExcel> list = new ArrayList<>();
		for (SendGoods sendGoods : sendGoodsList) {
			Order order = orderService.get(sendGoods.getOrderId());
			SendGoodsExcel sendGoodsExcel = new SendGoodsExcel();
			sendGoodsExcel.setSendGoodsId(sendGoods.getId());// 发货单
			sendGoodsExcel.setOrderId(order.getId()); // 订单ID
			sendGoodsExcel.setExpressCompany(sendGoods.getTransCompany()); // 物流公司
			sendGoodsExcel.setUserInfoId(order.getUserId().toString()); // 用户Id
			sendGoodsExcel.setConsignee(sendGoods.getRevicer());// 收件人
			sendGoodsExcel.setConsigneePhone(sendGoods.getRePhone());// 收件人手机号
			sendGoodsExcel.setSendGoodsTime(sendGoods.getAffirmTimeString());// 发货时间
			sendGoodsExcel.setCompanyName(order.getCompany());
			sendGoodsExcel.setCreateGoodsTime(sendGoods.getCreateTimeString());
			if (!StringUtils.isEmpty(order.getCompany())) {
				sendGoodsExcel.setCompanyName(order.getCompany());
			}
			list.add(sendGoodsExcel);
		}
		String filename = URLEncoder.encode("发货单", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition",
				"attachment; filename=" + filename + System.currentTimeMillis() + ".xlsx"); // 名字加时间戳
		ServletOutputStream sos = response.getOutputStream();
		ExcelWrite.writeExcel(sos, list); // 写入 Servlet 输出流中
		sos.close();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导出发货单");
	}

	/**
	 * 导出单商品详细
	 * 
	 * @param rId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "exportOrderExcel", method = RequestMethod.POST)
	public @ResponseBody void exportOrderExcel(String sId, HttpServletResponse response,HttpServletRequest request) throws Exception {
		SendGoods sendGoods = sendGoodsService.get(sId);

		List<OrderItem> itemList = orderItemService.list("from OrderItem o where o.orderId = ? order by o.id asc",
				sendGoods.getOrderId());
		Map<Integer, Integer> itemSendMap = sendGoods.getSendItemMap(); // 发货数量
		OrderItem orderItem = new OrderItem();
		for (Iterator<OrderItem> iterator = itemList.iterator(); iterator.hasNext();) {
			orderItem = iterator.next();
			if (!itemSendMap.containsKey(orderItem.getId())) { // 有相同移除
				iterator.remove();
			}
		}
		List<SendGoodsItmeExcel> list = new ArrayList<>();
		for (OrderItem orderItem1 : itemList) {
			SendGoodsItmeExcel sendGoodsItmeExcel = new SendGoodsItmeExcel();
			sendGoodsItmeExcel.setId(orderItem1.getSkuCode());
			sendGoodsItmeExcel.setSgnum(itemSendMap.get(orderItem1.getId()));
			sendGoodsItmeExcel.setName(orderItem1.getProductName());// 产品名称.
			sendGoodsItmeExcel.setUnit(orderItem1.getAttrAndAttrValues()); // 规格
			sendGoodsItmeExcel.setQuantity(orderItem1.getQuantity());// 购买数量
			sendGoodsItmeExcel.setBrand(productService.get(orderItem1.getProductId()).getBrandName());// 品牌名字
			list.add(sendGoodsItmeExcel);
		}
		String filename = URLEncoder.encode("发货单商品详情", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename=" + filename + sId + ".xlsx"); // 名字加时间戳
		ServletOutputStream sos = response.getOutputStream();
		ExcelWrite.writeExcel(sos, list); // 写入 Servlet 输出流中
		sos.close();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导出发货单商品详细");
	}

	@Autowired
	TemplatePrintService templatePrintService;

	@Autowired
	OrderCodeService orderCodeService;

	@Autowired
	SysParamService sysParamService;

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
	public String printOrder(String id, Model model) throws WriterException, IOException {

		SendGoods sendGoods = sendGoodsService.get(id);

		Order order = orderService.get(sendGoods.getOrderId());
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
		Map<Integer, Integer> itemSendMap = sendGoods.getSendItemMap(); // 发货数量
		OrderItem otm = new OrderItem();
		for (Iterator<OrderItem> iterator = orderItemList.iterator(); iterator.hasNext();) {
			otm = iterator.next();
			if (!itemSendMap.containsKey(otm.getId())) { // 有相同移除
				iterator.remove();
			}
		}

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
				TemplatePrintType.SEND_GOODS_NAME);

		Map<String, String> sysParamMap = sysParamService.findParamMap(SysParamType.PLATFORM_PARAM);
		model.addAttribute("sysParamMap", sysParamMap);

		WareHouse wareHouse = wareHouseService.get(sendGoods.getSendId() == null ? 0 : sendGoods.getSendId());

		model.addAttribute("wareHouse", wareHouse);
		model.addAttribute("sendGoods", sendGoods);
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

	@RequestMapping(value = "affirm", method = RequestMethod.GET)
	public @ResponseBody String affirm(String id) {
		SendGoods sendGoods = sendGoodsService.get(id);
		sendGoods.setAffirmTime(new Date());
		sendGoodsService.save(sendGoods);
		return ResultUtil.getSuccess();
	}
}