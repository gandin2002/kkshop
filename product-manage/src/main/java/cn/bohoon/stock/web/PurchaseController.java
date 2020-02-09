package cn.bohoon.stock.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.bohoon.excel.util.ExcelRead;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.domain.SkuExcelModel;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuWare;
import cn.bohoon.product.entity.SkuWareLocation;
import cn.bohoon.product.entity.Unit;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareLocationService;
import cn.bohoon.product.service.SkuWareService;
import cn.bohoon.stock.domain.Purch;
import cn.bohoon.stock.domain.PurchaseState;
import cn.bohoon.stock.entity.Purchase;
import cn.bohoon.stock.entity.PurchaseItem;
import cn.bohoon.stock.service.PurchaseItemService;
import cn.bohoon.stock.service.PurchaseService;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.util.ResultUtil;

/**
 * 货品库位管理
 * @author dujianqiao
 *
 */
@Controller
@RequestMapping(value = "purchase")
public class PurchaseController {
	@Autowired
	SkuService skuService ;
	@Autowired
	SkuWareService skuWareService ;
	@Autowired
	PurchaseService purchaseService ;
	@Autowired
	WareHouseService wareHouseService ;
	@Autowired
	PurchaseItemService purchaseItemService ;
	@Autowired
	SkuWareLocationService skuWareLocationService ;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	
	/**
	 * 采购单列表
	 * 
	 * @param request
	 * @param model
	 * @param corporateName
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id","") ;
		String operator = ServletRequestUtils.getStringParameter(request, "operator","") ;
		String supplierName = ServletRequestUtils.getStringParameter(request, "supplierName","") ;
		String wareHouseName = ServletRequestUtils.getStringParameter(request, "wareHouseName","") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<Purchase> _thisPage = new Page<Purchase>();
		_thisPage.setPageNo(pageNo);
		String hql = "from Purchase p where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(id)) {
			hql = hql + " and p.id like ? ";
			params.add('%' + id + '%');
			model.addAttribute("id", id);
		}
		if (!StringUtils.isEmpty(operator)) {
			hql = hql + " and p.operator like ? ";
			params.add('%' + operator + '%');
			model.addAttribute("operator", operator);
		}
		if (!StringUtils.isEmpty(supplierName)) {
			hql = hql + " and p.supplierName like ? ";
			params.add('%' + supplierName + '%');
			model.addAttribute("operator", supplierName);
		}
		if (!StringUtils.isEmpty(wareHouseName)) {
			hql = hql + " and p.wareHouseName like ? ";
			params.add('%' + wareHouseName + '%');
			model.addAttribute("wareHouseName", wareHouseName);
		}
		hql += " order by id desc" ;
		_thisPage = purchaseService.page(_thisPage, hql, params.toArray());
		model.addAttribute("_thisPage", _thisPage);
		
		return "purchase/purchaseList";
	}
	
	
	/**
	 * 新增采购单
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		return "purchase/purchaseAdd";
	}

	/**
	 * 保存采购单信息
	 * 
	 * @param request
	 * @param delivery
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Purch purch) throws Exception {
		
		try {
			LoginUser operator = UserSession.getLoginUser(request);
			if(!StringUtils.isEmpty(operator)) {
				purch.getPurchase().setOperator(operator.getUsername());
			}
		} catch (Exception e) {
			
		}
		String result = purchaseService.save(purch);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增采购入库单");
		return result ;
	}
	
	/**
	 * 查看
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String viewGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "") ;
		Purchase purchase = purchaseService.get(id) ;
		String hql = " from PurchaseItem where purchaseId=?" ;
		List<PurchaseItem> items = purchaseItemService.list(hql, id) ;
		items.forEach(item ->{
			System.out.println(item.getProductName()+"----"+item.getAttrName());
		});
		model.addAttribute("items", items) ;
		model.addAttribute("purchase", purchase) ;
		return "purchase/purchaseView";
	}
	
	/**
	 * 查看
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "") ;
		Purchase purchase = purchaseService.get(id) ;
		String hql = " from PurchaseItem where purchaseId=?" ;
		List<PurchaseItem> items = purchaseItemService.list(hql, id) ;
		model.addAttribute("items", items) ;
		model.addAttribute("purchase", purchase) ;
		return "purchase/purchaseEdit";
	}
	
	/**
	 * 删除选项
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delItem", method = RequestMethod.GET)
	public String delItem(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", 0) ;
		String purchaseId = ServletRequestUtils.getStringParameter(request, "purchaseId", "") ;
		purchaseItemService.delete(id);
		
		Purchase purchase = purchaseService.get(purchaseId) ;
		String hql = " from PurchaseItem where purchaseId=?" ;
		List<PurchaseItem> items = purchaseItemService.list(hql, purchaseId) ;
		model.addAttribute("items", items) ;
		model.addAttribute("purchase", purchase) ;
		return "purchase/purchaseEdit";
	}
	
	/**
	 * 删除采购信息
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, String id) throws Exception {
		purchaseService.delete(id);
		purchaseItemService.execute(" delete from PurchaseItem where purchaseId=?", id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除采购入库单:id"+id);
		return ResultUtil.getSuccessMsg();
	}
	
	
	/**
	 * 审核拒绝
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/refuse", method = RequestMethod.GET)
	@ResponseBody
	public String refuse(HttpServletRequest request, String id) throws Exception {
		Purchase purchase = purchaseService.get(id) ;
		purchase.setState(PurchaseState.REFUSE);
		purchaseService.save(purchase);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "审核拒绝采购入库单");
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 审核通过
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/pass", method = RequestMethod.GET)
	@ResponseBody
	public String pass(HttpServletRequest request, String id) throws Exception{
		Purchase purchase = purchaseService.get(id) ;
		purchase.setState(PurchaseState.PASS);
		String hql = " from PurchaseItem where purchaseId=?" ;
		List<PurchaseItem> items = purchaseItemService.list(hql, id) ;
		
		//入库
		for(PurchaseItem item:items) {
			if(!StringUtils.isEmpty(item.getSkuId())) {
				Sku sku = skuService.get(item.getSkuId()) ;
				int inventory = null != sku && null != sku.getInventory() ? sku.getInventory() : 0 ;
				int quantity = null != item.getQuantity() ? item.getQuantity() : 0 ;
				inventory += quantity ;
				sku.setInventory(inventory);
				skuService.save(sku);
				SkuWare skuWare = skuWareService.select(" from SkuWare where skuId=? and wareHouseId=? ", item.getSkuId(),purchase.getWareHouseId()) ;
				if(null == skuWare) {
					skuWare = new SkuWare() ;
					skuWare.setSkuId(sku.getId());
					skuWare.setQuantity(quantity);
					skuWare.setWareHouseId(purchase.getWareHouseId());
					skuWare.setWareHouseName(purchase.getWareHouseName());
				} else {
					int skuWareQuantity = null != skuWare.getQuantity() ? skuWare.getQuantity() : 0 ;
					skuWareQuantity += quantity ;
					skuWare.setQuantity(skuWareQuantity);
				}
				skuWareService.save(skuWare);
			}
		}
		purchase.setInWareHouseTime(new Date());
		purchaseService.save(purchase) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "审核通过采购入库单");
		return ResultUtil.getSuccessMsg();
	}
	
	
	/**
	 * 选择供应商
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getSkuInfoSelect",method = RequestMethod.GET)
	public String getSkuInfoSelect(HttpServletRequest request){
		String productName= ServletRequestUtils.getStringParameter(request, "productName", "") ;
		String barCode = ServletRequestUtils.getStringParameter(request, "barCode", "") ;
		String skuCode = ServletRequestUtils.getStringParameter(request, "skuCode", "") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo",1) ;
		Integer wareHouseId = ServletRequestUtils.getIntParameter(request,"wareHouseId",0) ;
		Page<Sku> skuPage = new Page<Sku>();
		skuPage.setPageNo(pageNo);
		String hql =" from Sku s  where 1=1 ";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(barCode)){
			hql +=" and   s.barCode like ? ";
			params.add("%"+barCode+"%");
		}
		if(!StringUtils.isEmpty(productName)){
			hql +=" and   s.name like ? ";
			params.add("%"+productName+"%");
		}
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
			Unit unit = null != product ? product.getUnit() : null ;
			if(!StringUtils.isEmpty(barCode)){
				skuMap.put("id", sku.getBarCode()) ;
			}
			if(!StringUtils.isEmpty(productName)){
				skuMap.put("id", product.getName()) ;
			}
			if(!StringUtils.isEmpty(skuCode)){
				skuMap.put("id", sku.getCode()) ;
			}
			String locations = "" ;
			if(wareHouseId != 0) {
				String hqls = " from SkuWareLocation where wareHouseId=? and skuId=?" ;
				List<SkuWareLocation> skuWareLocations = skuWareLocationService.list(hqls,wareHouseId,sku.getId()) ;
				if(skuWareLocations.size() >0 ) {
					locations= skuWareLocations.get(0).getLocaltionName() ;
					
				}
			}
			skuMap.put("location",locations) ;
			skuMap.put("skuId", sku.getId()) ;
			skuMap.put("name", product.getName()) ;
			skuMap.put("imageUrl", sku.getImageUrl()) ;
			skuMap.put("code", sku.getCode()) ;
			skuMap.put("attrName", sku.getAttrAndAttrValues()) ;
			skuMap.put("skuPrice", sku.getSkuPrice()) ;
			skuMap.put("barCode", sku.getBarCode()) ;
			skuMap.put("unitName", null != unit ? unit.getName() : "" ) ;
			skuMap.put("categoryName", null != cate ? cate.getName() : "" ) ;
			listMap.add(skuMap) ;
		}
		mapPage.setTotalCount(skuPage.getTotalCount());
		mapPage.setPageNo(skuPage.getPageNo());
		mapPage.setPageCount(skuPage.getPageCount());
		mapPage.setPageSize(skuPage.getPageSize());
		mapPage.setResult(listMap);
		
		return JsonUtil.toJson(mapPage);
	}
	
	@ResponseBody
	@RequestMapping(value="parseExcel",method=RequestMethod.POST)
	public String parseExcel(@RequestParam(name="file",required=true) MultipartFile file,HttpServletRequest request) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","读取成功！");
		jsonObject.put("code",1);
		
		List<SkuExcelModel> list = new ArrayList<SkuExcelModel>();
		try {
			list = ExcelRead.fastReadExcel(file.getInputStream(), SkuExcelModel.class);
		}catch (Exception e) {
			
			jsonObject.put("msg","Excel不合法 读取失败！");
			jsonObject.put("code",0);
			e.printStackTrace();
		}
		
		JSONArray jsonArray = new JSONArray();
		for (SkuExcelModel skuExcelModel : list) {
			Sku sku = skuService.select(" from Sku where code = ? ", skuExcelModel.getSkuCode());
			if(sku != null){
				Product product = sku.getProduct();
				JSONObject jo = new JSONObject();
				String  quantity = skuExcelModel.getQuantity();
				
				if(!NumberUtils.isNumber(quantity)){
					quantity = "1";
				}
				
				BigDecimal totalPrice = sku.getVipPrice().multiply(new BigDecimal(quantity));
				
				String category =StringUtils.isEmpty(product.getCategoryName())? "": product.getCategoryName();
				product.setCategoryName(category);
				
				jo.put("wareLocation", "");
				jo.put("product", product);
				jo.put("sku",sku);
				jo.put("quantity", quantity);
				jo.put("totalPrice", totalPrice);
				jo.put("barCode", sku.getBarCode());
				jo.put("skuCode", sku.getCode());
				jo.put("AttrAndAttrValues", sku.getAttrAndAttrValues());
				jo.put("vipPrice", sku.getVipPrice());
				jsonArray.add(jo);

			}
		}
		jsonObject.put("data", jsonArray);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导入采购入库单");
		return JsonUtil.toJson(jsonObject);
	}
}
