package cn.bohoon.order.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.order.domain.OrderType;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderBarter;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderRepaire;
import cn.bohoon.order.service.OrderBarterService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderRepaireService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductScore;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuScore;
import cn.bohoon.product.entity.SkuWare;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.ProductScoreService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuScoreService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareService;
import cn.bohoon.util.ConvertUtils;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "orderItem")
public class OrderItemController {

	@Autowired
	SkuService skuService ;
	@Autowired
    OrderService orderService;
	@Autowired
	SkuWareService skuWareService ;
	@Autowired
	ProductService productService ;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	OrderBarterService orderBarterService;
	@Autowired
	OrderRepaireService orderRepaireService;
	@Autowired
	ProductScoreService productScoreService;
	@Autowired
	SkuScoreService skuScoreService;
	@Autowired
	BrandService brandService;
	/**
	 * 订单货品列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String orderId = ServletRequestUtils.getStringParameter(request, "orderId", "");
		listModel(model,pageNo,orderId);
		Order order = orderService.get(orderId) ;
		model.addAttribute("order", order);
		return "orderItem/orderItemList";
	}
	
	/**
	 * 换货单货品列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list2", method = RequestMethod.GET)
	public String list2(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		OrderBarter obModel = orderBarterService.get(id) ;
		listModel(model,pageNo,obModel.getOrderId());
		model.addAttribute("obModel", obModel);
		return "orderItem/orderItemList2";
	}
	
	/**
	 * 维修单货品列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list3", method = RequestMethod.GET)
	public String list3(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		OrderRepaire orderRepaire = orderRepaireService.get(id) ;
		listModel(model,pageNo,orderRepaire.getOrderId());
		model.addAttribute("orderRepaire", orderRepaire);
		return "orderItem/orderItemList3";
	}
	
	
	public void listModel(Model model,Integer pageNo,String orderId) {
		Page<OrderItem> page = new Page<OrderItem>(5);
		page.setPageNo(pageNo);
        
		Order oder=orderService.get(orderId);
		page = orderItemService.page(page, "from OrderItem where orderId=?", orderId);
		
		Map<OrderItem,Object> productMap = new HashMap<OrderItem,Object>() ;
		Map<OrderItem,Object> skuWaresMap = new HashMap<OrderItem,Object>() ;
		Map<OrderItem,Object>	SkuMap = new HashMap<OrderItem,Object>();
		Map<Object,String> brandNameMap = new HashMap<Object,String>();
		for(OrderItem oi : page.getResult()) {
            //积分订单
			if(null!=oder&&oder.getOrderType().equals(OrderType.SCOREEXCHANGE)){
				ProductScore productScore = productScoreService.get(oi.getProductId()) ;
				productMap.put(oi,productScore);
				Brand brand = brandService.get(productScore.getBrandId());
				brandNameMap.put(productScore, brand.getName());
				SkuScore sku =skuScoreService.select("from SkuScore where id="+oi.getSkuId());
				SkuMap.put(oi, sku);
			}else{//非积分订单
				Product product = productService.get(oi.getProductId()) ;
				productMap.put(oi,product);
				Sku sku =skuService.select("from Sku where id="+oi.getSkuId());
				Brand brand = brandService.get(product.getBrandId());
				brandNameMap.put(product, brand.getName());
				SkuMap.put(oi, sku);
			}
			
			List<SkuWare> skuWares = skuWareService.list(" from SkuWare where skuId=?", ConvertUtils.parseInteger(oi.getSkuId())) ;
			skuWaresMap.put(oi, skuWares) ;
			
		}
		model.addAttribute("brandNameMap",brandNameMap);
		model.addAttribute("SkuMap", SkuMap);
		model.addAttribute("pageOrderItem", page);
		model.addAttribute("productMap", productMap);
		model.addAttribute("skuWaresMap", skuWaresMap);
	}
	
	
	

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		return "orderItem/orderItemAdd";
	}

	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, OrderItem orderItem) {
		orderItemService.save(orderItem);
		return ResultUtil.getSuccessMsg();
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		OrderItem orderItem = orderItemService.get(id);
		model.addAttribute("item", orderItem);
		return "orderItem/orderItemEdit";
	}

	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(OrderItem orderItem) {
		orderItemService.save(orderItem);
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 新增订单选项
	 * 
	 * @param request
	 * @param orderItem
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addNewItem", method = RequestMethod.POST)
	public String addNewItem(HttpServletRequest request, OrderItem orderItem) {
		Order order = orderService.get(orderItem.getOrderId()) ;
		Integer skuId = ConvertUtils.parseInteger(orderItem.getSkuId()) ;
		Sku sku = skuService.get(skuId) ;
		
		OrderItem oit = new OrderItem() ;
		oit.setCreateDate(new Date());
		oit.setMemberId(order.getMemberId());
		oit.setOrderId(order.getId());
		oit.setSkuId(sku.getId()+"");
		oit.setQuantity(orderItem.getQuantity());
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
		oit.setUnitName(sku.getProduct().getUnit().getName());
		orderItemService.save(oit);
		
		countNewOrder(orderItem.getOrderId()) ;
		
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 编辑数量
	 * 
	 * @param id
	 * @param quantity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "editQuantity")
	public String editQuantity(Integer id,Integer quantity) {
		orderItemService.execute(" update OrderItem set quantity=? where id=?", quantity,id) ;
		OrderItem orderItem = orderItemService.get(id) ;
		String orderId = null != orderItem ? orderItem.getOrderId() : "" ;
		countNewOrder(orderId) ;
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 从新计算订单
	 * @param orderId
	 */
	public void countNewOrder(String orderId) {
		if(!StringUtils.isEmpty(orderId)) {
			Order order = orderService.get(orderId) ;
			String hql = "from OrderItem where orderId=?" ;
			List<OrderItem> orderItems = orderItemService.list(hql, orderId) ;
			BigDecimal total = new BigDecimal(0) ;
			int totalNum = 0 ;
			for(OrderItem oit : orderItems) {
				BigDecimal price = oit.getPrice().multiply(new BigDecimal(oit.getQuantity())) ;
				total = total.add(price) ;
				totalNum += oit.getQuantity() ;
			}
			order.setProductFee(total);
			order.setTotal(total.add(order.getDeliverFee()));
			order.setTotalNum(totalNum);
			orderService.save(order);
		}
	}

	/**
	 * 删除
	 * @param request
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, Integer id) {
		OrderItem orderItem = orderItemService.get(id) ;
		String orderId = null != orderItem ? orderItem.getOrderId() : "" ;
		orderItemService.delete(id);
		countNewOrder(orderId) ;
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 获取sku 信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getSkuInfoSelect",method = RequestMethod.GET)
	public String getSkuInfoSelect(HttpServletRequest request){
		String skuCode = ServletRequestUtils.getStringParameter(request, "skuCode", "") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo",1) ;
		Page<Sku> skuPage = new Page<Sku>();
		skuPage.setPageNo(pageNo);
		String hql =" from Sku s  where 1=1 ";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(skuCode)){
			hql +=" and   s.code like ? ";
			params.add("%"+skuCode+"%");
		}
		skuPage = skuService.page(skuPage, hql,params.toArray());
		
		Page<Map<String,Object>> mapPage = new Page<Map<String,Object>>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>() ;
		for(Sku sku :skuPage.getResult() ) {
			Map<String,Object> skuMap = new HashMap<String,Object>() ;
			Product product = sku.getProduct() ;
			Category cate = null != product ? product.getCategory() :null ;
			Brand brand = null != product ? product.getBrand() : null ;
			if(!StringUtils.isEmpty(skuCode)){
				skuMap.put("id", sku.getCode()) ;
			}
			skuMap.put("skuId", sku.getId()) ;
			skuMap.put("name", product.getName()) ;
			skuMap.put("imageUrl", sku.getImageUrl()) ;
			skuMap.put("code", sku.getCode()) ;
			skuMap.put("attrName", sku.getAttrAndAttrValues()) ;
			skuMap.put("skuPrice", sku.getSkuPrice()) ;
			skuMap.put("barCode", sku.getBarCode()) ;
			skuMap.put("brandName", null != brand ? brand.getName() : "" ) ;
			skuMap.put("categoryName", null != cate ? cate.getName() : "" ) ;
			skuMap.put("tax", sku.getTax()) ;
			listMap.add(skuMap) ;
		}
		mapPage.setTotalCount(skuPage.getTotalCount());
		mapPage.setPageNo(skuPage.getPageNo());
		mapPage.setPageCount(skuPage.getPageCount());
		mapPage.setPageSize(skuPage.getPageSize());
		mapPage.setResult(listMap);
		
		return JsonUtil.toJson(mapPage);
	}

}