package cn.bohoon.order.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.order.entity.AmOrderItem;
import cn.bohoon.order.service.AmOrderItemService;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.util.ConvertUtils;

@Controller
@RequestMapping(value = "amOrderItem")
public class AmOrderItemController {

	@Autowired
	SkuService skuService ;
	@Autowired
	AmOrderItemService amOrderItemService ;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws Exception {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String amOrderId = ServletRequestUtils.getStringParameter(request, "amOrderId", "");
		Page<AmOrderItem> page = new Page<AmOrderItem>();
		page.setPageNo(pageNo);
		String hql = "from AmOrderItem t where amOrderId =? ";

		page = amOrderItemService.page(page, hql,amOrderId);
		Map<AmOrderItem,Sku> skuMap = new HashMap<AmOrderItem,Sku>() ;
		for(AmOrderItem item : page.getResult()) {
			Sku sku = skuService.get(ConvertUtils.parseInteger(item.getSkuId())) ;
			skuMap.put(item, sku) ;
		}
		model.addAttribute("itemPage", page);
		model.addAttribute("skuMap", skuMap);
		model.addAttribute("amOrderId", amOrderId);
		
		return "aftermarketOrder/amOrderItemList";
	}
}
