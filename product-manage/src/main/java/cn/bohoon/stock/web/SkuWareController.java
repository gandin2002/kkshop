package cn.bohoon.stock.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.product.entity.SkuWare;
import cn.bohoon.product.service.SkuWareService;

@Controller
@RequestMapping(value = "skuWare")
public class SkuWareController {

	@Autowired
	SkuWareService skuWareService ;
	@Autowired
	OrderItemService orderItemService;
	
	@RequestMapping("getOrderItemSkuWareList")
	@ResponseBody
	public String getOrderItemSkuWareList(Integer wareHouseId,String orderId) {
		String hql = " from SkuWare where wareHouseId=?" ;
		List<SkuWare> skuWares = skuWareService.list(hql,wareHouseId) ;
		return JsonUtil.toJson(skuWares) ;
	}
}
