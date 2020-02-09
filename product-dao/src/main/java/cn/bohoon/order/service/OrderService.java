package cn.bohoon.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CreditFlow;
import cn.bohoon.company.service.CompanyDepartmentService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.company.service.CreditFlowService;
import cn.bohoon.distribution.entity.ExpfeeTemplate;
import cn.bohoon.distribution.service.ExpfeeTemplateService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderDao;
import cn.bohoon.order.domain.OrderCheckState;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.domain.OrderType;
import cn.bohoon.order.domain.SettleWay;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderLog;
import cn.bohoon.order.entity.OrderReceipt;
import cn.bohoon.order.entity.ShoppingCart;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuScore;
import cn.bohoon.product.entity.TaxCode;
import cn.bohoon.product.entity.Unit;
import cn.bohoon.product.service.ProductScoreService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuScoreService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.TaxCodeService;
import cn.bohoon.userInfo.domain.LoginUser;
import cn.bohoon.userInfo.domain.ScoreType;
import cn.bohoon.userInfo.entity.ScoreLog;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.ScoreLogService;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ConvertUtils;
import cn.bohoon.util.ExpFeeUtil;
import cn.bohoon.util.UserSession;

@Service
@Transactional
public class OrderService extends BaseService<Order,String>{
 
	@Autowired
	OrderDao orderDao;
	@Autowired
	SkuService skuService ;
	@Autowired
	SkuScoreService skuScoreService ;
	@Autowired
	ProductScoreService productScoreService ;
	@Autowired
	ProductService productService ;
	@Autowired
	UserInfoService userInfoService ;
	@Autowired
	CompanyService companyService ;
	@Autowired
	ScoreLogService scoreLogService ;
	@Autowired
	OrderLogService orderLogService ;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	CreditFlowService creditFlowService ;
	@Autowired
	OrderReceiptService orderReceiptService ;
	@Autowired
	ShoppingCartService shoppingCartService ;
	@Autowired
	TaxCodeService taxCodeService;
	@Autowired
	ExpfeeTemplateService expfeeTemplateService;
	@Autowired
	CompanyDepartmentService companyDepartmentService;
	
	List<Order> orderList=null;
	
	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	public List<Order> getOrderList() {
		return orderList;
	}

	@Autowired
	OrderService(OrderDao orderDao){
		super(orderDao);
	}

	/**
	 * 下订单
	 * 
	 * @param order
	 * @param carts
	 * @return
	 * @throws Exception
	 */
	public List<OrderItem> createOrder(Order order, List<ShoppingCart> carts) {
		List<OrderItem> list = new ArrayList<OrderItem>();
		BigDecimal totalPrice = new BigDecimal(0) ; //订单总金额
		BigDecimal deliverFee = new BigDecimal(15) ; //运费
		BigDecimal couponReduction = new BigDecimal(0) ;
		BigDecimal totalWeight = new BigDecimal(0); // 总重量
		BigDecimal totalVolume = new BigDecimal(0); // 总体积
		BigDecimal totaloTax = new BigDecimal(0);//总税金
		BigDecimal afterTax = new BigDecimal(0);//税后金额

		int totalNum = 0 ;
		Integer rowNo = 1;

		for (ShoppingCart cart : carts) {
			Sku sku = skuService.get(cart.getSkuId()) ;
			BigDecimal price = sku.getVipPrice(order.getUserId()) ;
			BigDecimal marketPrice = sku.getSkuPrice().subtract(price) ;
			Integer quantity = cart.getQuantity() ;

			Integer num = cart.getQuantity()/sku.getTranslateRate();
			//后台判断是否在下单时减库存
			skuService.subInventory(num, cart.getSkuId(), "0", 0);
			BigDecimal _item_price = price.multiply(new BigDecimal(num)) ;
			//实际金额
			OrderItem orderItem = makeItem(quantity,sku,order);
			orderItem.setRowNo(rowNo++);
			orderItem.setPrice(price);
			orderItem.setSkuErpCode(sku.getErpCode());
			if(!StringUtils.isEmpty(sku.getTaxId())){
				TaxCode tax = taxCodeService.select(" from TaxCode where id = ?",sku.getTaxId());
				orderItem.setTax(tax.getTax());
				orderItem.setTaxId(tax.getId());
				if(!StringUtils.isEmpty(sku.getTaxCode())){
					orderItem.setErpTaxId(tax.getErpCode());
				}
				BigDecimal oAmountWithTax = orderItem.getPrice().multiply(new BigDecimal(num));//总金额 含税金额

				BigDecimal oAmount = oAmountWithTax.divide(new BigDecimal(orderItem.getTax()*0.01+1),2,BigDecimal.ROUND_HALF_UP);//不含税金额
				
				BigDecimal 	otax = oAmountWithTax.subtract(oAmount);//税金

				orderItem.setoAmountWithTax(oAmountWithTax);
				orderItem.setoAmount(oAmount);
				orderItem.setOtax(otax);
				totaloTax = totaloTax.add(otax);
			}
			Integer saleQuantity= num;

			orderItem.setSaleQuantity(saleQuantity);
			orderItem.setSaleUnitName(sku.getAuxUnitName());
			//			orderItem.setSkuCode(sku.getCode());
			orderItem.setUnitCode(sku.getErpUnitCode());
			orderItemService.save(orderItem);
			totalPrice = totalPrice.add(_item_price) ;
			totalWeight = totalWeight.add(sku.getWeight().multiply(new BigDecimal(num))) ;
			totalVolume = totalVolume.add(sku.getVolume().multiply(new BigDecimal(num))) ;
			couponReduction = couponReduction.add(marketPrice.multiply(new BigDecimal(num))) ;
			list.add(orderItem);
			totalNum += quantity ;
			if(!StringUtils.isEmpty(cart.getId())) {
				Integer cartId = cart.getId() ;
				shoppingCartService.execute(" delete from ShoppingCart where id=?", cartId) ;
			}

		}
		afterTax = totalPrice.subtract(totaloTax);
		//运费计算
		ExpfeeTemplate template = expfeeTemplateService.getSet();

		String userId = order.getUserId();
		ShippingInfo sif = order.getShippingInfoModel();

		if (null != template) {
			if (template.getCalcTye() == 0) {
				deliverFee = ExpFeeUtil.calcByPrice(totalPrice,userId, template);
			}else if (template.getCalcTye() == 1) {

				if (template.getCoefficient()!=null&&template.getCoefficient().doubleValue()!=0) {
					//先换算体积得到体重
					Double dou = totalVolume.doubleValue()/template.getCoefficient().doubleValue();
					//判断 谁重则取谁为体重计算
					Double wg=dou>=totalWeight.doubleValue()?dou:totalWeight.doubleValue();
					BigDecimal weight1=BigDecimal.valueOf(wg);
					//使用重量运费计算方法
					deliverFee=ExpFeeUtil.calcByTemplate(sif,weight1,userId, template);
				}else {
					//使用重量计算
					deliverFee=ExpFeeUtil.calcByTemplate(sif,totalWeight,userId, template);
				}
			}
		} else {
			deliverFee = new BigDecimal(15);

		}
		if (!list.isEmpty()) {
			order.setAfterTax(afterTax);
			order.setoTax(totaloTax);
			order.setProductFee(totalPrice);
			order.setDeliverFee(deliverFee);
			order.setTotalWeight(totalWeight);
			order.setTotalVolume(totalVolume);
			order.setTotalNum(totalNum);
			order.setCouponReduction(couponReduction);
			order.setTotal(totalPrice.add(deliverFee));
			this.save(order);
		}
		return list ;
	}

	/**
	 * 创建积分订单
	 * 
	 * @param order
	 * @param carts
	 * @return
	 * @throws Exception
	 */
	public List<OrderItem> createScoreOrder(Order order, SkuScore sku ,Integer quantity) {
		List<OrderItem> list = new ArrayList<OrderItem>();
		BigDecimal totalPrice = new BigDecimal(0) ; //订单总金额
		BigDecimal deliverFee = new BigDecimal(15) ; //运费
		BigDecimal couponReduction = new BigDecimal(0) ;
		BigDecimal totalWeight = new BigDecimal(0); // 总重量
		BigDecimal totalVolume = new BigDecimal(0); // 总体积

		int totalNum = 0 ;
		Integer needScore = 0 ;

		BigDecimal price = sku.getSkuPrice() ;
		BigDecimal marketPrice = sku.getSkuPrice().subtract(price) ;
		BigDecimal _item_price = price.multiply(new BigDecimal(quantity)) ;
		//实际金额
		OrderItem orderItem = makeItem(quantity,sku,order) ;
		orderItem.setPrice(price);
		orderItem.setSaleQuantity(quantity);
		orderItem.setSaleUnitName(sku.getAuxUnitName());
		orderItemService.save(orderItem);
		totalPrice = totalPrice.add(_item_price) ;
		totalWeight = totalWeight.add(sku.getWeight().multiply(new BigDecimal(quantity))) ;
		totalVolume = totalVolume.add(sku.getVolume().multiply(new BigDecimal(quantity))) ;
		couponReduction = couponReduction.add(marketPrice.multiply(new BigDecimal(quantity))) ;
		needScore += sku.getScore()* quantity ;
		list.add(orderItem);
		totalNum=quantity ;

		//运费计算
		LoginUser loginUser = UserSession.getLoginUser();
		ExpfeeTemplate template = expfeeTemplateService.getSet();

		ShippingInfo sif = order.getShippingInfoModel();
		if (null != template) {
			if (template.getCalcTye() == 0) {
				deliverFee = ExpFeeUtil.calcByPrice(totalPrice, loginUser.getUserId(), template);
			}else if (template.getCalcTye() == 1) {

				if (template.getCoefficient()!=null&&template.getCoefficient().doubleValue()!=0) {
					//先换算体积得到体重
					Double dou = totalVolume.doubleValue()/template.getCoefficient().doubleValue();
					//判断 谁重则取谁为体重计算
					Double wg=dou>=totalWeight.doubleValue()?dou:totalWeight.doubleValue();
					BigDecimal weight1=BigDecimal.valueOf(wg);
					//使用重量运费计算方法
					deliverFee=ExpFeeUtil.calcByTemplate(sif,weight1, loginUser.getUserId(), template);
				}else {
					//使用重量计算
					deliverFee=ExpFeeUtil.calcByTemplate(sif,totalWeight, loginUser.getUserId(), template);
				}
			}
		} else {
			deliverFee = new BigDecimal(15);

		}
		if (!list.isEmpty()) {
			order.setProductFee(totalPrice);
			order.setDeliverFee(deliverFee);
			order.setTotalWeight(totalWeight);
			order.setTotalVolume(totalVolume);
			order.setTotalNum(totalNum);
			order.setCouponReduction(couponReduction);
			order.setTotal(totalPrice.add(deliverFee));
			order.setNeedScore(needScore);
			this.save(order);
		}
		return list ;
	}

	/**
	 * 取消订单
	 * 
	 * @param username
	 * @param note
	 */
	public void cancelOrder(Order order, String username, String note) throws Exception {
		order.setOrderState(OrderState.CANCEL);
		orderDao.save(order);
		orderLogService.save(username, order, OrderCheckState.CONFIRM_CANCEL, note);
	}

	/**
	 * 信用支付
	 * 
	 * @param order
	 * @param company
	 */
	public void creditPay(Order order, Company company) {
		orderDao.save(order);
		List<OrderItem> orderItems = orderItemService.list(" from OrderItem where orderId =?",order.getId()) ;

		// 订单的总的积分
		//int totalScore = 0 ;
		for(OrderItem orderItem : orderItems ) {
			productService.execute(" update Product set salesNum=salesNum+? where id=? ", orderItem.getQuantity() ,orderItem.getProductId()) ;
			Integer score = orderItem.getScore() ;
			score = null != score?score:0 ;

			// 总金额乘以积分百分比
			//totalScore += Math.round(score  * (orderItem.getoAmountWithTax().doubleValue() / 100 ));
			//			System.out.println(Math.round(score  * (orderItem.getoAmountWithTax().doubleValue() / 100 )));

			CreditFlow cf = new CreditFlow(orderItem, company) ;
			creditFlowService.save(cf);
		}
		/*//调整积分
		userInfoService.execute("update UserInfo set score=score+? where id=?", totalScore,order.getUserId()) ;
		ScoreLog sl = new ScoreLog() ;
		sl.setMemberId(order.getUserId());
		sl.setFlag(true);
		sl.setOrderId(order.getId());
		sl.setScoreType(ScoreType.BUY_GOODS);

		// 在这里判断公式
		sl.setScore(totalScore);
		sl.setNote("订单支付，获取积分！");
		scoreLogService.save(sl);*/

		companyService.save(company);
	}

	/**
	 * 积分支付
	 * 
	 * @param order
	 * @param company
	 */
	public void orderPayByScore(Order order, UserInfo userInfo) {
		orderDao.save(order);
		userInfoService.save(userInfo) ;
		List<OrderItem> orderItem= new ArrayList<OrderItem>();
		orderItem=orderItemService.list("from OrderItem where orderId="+order.getId());
		for (OrderItem orderItem2 : orderItem) {
			Sku sku=skuService.get(ConvertUtils.parseInteger(orderItem2.getSkuId()));
			Integer num = orderItem2.getQuantity()/sku.getTranslateRate();
			skuService.subInventory(num, sku.getId(),"2", 0);
		}
		
		ScoreLog sl = new ScoreLog() ;
		sl.setMemberId(order.getUserId());
		sl.setFlag(true);
		sl.setOrderId(order.getId());
		sl.setScoreType(ScoreType.BUY_GOODS);
		sl.setScore(userInfo.getScore());
		sl.setNote("订单积分兑换，消费积分！");
		scoreLogService.save(sl);
	}


	/**
	 * 构造清单
	 * 
	 * @param quantity
	 * @param sku
	 * @param order
	 * @return
	 */
	private OrderItem makeItem(Integer quantity, Sku sku, Order order) {
		OrderItem oit = new OrderItem() ;
		oit.setCreateDate(new Date());
		oit.setMemberId(order.getMemberId());
		oit.setOrderId(order.getId());
		oit.setSkuId(sku.getId()+"");
		oit.setQuantity(quantity);
		oit.setProductId(sku.getProductId());
		oit.setProductName(sku.getProduct().getName());
		oit.setAttrAndAttrValues(sku.getAttrAndAttrValues());
		oit.setSkuCode(sku.getCode());
		oit.setUserId(order.getUserId());
		oit.setUsername(order.getUsername());
		oit.setStorageOut(false);
		oit.setSkuImage(sku.getImageUrl());
		oit.setPrice(sku.getVipPrice(order.getUserId()));
		oit.setVolume(sku.getVolume());
		oit.setWeight(sku.getWeight());
		oit.setScore(sku.getScore());
		oit.setOrdinaryPrice(sku.getProduct().getDisplayPrice());
		Product prdocut = sku.getProduct();
		Unit unit = prdocut.getUnit();
		if(unit != null){
			oit.setUnitName(unit.getName());
		}
		oit.setTax(sku.getTax());
		return oit;
	}

	/**
	 * 构造积分清单
	 * 
	 * @param quantity
	 * @param sku
	 * @param order
	 * @return
	 */
	private OrderItem makeItem(Integer quantity, SkuScore sku, Order order) {
		OrderItem oit = new OrderItem() ;
		oit.setCreateDate(new Date());
		oit.setMemberId(order.getMemberId());
		oit.setOrderId(order.getId());
		oit.setSkuId(sku.getId()+"");
		oit.setQuantity(sku.getTranslateRate());
		oit.setProductId(sku.getProductId());
		oit.setProductName(sku.getProduct().getName());
		oit.setAttrAndAttrValues(sku.getAttrAndAttrValues());
		oit.setSkuCode(sku.getCode());
		oit.setUserId(order.getUserId());
		oit.setUsername(order.getUsername());
		oit.setStorageOut(false);
		oit.setSkuImage(sku.getImageUrl());
		oit.setPrice(sku.getSkuPrice());
		oit.setVolume(sku.getVolume());
		oit.setWeight(sku.getWeight());
		oit.setScore(sku.getScore());
		oit.setOrdinaryPrice(sku.getProduct().getDisplayPrice());
		oit.setUnitName(sku.getProduct().getUnit().getName());
		oit.setTax(sku.getTax());
		return oit;
	}

	/**
	 * 用户付款成功 
	 * 
	 * @param order
	 * @param orderLog
	 */
	public synchronized void paySuccess(Order order, OrderLog orderLog) {
		order.setPayState(true);
		order.setOrderState(OrderState.WAIT_DELIVERY);
		order.setPayDate(new Date());
		orderDao.save(order) ;
		orderLogService.save(orderLog) ;

		List<OrderItem> orderItems = orderItemService.list(" from OrderItem where orderId =?",order.getId()) ;
		int totalScore = 0 ;
		Company company = new Company() ;
		company.setId(order.getMemberId());
		if(OrderType.SHOPPING.equals(order.getOrderType())) {
			for(OrderItem orderItem : orderItems ) {
				productService.execute(" update Product set salesNum=salesNum+? where id=? ", orderItem.getQuantity() ,orderItem.getProductId()) ;
				Integer score = orderItem.getScore() ;
				score = null != score?score:0 ;
				totalScore += score ;
				CreditFlow cf = new CreditFlow(orderItem, company) ;
				creditFlowService.save(cf);
			}
			/*减库存*/
			List<OrderItem> orderItem= new ArrayList<OrderItem>();
			orderItem=orderItemService.list("from OrderItem where orderId="+order.getId());
			for (OrderItem orderItem2 : orderItem) {
				Sku sku=skuService.get(ConvertUtils.parseInteger(orderItem2.getSkuId()));
				Integer num = orderItem2.getQuantity()/sku.getTranslateRate();
				skuService.subInventory(num, sku.getId(),"2", 0);
			}
			
			//调整积分
			userInfoService.execute("update UserInfo set score=score+? where id=?", totalScore,order.getUserId()) ;
		} else {
			totalScore = order.getNeedScore() ;
			userInfoService.execute("update UserInfo set score=score-? where id=?", totalScore,order.getUserId()) ;
		}
		ScoreLog sl = new ScoreLog() ;
		sl.setMemberId(order.getUserId());
		sl.setFlag(true);
		sl.setOrderId(order.getId());
		sl.setScoreType(ScoreType.BUY_GOODS);
		sl.setScore(totalScore);
		sl.setNote("订单支付，消费积分！");
		scoreLogService.save(sl);

	}

	/**
	 * 完成订单 	
	 * @param order
	 * @param or
	 */
	public void finish(Order order, OrderReceipt or) {
		orderDao.save(order) ;
		if (!(or.getSettleWay().equals(SettleWay.CREDIT))){

			orderReceiptService.save(or);
		}
	}

	/**
	 * 完成订单--信用支付，只用保存完成订单即可，不用保存收款明细
	 */
	public void save(Order order){
		orderDao.save(order);
	}
	
	
	public void subint(){
		
	}
	
	/**
	 * 通过部门id 获取上一级部门的所有审核通过订单
	public List<Order>  getOrder(String deptId){
		
		
		List<Order> list = new ArrayList<Order>();
		
		
		// 先获取本部门的订单
		
		
		
		String hql = "from CompanyDepartment where pid = ?";
		
		List<CompanyDepartment> depts = companyDepartmentService.list(hql,deptId);
		
		
		
		
		return null;
		
	}
	 */

}
