package cn.bohoon.order.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EnumType;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.order.domain.ReOrderType;
import cn.bohoon.order.domain.SettleWay;
import cn.bohoon.order.entity.AmOrderItem;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.OrderReceipt;
import cn.bohoon.order.entity.OrderRefund;
import cn.bohoon.order.service.AmOrderItemService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderRefundService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;

/**
 * 退款单
 */
@Controller
@RequestMapping(value = "orderRefund")
public class OrderRefundController {
	
	@Autowired
	OrderService orderService ;
	@Autowired
	OrderItemService orderItemService ;
	@Autowired
	OrderRefundService orderRefundService ;
	@Autowired
	AmOrderItemService amOrderItemService;
	
	@Autowired
	ProductService productService;
	@Autowired
	SkuService skuService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	
	/**
	 * 退款单列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 * @throws ServletRequestBindingException 
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model) throws ParseException, ServletRequestBindingException{
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
		String company = ServletRequestUtils.getStringParameter(request, "company", "");
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		String reOrderType=ServletRequestUtils.getStringParameter(request, "reOrderType","");
		String settleWay = ServletRequestUtils.getStringParameter(request, "settleWay", "");
		
        String hql = " from OrderRefund t where 1=1 " ;
        List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(id)) {
			hql += " and t.id=?";
			params.add(id);
			model.addAttribute("id", id);
		}
		if (!StringUtils.isEmpty(company)) {
			hql += " and t.company=?";
			params.add(company);
			model.addAttribute("company", company);
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
		if (!StringUtils.isEmpty(reOrderType)) {
			hql = hql + " and t.reOrderType =?";
			params.add(Enum.valueOf(ReOrderType.class,reOrderType));
			model.addAttribute("reOrderType", reOrderType);
		}
		 if(!StringUtils.isEmpty(settleWay)){
	        	hql= hql+(" and t.settleWay = ?");
	        	params.add(Enum.valueOf(SettleWay.class, settleWay));
	        	model.addAttribute("settleWay", settleWay);
	        }
		
		
		
        Page<OrderRefund> pageOrderRefund = new Page<OrderRefund>();
        
        pageOrderRefund.setPageNo(pageNo);
        pageOrderRefund = orderRefundService.page(pageOrderRefund, hql,params.toArray()) ;
        
     
        	BigDecimal sum = new BigDecimal(0.00);
        	List<OrderRefund> orderRefund= orderRefundService.list(hql.toString(), params.toArray());
        	for(OrderRefund or:orderRefund){
        		sum=sum.add(or.getRefundMoney());
        	}
        model.addAttribute("sum", sum);
        model.addAttribute("pageOrderRefund", pageOrderRefund);
        return "orderRefund/orderRefundList";
    }
	
	/**
	 * 退款单列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "listDetails", method = RequestMethod.GET)
    public String listDetails(HttpServletRequest request,Model model) throws ParseException{
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
		String company = ServletRequestUtils.getStringParameter(request, "company", "");
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		
        String hql = " from OrderRefund t where 1=1 " ;
        List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(id)) {
			hql += " and t.id=?";
			params.add(id);
			model.addAttribute("id", id);
		}
		if (!StringUtils.isEmpty(company)) {
			hql += " and t.company=?";
			params.add(company);
			model.addAttribute("company", company);
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
		
        Page<OrderRefund> pageOrderRefund = new Page<OrderRefund>();
        pageOrderRefund.setPageNo(pageNo);
        pageOrderRefund = orderRefundService.page(pageOrderRefund, hql,params.toArray()) ;
        model.addAttribute("pageOrderRefund", pageOrderRefund);
        return "orderRefund/orderRefundDetailsList";
    }
	
	/**
	 * 退款单明细
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(Model model,String id) throws ParseException{
        OrderRefund orderRefund = orderRefundService.get(id) ;
        Order order = orderService.select(" from Order where id = ? ",orderRefund.getOrderId());
        
        model.addAttribute("order", order);
        model.addAttribute("orderRefund", orderRefund);
        if(orderRefund.getReOrderType().equals(ReOrderType.CANCEL_ORDER)) {
        	String hql = " from OrderItem where orderId=?" ;
        	List<OrderItem> items = orderItemService.list(hql,order.getId()) ;
        	model.addAttribute("aItems", items);	
        } else {
        	String hql = " from AmOrderItem where amOrderId=?" ;
        	List<AmOrderItem> items = amOrderItemService.list(hql,orderRefund.getAmOrderId()) ;
        	List<String> productList =new ArrayList<String>();
        	Map<AmOrderItem,Sku> map = new HashMap<AmOrderItem,Sku>();
        	for (AmOrderItem amOrderItem : items) {
    			Product product = productService.get(amOrderItem.getProductId());
    			productList.add(product.getBrandName());
    			Sku sku=skuService.select("from Sku where id="+amOrderItem.getSkuId());
    			map.put(amOrderItem, sku);
        	}
        	model.addAttribute("map", map);
        	model.addAttribute("aItems", items);	
        	model.addAttribute("productList", productList);
        }
        return "orderRefund/orderRefundDetails";
    }
	
 

}