package cn.bohoon.order.web;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.company.domain.CreditType;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyBank;
import cn.bohoon.company.entity.Credit;
import cn.bohoon.company.service.CompanyBankService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.company.service.CreditService;
import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.UploadService;
import cn.bohoon.main.util.IDUtil;
import cn.bohoon.order.domain.CashWay;
import cn.bohoon.order.domain.OrderItmeExcel;
import cn.bohoon.order.domain.OrderReceiptExcel;
import cn.bohoon.order.domain.PayType;
import cn.bohoon.order.domain.Payway;
import cn.bohoon.order.domain.SettleWay;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderBarter;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderReceipt;
import cn.bohoon.order.entity.OrderRepaire;
import cn.bohoon.order.entity.SendGoods;
import cn.bohoon.order.service.OrderBarterService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderReceiptService;
import cn.bohoon.order.service.OrderRepaireService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.order.service.SendGoodsService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.userInfo.service.UserService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.util.SyncDataUtils;

@Controller
@RequestMapping(value = "orderReceipt")
public class OrderReceiptController {
	
	@Autowired
	OrderService orderService ;
	@Autowired
	SendGoodsService sendGoodsService;
	@Autowired
	OrderBarterService orderBarterService;
	@Autowired
	OrderReceiptService orderReceiptService;
	@Autowired
	OrderItemService orderItemService ;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	CompanyService companyService;
	@Autowired
	UploadService uploadService;
	@Autowired
	OrderRepaireService orderRepaireService;
	@Autowired
	ProductService productService;
	@Autowired
	CompanyBankService companyBankService;
	
	@Autowired
	UserService  userService;
	@Autowired
	SysParamService  sysParamService;
	@Autowired
	CreditService creditService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	/**
	 * 购买订单单据信息
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderBills", method = RequestMethod.GET)
	public String orderBills(HttpServletRequest request, Model model) {
		String orderId = ServletRequestUtils.getStringParameter(request, "orderId", "");
		initData(model,orderId);
		String sgsHql = " from SendGoods where orderId=?" ;
		List<SendGoods> sendgoods = sendGoodsService.list(sgsHql, orderId);
		model.addAttribute("sendgoods", sendgoods);
		return "orderReceipt/orderReceipt";
	}
	
	/**orderReceipt/list
	 * 换货单单据信息
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderBills2", method = RequestMethod.GET)
	public String orderBills2(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		OrderBarter obModel = orderBarterService.get(id) ;
		initData(model,obModel.getOrderId());
		model.addAttribute("obModel", obModel) ;
		String sgsHql = " from SendGoods where orderId=?" ;
		List<SendGoods> sendgoods = sendGoodsService.list(sgsHql, id);
		model.addAttribute("sendgoods", sendgoods);
		return "orderReceipt/orderReceipt2";
	}
	
	/**
	 * 维修单单据信息
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderBills3", method = RequestMethod.GET)
	public String orderBills3(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		OrderRepaire orderRepaire = orderRepaireService.get(id) ;
		initData(model,orderRepaire.getOrderId());
		model.addAttribute("orderRepaire", orderRepaire) ;
		String sgsHql = " from SendGoods where orderId=?" ;
		List<SendGoods> sendgoods = sendGoodsService.list(sgsHql, id);
		model.addAttribute("sendgoods", sendgoods);
		return "orderReceipt/orderReceipt3";
	}
	
	public void initData(Model model,String orderId) {
		String hql = "from OrderReceipt where orderId = ?" ;
		//收款单信息
		OrderReceipt receipt = orderReceiptService.select(hql, orderId);
		
		
		Order order = orderService.get(orderId) ;
		model.addAttribute("item", order) ;
		model.addAttribute("receipt", receipt);
		
	}
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model) throws ParseException{
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String nickname=ServletRequestUtils.getStringParameter(request,"nickname","");
        String id = ServletRequestUtils.getStringParameter(request, "id","").trim();
        String userInfoId = ServletRequestUtils.getStringParameter(request, "userInfoId", "").trim();
        String settleWay = ServletRequestUtils.getStringParameter(request, "settleWay", "");
        String startTime = ServletRequestUtils.getStringParameter(request, "startTime","").trim();
        String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
        
        Page<OrderReceipt> pageOrderReceipt=new Page<OrderReceipt>();
        pageOrderReceipt.setPageNo(pageNo); 

        StringBuilder hql = new StringBuilder("from OrderReceipt r where 1 = 1");
        List<Object> params = new ArrayList<>();
        
        if(!StringUtils.isEmpty(id)){
        	hql.append(" and r.id = ?");
        	params.add(id);
        	model.addAttribute("id", id);
        }
        if(!StringUtils.isEmpty(userInfoId)){
        	/*Integer userId = -1;
        	try{
        		userId = Integer.valueOf(userInfoId);
        	}catch(NumberFormatException e){
        		userId = -1;
        	}*/
        	hql.append(" and r.userInfoId = ?");
        	params.add(userInfoId);
        	model.addAttribute("userInfoId", userInfoId);
        }
        if(!StringUtils.isEmpty(settleWay)){
        	hql.append(" and r.settleWay = ?");
        	params.add(Enum.valueOf(SettleWay.class, settleWay));
        	model.addAttribute("settleWay", settleWay);
        }
        if(!StringUtils.isEmpty(startTime)){
        	hql.append(" and r.payDate >= ?");
        	params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
        	model.addAttribute("startTime", startTime);
        }
        if(!StringUtils.isEmpty(endTime)){
        	hql.append(" and r.payDate < ?");
        	params.add(DateUtil.getNDayAfter(endTime, 1));
        	model.addAttribute("endTime", endTime);
        }
        
        hql.append(" order by r.payDate desc");
        	
        pageOrderReceipt=orderReceiptService.page(pageOrderReceipt, hql.toString(),params.toArray());
       
        
        Map<OrderReceipt, UserInfo> userInfoMap = new HashMap<>();
        Map<OrderReceipt, Company> companyMap = new HashMap<>();
        
      
      
        for(OrderReceipt orderReceipt : pageOrderReceipt.getResult()){
        	if(!StringUtils.isEmpty(orderReceipt.getUserInfoId())){
        		userInfoMap.put(orderReceipt, userInfoService.get(orderReceipt.getUserInfoId()));
        	
        	}
        	
        	if(!StringUtils.isEmpty(orderReceipt.getCompanyId())){
        		companyMap.put(orderReceipt, companyService.get(orderReceipt.getCompanyId()));
        				
        	}
        }
   
        BigDecimal sum=new BigDecimal(0.00);
        //模糊查询OrderReceipt  List
        List<OrderReceipt> orderReceipt=orderReceiptService.list(hql.toString(), params.toArray());
        for(OrderReceipt or1:orderReceipt){
        	sum=sum.add(or1.getCashFee());
        	
        }
        
        
		model.addAttribute("sum", sum);
        model.addAttribute("pageOrderReceipt", pageOrderReceipt);
        model.addAttribute("userInfoMap", userInfoMap);
        model.addAttribute("companyMap", companyMap);
        return "orderReceipt/orderReceiptList";
    }
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		/*String hql = " from UserInfo where companyId='".concat(companyId).concat("' ") ;*/
		SysParam sysParam = sysParamService.findParam(SysParamHelper.BANK_DATA_LIST, SysParamType.BACKBANK_PARAM);
		
		List<Company> companyList=companyService.list();
		model.addAttribute("companyList", companyList);
		List<UserInfo>	userInfoList=userInfoService.list();
		model.addAttribute("userInfoList", userInfoList);
		model.addAttribute("sysParam", sysParam);
		return "orderReceipt/orderReceiptAdd";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Model model,OrderReceipt orderReceipt) throws Exception {
		
		
		String companyId = ServletRequestUtils.getStringParameter(request, "companyId", "");				 
		String payType = ServletRequestUtils.getStringParameter(request, "payType", "");
		String payway = ServletRequestUtils.getStringParameter(request, "payway", "");
		String settleWay = ServletRequestUtils.getStringParameter(request, "settleWay", "");
		String cashWay = ServletRequestUtils.getStringParameter(request, "cashWay", "");
		String nickname= ServletRequestUtils.getStringParameter(request, "nickname", "");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	
		MultipartFile file = multipartRequest.getFile("file");
	    
		//UserInfo UuserInfo=	userInfoService.getUserInfoByNickname(nickname);
		
		String hql=" from UserInfo where (realname='"+nickname+"' "+" or nickname='"+nickname+"')"+ " and  companyId='"+companyId+"'";
		UserInfo  UuserInfo= userInfoService.select(hql);
		//bigan   把企业新增的还款金额添加到企业信用余额
		  Company   company=companyService.get(companyId);
		  company.setCreditOver(company.getCreditOver().add(orderReceipt.getCashFee()));
		  if(company.getCredits().intValue()>0){
		     companyService.save(company); 
		    
		  }
		  //若成功添加一个还款账单则对应添加一个信用记录
		  if(null!=orderReceipt){
			  Credit  credi=new Credit();
			  credi.setCompanyId(companyId);
			  credi.setCorrelationTime(new Date());
			  credi.setCreditOver(orderReceipt.getCashFee());
			  credi.setCreditType(CreditType.ADD);
			  credi.setParticular(orderReceipt.getNote());
			  creditService.save(credi);
		  }
		//end
		
		if (!StringUtils.isEmpty(file)) {
			String image = uploadService.handleFileUploadNoDomain(file, "orderReceipt");
			orderReceipt.setAttachImage(image);
		}
		PayType pt = Enum.valueOf(PayType.class, payType) ;
	
	    orderReceipt.setUserInfoId(UuserInfo.getId());	
		orderReceipt.setId(IDUtil.getInstance().getId("OR"));
		orderReceipt.setPayType(pt);
		orderReceipt.setPayway(Enum.valueOf(Payway.class, payway));
		orderReceipt.setSettleWay(StringUtils.isEmpty(settleWay) ? SettleWay._DEFAULT : Enum.valueOf(SettleWay.class, settleWay));
		orderReceipt.setCashWay(StringUtils.isEmpty(cashWay) ? CashWay._DEFAULT : Enum.valueOf(CashWay.class, cashWay));
		orderReceipt.setStatus(true);
	 //companyService.select("from Company c where c.name", values)
		if(PayType.CREDIT.equals(pt)) {
//			Company company = companyService.get(orderReceipt.getCompanyId()) ;
//			if(null != company1 ) {
//				BigDecimal co = company1.getCreditOver() ;
//				company1.setCreditOver(co.add(orderReceipt.getCashFee()));
//				companyService.save(company1);
//			}
		}
		if(!StringUtils.isEmpty(orderReceipt.getOrderId())){
			Order order = orderService.get(orderReceipt.getOrderId());
			if(!StringUtils.isEmpty(order)){
				orderReceipt.setUserInfoId(UuserInfo.getId());
				orderReceipt.setOrderFee(order.getTotal());
				orderReceipt.setOrderUserName(nickname);
				order.setPayDate(orderReceipt.getPayDate());
				order.setPayState(orderReceipt.getStatus());
				order.setPayway(orderReceipt.getPayway());
				orderService.update(order);
			}
		}

		orderReceiptService.save(orderReceipt);
//		SyncDataUtils.syncSoleDate(orderReceipt.getUserInfoId(),"/syncData/addReceipt",orderReceipt.getOrderId(), orderReceipt.getId());
		//companyBankService.select(" from CompanyBank c where c.companyId= ?",null );
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "订单收款:id"+orderReceipt.getId());
		return ResultUtil.getSuccessMsg();
	}
	
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String addDetail(HttpServletRequest request, Model model,String id) {
		OrderReceipt orderReceipt = orderReceiptService.get(id);
		Company company = companyService.get(orderReceipt.getCompanyId());
					
		List<OrderItem> itemList = orderItemService.list("from OrderItem o where o.orderId = ? order by o.id asc", orderReceipt.getOrderId());
		List<String> productList= new ArrayList<String>();
		for (OrderItem productString : itemList) {
			Product product = productService.get(productString.getProductId());
			productList.add(product.getBrandName());
		}
		UserInfo userInfo=	userInfoService.getUserInfoByNickname(orderReceipt.getOrderUserName());
							
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("productList", productList);
		model.addAttribute("item", orderReceipt);
		model.addAttribute("itemCompany", company);
		model.addAttribute("itemList", itemList);
		return "orderReceipt/orderReceiptDetail";
	}
	
	@ResponseBody
	@RequestMapping(value = "orderList", method = RequestMethod.POST)
	public String orderListPost(HttpServletRequest request, Model model) {
		Integer companyId = ServletRequestUtils.getIntParameter(request, "companyId", -1);
		//System.out.println("公司id"+companyId);
		List<Order> orderList = orderService.list("from Order o where o.memberId =?", companyId.toString());
		
		Map<String, String> map = new HashMap<>();
		for(Order order : orderList){
			map.put(order.getId(), order.getId()+" "+order.getTotal()+" "+order.getCompany());
		}
		
		return JsonUtil.toJson(map);
	}
	
	
	//用ajax根据公司id显示下面的银行和用户名
	@ResponseBody
	@RequestMapping(value = "autodate", method = RequestMethod.POST)
	public String getDate(HttpServletRequest request, Model model) {
		String companyId=ServletRequestUtils.getStringParameter(request,"companyId","");
		//查询该公司所有会员
		String hql = " from UserInfo where companyId='".concat(companyId).concat("' ") ;
		
		List<UserInfo> userInfoList = userInfoService.list(hql);
		//String  hql2="from CompanyBank　companyId'"+companyId+"'";
		//找出相应的企业银行
	
		
		//查询 出用户登录表信息
		/*String hql3=" from User where userInfoId='".concat(companyId).concat("' ");
		List<User> userList = userService.list(hql3);*/
		//List<String> userNameList=new LinkedList<>();
		/*for(UserInfo userinfo:userInfoList){
			System.out.println("呢次"+userinfo.getNickname().length());
			if(userinfo.getNickname()!=null && userinfo.getNickname().length()>0){
				userNameList.add(userinfo.getNickname());
			}else{
				User userById = userService.getUserById(userinfo.getId());
				userNameList.add(userById.getUsername());
				System.out.println("用户名"+userById.getUsername());
			}
		}*/
		
		/*Map<List<UserInfo>,List<CompanyBank>>  map=new HashMap<List<UserInfo>, List<CompanyBank>>();
	     map.put(userInfoList, bankList);*/
		//System.out.println("大小"+userNameList.size()+"公司个数"+bankList.size());
		//System.out.println("键值对"+map.get(userInfoList)+bankList.size()+"用户个数"+userInfoList.size()+"公司号"+companyId);
		
		return  JsonUtil.toJson(userInfoList);
	}
	
	//根据companyId获取相关联的银行账户
	@ResponseBody
	@RequestMapping(value = "BandList", method = RequestMethod.POST)
	public String getBandListById(HttpServletRequest request) {
		String companyId=ServletRequestUtils.getStringParameter(request,"companyId","");
		String hql2 = " from CompanyBank where companyId= ? " ;
		List<CompanyBank> bankList = companyBankService.list(hql2,companyId);
	
		return  JsonUtil.toJson(bankList);
	}
	
	@RequestMapping(value = "attachImage", method = RequestMethod.GET)
	public String attachImageGet(HttpServletRequest request, Model model,String attachImage) {
		model.addAttribute("attachImage", attachImage);		
		return "orderReceipt/attachImage";
	}
	
	/**
	 * 金额总计
	 *//*
	@ResponseBody
	@RequestMapping(value ="ordertotalMoney", method=RequestMethod.POST)
	public String orderListTotalMoney(HttpServletRequest request,Model model){
		
		return null;
	}*/
	
	
	
	
	
	/**
	 * 导出收款单列表 
	 * @param rId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "exportExcel", method = RequestMethod.POST)
	public @ResponseBody void exportExcel(String[] rId, HttpServletResponse response,HttpServletRequest request) throws Exception {
		if(ArrayUtils.isEmpty(rId)){
			return ;
		}
		List<OrderReceiptExcel> list = new ArrayList<>();
		for (String string : rId) {
			OrderReceipt orderReceipt = orderReceiptService.get(string);
			String companyName = "";
			if(!StringUtils.isEmpty(orderReceipt.getCompanyId())){
				companyName = companyService.get(orderReceipt.getCompanyId()).getName();
			}
			
			OrderReceiptExcel orderReceiptExcel = new OrderReceiptExcel(orderReceipt.getId(), orderReceipt.getOrderId(),
					orderReceipt.getOrderFee(), orderReceipt.getSettleWay().getName(), orderReceipt.getUserInfoId(),
					companyName, orderReceipt.getTimeString());
			list.add(orderReceiptExcel);
		}
	    String filename = URLEncoder.encode("收款单", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename="+filename+System.currentTimeMillis()+".xlsx"); //名字加时间戳
		ServletOutputStream sos =  response.getOutputStream();
		ExcelWrite.writeExcel(sos, list);
		sos.close();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导出收款单");
	}
	
	/**
	 * 导出收款单商品详细
	 * @param rId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "exportOrderExcel", method = RequestMethod.POST)
	public @ResponseBody void exportOrderExcel(String rId, HttpServletResponse response,HttpServletRequest request) throws Exception{
		ProductService productService = SpringContextHolder.getBean(ProductService.class);
		OrderReceipt orderReceipt =orderReceiptService.get(rId);
		List<OrderItem> orderItemList = orderItemService.list(" from OrderItem where orderId = ? ", orderReceipt.getOrderId());
		List<OrderItmeExcel> list = new ArrayList<>();
		for (OrderItem orderItem : orderItemList) {
			Product product = productService.get(orderItem.getProductId());
			OrderItmeExcel orderItmeExcel = new OrderItmeExcel(orderItem.getSkuCode(), product.getName(),
					product.getUnitName(), product.getBrandName(), orderItem.getPrice(), orderItem.getScore(),
					orderItem.getQuantity());
			list.add(orderItmeExcel);
		}
		String filename = URLEncoder.encode("收款单商品详情", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment; filename="+filename+rId+".xlsx"); //名字加时间戳
		ServletOutputStream sos =  response.getOutputStream();
		ExcelWrite.writeExcel(sos, list);
		sos.close();
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导出收款单商品详细");
		
	}


}