package cn.bohoon.index.web;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.order.service.AfterMarketOrderService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.order.service.SendGoodsService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.quotation.service.QuotationItemService;
import cn.bohoon.userInfo.service.ScoreLogService;
import cn.bohoon.userInfo.service.UserInfoService;

@Controller
@RequestMapping(value = "index")
public class HomePageController {
	
	@Autowired
	OrderService orderService ;
	@Autowired
	CompanyService companyService ;
	@Autowired
	UserInfoService userInfoService ;
	@Autowired
	SendGoodsService sendGoodsService ;
	@Autowired
	AfterMarketOrderService afterMarketOrderService ;
	@Autowired
	ScoreLogService scoreLogService;
	@Autowired
	SkuService skuService;
	@Autowired
	ProductService productService;
	@Autowired
	SysParamService sysParamService;
	@Autowired
	QuotationItemService quotationItemservice;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		
		/*******************其他待处理事项******************/
		//待处理售后单
		
		
		//待审核实名会员
		Long idCard=userInfoService.select("SELECT count(*) from UserInfo where idCardAuthState in ('NO_AUTHED','AUTHING','REAUTHING','AUTH_REFUSE')",Long.class);
		model.addAttribute("idCard", idCard);
		
		//库存预警货品
		Long inventoryHintSum=productService.select("SELECT count(*) from Product pt ,Sku s where pt.id = s.productId and (s.inventory<pt.inventoryHint)", Long.class);
		model.addAttribute("inventoryHintSum", inventoryHintSum);
		
		//待审核议价//		
		Long quotationItemSum=quotationItemservice.select("SELECT count(*) from QuotationItem where quotationState='BE_ON_QUOTATION'", Long.class);
		model.addAttribute("quotationItemSum", quotationItemSum);			
		/****************************当前数据统计****************/
		//待审核企业数量
		Long auditCompanys = companyService.select(" select count(*) from Company where companySate ='INIT'",Long.class) ;
		model.addAttribute("auditCompanys", auditCompanys) ;
		//待确认订单
		Long initOrders = orderService.select("select count(*) from Order where orderState='INIT_STATE' ", Long.class) ;
		model.addAttribute("initOrders", initOrders) ;
		//待发货订单
		Long waitDeliveryOrders = orderService.select("select count(*) from Order where orderState='WAIT_DELIVERY' ", Long.class) ;
		model.addAttribute("waitDeliveryOrders", waitDeliveryOrders) ;
		//待退款售后单
		Long waitReturnMoney = afterMarketOrderService.select("select count(*) from AfterMarketOrder where orderState='TREASURER_AUDIT'", Long.class) ;
		model.addAttribute("waitReturnMoney", waitReturnMoney) ;
		
		/*******************当天数据统计******************/
		//新增企业
		String now = DateUtil.getNowSimple(DateUtil.COMMON_DATE) ;
		model.addAttribute("now",now);
		Long dayAddCompanys = companyService.select(" select count(*) from Company where regtime like '"+now+"%'",Long.class) ;
		model.addAttribute("dayAddCompanys", dayAddCompanys) ;
		//新增会员
		Long dayAddUsers = userInfoService.select(" select count(*) from UserInfo where createTime like '"+now+"%'",Long.class) ;
		model.addAttribute("dayAddUsers", dayAddUsers) ;
		//下单付款额
		BigDecimal dayOrderMoney = orderService.select(" select sum(total) from Order where createDate like '"+now+"%'",BigDecimal.class) ;
		model.addAttribute("dayOrderMoney", dayOrderMoney) ;
		//发货货品
		Long daySendTotals = sendGoodsService.select(" select sum(currSendNum) from SendGoods where sendTime like '"+now+"%'",Long.class) ;
		model.addAttribute("daySendTotals", daySendTotals) ;
		//累计收款金额
		BigDecimal dayReviceMoney = orderService.select(" select sum(cashFee) from OrderReceipt where payDate like '"+now+"%'",BigDecimal.class) ;
		model.addAttribute("dayReviceMoney", dayReviceMoney) ;
		
		//当天送出总积分
		Long scoreTotal =scoreLogService.select("select sum(score) from  ScoreLog where scoreType in ('MANUALLY_ADD','CONCERN ','BINDING ','COMMEND','COMMEND_PLACE_AN_ORDER ','BUY_GOODS ','EVALUATION ','EMALI_SUBMIT ','PHONE_SUBMIT ','FOLLOW_ON_WECHAT','SIGN_IN') and createDate like '"+now+"%'", Long.class);
		model.addAttribute("scoreTotal", scoreTotal) ;
		
		/*******************昨天数据统计******************/
		//新增企业
				String yesterday = DateUtil.plusDay2(-1)  ;
				
				Long ydayAddCompanys = companyService.select(" select count(*) from Company where regtime like '"+yesterday+"%'",Long.class) ;
				model.addAttribute("ydayAddCompanys", ydayAddCompanys) ;
				//新增会员
				Long ydayAddUsers = userInfoService.select(" select count(*) from UserInfo where createTime like '"+yesterday+"%'",Long.class) ;
				model.addAttribute("ydayAddUsers", ydayAddUsers) ;
				//下单付款额
				BigDecimal ydayOrderMoney = orderService.select(" select sum(total) from Order where createDate like '"+yesterday+"%'",BigDecimal.class) ;
				model.addAttribute("ydayOrderMoney", ydayOrderMoney) ;
				//发货货品
				Long ydaySendTotals = sendGoodsService.select(" select sum(currSendNum) from SendGoods where sendTime like '"+yesterday+"%'",Long.class) ;
				model.addAttribute("ydaySendTotals", ydaySendTotals) ;
				//累计收款金额
				BigDecimal ydayReviceMoney = orderService.select(" select sum(total) from Order where payDate like '"+yesterday+"%' and payState=1",BigDecimal.class) ;
				model.addAttribute("ydayReviceMoney", ydayReviceMoney) ;
				
				//昨天送出总积分
				Long yscoreTotal =scoreLogService.select("select sum(score) from  ScoreLog where scoreType in ('MANUALLY_ADD','CONCERN ','BINDING ','COMMEND','COMMEND_PLACE_AN_ORDER ','BUY_GOODS ','EVALUATION ','EMALI_SUBMIT ','PHONE_SUBMIT ','FOLLOW_ON_WECHAT','SIGN_IN') and createDate like '"+yesterday+"%'", Long.class);
				model.addAttribute("yscoreTotal", yscoreTotal) ;
				
				
				/*******************累计增加数据统计******************/
				//企业
				Long SumAddCompanys = companyService.select(" select count(*) from Company  ",Long.class) ;
				model.addAttribute("SumAddCompanys", SumAddCompanys) ;
				//新增会员
				Long SumdayAddUsers = userInfoService.select(" select count(*) from UserInfo ",Long.class) ;
				model.addAttribute("SumdayAddUsers", SumdayAddUsers) ;
				//下单付款额
				BigDecimal SumdayOrderMoney = orderService.select(" select sum(total) from Order",BigDecimal.class) ;
				model.addAttribute("SumdayOrderMoney", SumdayOrderMoney) ;
				//发货货品
				Long SumdaySendTotals = sendGoodsService.select(" select sum(currSendNum) from SendGoods ",Long.class) ;
				model.addAttribute("SumdaySendTotals", SumdaySendTotals) ;
				//累计收款金额
				BigDecimal SumdayReviceMoney = orderService.select(" select sum(total) from Order where  payState=1",BigDecimal.class) ;
				model.addAttribute("SumdayReviceMoney", SumdayReviceMoney) ;
				
				//总送出总积分
				Long SumscoreTotal =scoreLogService.select("select sum(score) from  ScoreLog where scoreType in ('MANUALLY_ADD','CONCERN ','BINDING ','COMMEND','COMMEND_PLACE_AN_ORDER ','BUY_GOODS ','EVALUATION ','EMALI_SUBMIT ','PHONE_SUBMIT ','FOLLOW_ON_WECHAT','SIGN_IN')", Long.class);
				model.addAttribute("SumscoreTotal", SumscoreTotal) ;
				
				//在售货品种类
				Long SumSort=skuService.select("select count(*) FROM Sku where flag='1' ORDER BY id ", Long.class);
				model.addAttribute("SumSort", SumSort);	
				
				//门店总数
				
				//String value=sysParamService.findParam("sysParamService", SysParamType.PLATFORM_PARAM).getValue();
				//model.addAttribute("value", value);
		return "index/indexList";
	}

	@RequestMapping(value = "load", method = RequestMethod.GET)
	public String receive(HttpServletRequest request, Model model) {

		return "index/receive";
	}
}
