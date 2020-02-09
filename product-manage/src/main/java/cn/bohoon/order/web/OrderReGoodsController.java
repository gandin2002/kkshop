package cn.bohoon.order.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
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

import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.order.entity.AfterMarketOrder;
import cn.bohoon.order.entity.AmOrderItem;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.service.AfterMarketOrderService;
import cn.bohoon.order.service.AmOrderItemService;
import cn.bohoon.order.service.OrderItemService;
import cn.bohoon.order.service.OrderService;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.stock.entity.WareHouse;
import cn.bohoon.stock.entity.WarehLocation;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.stock.service.WarehLocationService;
import cn.bohoon.stock.service.WarehouseTypeService;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.userInfo.service.ShippingInfoService;
import cn.bohoon.userInfo.service.UserInfoService;

/**
 * 退货单
 */
@Controller
@RequestMapping(value = "orderReGoods")
public class OrderReGoodsController {
	@Autowired
	AmOrderItemService amOrderItemService ;
	@Autowired
	AfterMarketOrderService afterMarketOrderService;
	@Autowired
	WarehouseTypeService WarehouseTypeService ;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	CompanyService companyService;
	@Autowired
	ProductService productService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	OrderService orderService;
	@Autowired
	ShippingInfoService shippingInfoService;
	@Autowired
	WarehLocationService warehLocationService;
	@Autowired
	SkuService skuService;
	@Autowired
	WareHouseService wareHouseService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	/**
	 * 退货单列表
	 * 
	 * @param request
	 * @param model
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model, AfterMarketOrder search) throws ParseException{
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		String wareHouserId =ServletRequestUtils.getStringParameter(request, "wareHouserId","");
		Page<AfterMarketOrder> page = new Page<AfterMarketOrder>();
		page.setPageNo(pageNo);
		String hql = "from AfterMarketOrder t where 1 =1   ";
		List<Object> params = new ArrayList<>();
		hql += "  and t.reOrderType in ('REFUNDANDGOODS','CHANGEGOODS','IN_REPAIR') " ;
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
		if(!StringUtils.isEmpty(wareHouserId)) {
			hql +=" and t.wareHouseId =?";
			params.add(Integer.parseInt(wareHouserId));
			model.addAttribute("wareHouserId", wareHouserId);
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
		String hql1=" from WareHouse where 1=1 ";
		List<WareHouse> WareHouseList=	wareHouseService.list(hql1);
		
		hql += " order by t.createDate desc ";
		page = afterMarketOrderService.page(page, hql, params.toArray());
		Map<AfterMarketOrder,WareHouse> Map = new HashMap<AfterMarketOrder,WareHouse>();
			List<AfterMarketOrder>	AOrderList=page.getResult();
			for (AfterMarketOrder afterMarketOrder : AOrderList) {
				WareHouse warehouse=wareHouseService.select("from WareHouse where id="+afterMarketOrder.getWareHouseId());
				Map.put(afterMarketOrder, warehouse);
			}
		BigDecimal f= new BigDecimal(0.00);
		List<AfterMarketOrder>	AfterMarketOrderList=afterMarketOrderService.list(hql,params.toArray());
		for (AfterMarketOrder afterMarketOrder : AfterMarketOrderList) {
			f=f.add(afterMarketOrder.getReFundFee());
		}
		model.addAttribute("WareHouseList",WareHouseList);
		model.addAttribute("pageOrder", page);
		model.addAttribute("warhouserMap", Map);
		model.addAttribute("search", search);
        model.addAttribute("f",f);
        return "orderReGoods/orderReGoodsList";
    }
	
	/**
	 * 退货单详情
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(Model model , String id) {
		AfterMarketOrder afterMarketOrder = afterMarketOrderService.get(id) ;
		
		String hql = " from AmOrderItem where amOrderId=? " ;
		List<AmOrderItem> aItems = amOrderItemService.list(hql,id) ;
		
		Map<AmOrderItem,Product> mapop=new HashMap<AmOrderItem,Product>();
		Map<AmOrderItem,Sku>  map = new HashMap<AmOrderItem,Sku>();
		List<String> userInfoList= new ArrayList<String>();
			for (AmOrderItem amOrderItem : aItems) {
				if(!StringUtils.isEmpty(amOrderItem)){
					userInfoList.add(userInfoService.get(amOrderItem.getUserId()).getNickname().toString());
					Sku sku=skuService.select("from Sku where id ="+amOrderItem.getSkuId());
					mapop.put(amOrderItem, productService.get(amOrderItem.getProductId()));
					map.put(amOrderItem, sku);
					model.addAttribute("mapop", mapop);
					model.addAttribute("map", map);
				}
			}
		
			
			
		String hql1=" from OrderItem  where  orderId=? " ;
		Order order	=orderService.get(afterMarketOrder.getOrderId());
		//List<OrderItem> itemList = orderItemService.list("from OrderItem o where o.orderId = ? order by o.id asc", order.getId());
		ShippingInfo shippingInfo=shippingInfoService.get(order.getShippingInfoId());
		OrderItem orderItem=orderItemService.select(hql1,order.getId());
		List<Product> productList =new ArrayList<Product>();
		if(!StringUtils.isEmpty(afterMarketOrder)){
			//WarehLocation  warehLocation=warehLocationService.get(afterMarketOrder.getWareHouseId());
			//model.addAttribute("warehLocation", warehLocation);
		}
		
		model.addAttribute("productList", productList);
		
		model.addAttribute("order", order);
		model.addAttribute("shippingInfo", shippingInfo);
		model.addAttribute("userInfoList", userInfoList);							
		model.addAttribute("orderItem", orderItem);
		model.addAttribute("aItems", aItems) ;
		model.addAttribute("amOrder", afterMarketOrder) ;
		return "orderReGoods/detail" ;
	}

}