package cn.bohoon.stock.web;

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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuWare;
import cn.bohoon.product.entity.SkuWareLocation;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareLocationService;
import cn.bohoon.product.service.SkuWareService;
import cn.bohoon.stock.entity.WareHouse;
import cn.bohoon.stock.entity.WarehLocation;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.stock.service.WarehLocationService;
import cn.bohoon.util.ResultUtil;

/**
 * 货品库位管理
 * @author dujianqiao
 *
 */
@Controller
@RequestMapping(value = "skuWareLocation")
public class SkuWareLocationController {
	@Autowired
	SkuService skuService ;
	@Autowired
	SkuWareService skuWareService ;
	@Autowired
	WareHouseService wareHouseService ;
	@Autowired
	WarehLocationService warehLocationService ;
	@Autowired
	SkuWareLocationService skuWareLocationService ;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	
	
	/**
	 * 货品库位列表
	 * skuWareLocation/list
	 * @param request
	 * @param model
	 * @param corporateName
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		String skuName = ServletRequestUtils.getStringParameter(request, "skuName","") ;
		Integer wareHouseId = ServletRequestUtils.getIntParameter(request, "wareHouseId",0) ;
		Integer locationId = ServletRequestUtils.getIntParameter(request, "locationId",0) ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<SkuWareLocation> _thisPage = new Page<SkuWareLocation>();
		_thisPage.setPageNo(pageNo);
		String hql = "from SkuWareLocation sw where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(skuName)) {
			hql = hql + " and sw.skuName like ? ";
			params.add('%' + skuName + '%');
			model.addAttribute("skuName", skuName);
		}
		if (wareHouseId != 0 ) {
			hql = hql + " and sw.wareHouseId = ? ";
			params.add(wareHouseId);
			model.addAttribute("wareHouseId", wareHouseId);
			String hqls = " from WarehLocation where wareHouseId =? " ;
			List<WarehLocation> localList = warehLocationService.list(hqls ,wareHouseId) ;
			model.addAttribute("localList", localList);
		}
		if (locationId != 0 ) {
			hql = hql + " and sw.locationId = ? ";
			params.add(locationId);
			model.addAttribute("locationId", locationId);
		}
		_thisPage = skuWareLocationService.page(_thisPage, hql, params.toArray());
		model.addAttribute("_thisPage", _thisPage);
		
		List<WareHouse> wareHouses = wareHouseService.list() ; 
		model.addAttribute("wareHouses", wareHouses);
		return "skuIventory/skuWarehLocationList";
	}
	
	/**
	 * 新增货品库位
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		
		return "skuIventory/skuWarehLocationAdd";
	}
	
	/**
	 * 去编辑仓库库位
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		SkuWareLocation skuWareLocation = skuWareLocationService.get(id);
		model.addAttribute("item", skuWareLocation);
		return "skuIventory/skuWarehLocationEdit";
	}
	
	

	/**
	 * 删除货品库位
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, Integer id) throws Exception {
		skuWareLocationService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除货品库位:id"+id.toString());
		return ResultUtil.getSuccessMsg();
	}
	

	/**
	 * 保存货品库位
	 * 
	 * @param request
	 * @param delivery
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, SkuWareLocation skuWareLocation) throws Exception {
		List<Object> params = new ArrayList<Object>() ;
		Integer houseId = skuWareLocation.getWareHouseId() ;
		Integer skuId = skuWareLocation.getSkuId() ;
		if(StringUtils.isEmpty(skuWareLocation.getId())) {
			String hql = " from SkuWareLocation where skuId=? and wareHouseId=? and locationId=? " ;
			params.add(skuId) ;
			params.add(houseId) ;
			params.add(skuWareLocation.getLocationId()) ;
			List<SkuWareLocation> list = skuWareLocationService.list(hql,params.toArray()) ;
			if(list.size() >0 ) {
				return ResultUtil.getError("该货品在该库位已存在！") ;
			}
		}
		//货品 仓库 库存 调整
		String skuWHql = " from SkuWare where wareHouseId=? and skuId=?" ;
		
		SkuWare skuWare = skuWareService.select(skuWHql, houseId,skuId) ;
		if(null == skuWare) {
			skuWare = new SkuWare() ;
			skuWare.setQuantity(0);
			skuWare.setWareHouseId(houseId);
			skuWare.setSkuId(skuId);
			skuWare.setWareHouseName(skuWareLocation.getWareHouseName());
			skuWareService.save(skuWare);
		}
		skuWareLocationService.save(skuWareLocation);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增货品库位:"+skuWareLocation.getWareHouseName());
		return ResultUtil.getSuccessMsg();
	}
	
	
	/**
	 * 选择货品
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getSkuInfo",method = RequestMethod.GET)
	public String getSkuInfo(HttpServletRequest request){
		String code = ServletRequestUtils.getStringParameter(request, "code", "") ;
		String name= ServletRequestUtils.getStringParameter(request, "name", "") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo",1) ;
		Page<Sku> skuPage = new Page<Sku>();
		skuPage.setPageNo(pageNo);
		String hql =" select s from Sku s,Product p  where s.productId = p.id ";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(code)){
			hql +=" and  (s.code like ? ";
			params.add("%"+code+"%");
			
			hql +=" or p.name like ? ) ";
			params.add("%"+name+"%");
		}
		
		skuPage = skuService.page(skuPage, hql,params.toArray());
		
		Page<Map<String,Object>> mapPage = new Page<Map<String,Object>>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>() ;
		for(Sku sku :skuPage.getResult() ) {
			Map<String,Object> skuMap = new HashMap<String,Object>() ;
			skuMap.put("id", sku.getId()) ;
			skuMap.put("name", sku.getProduct().getName()) ;
			skuMap.put("imageUrl", sku.getImageUrl()) ;
			skuMap.put("code", sku.getCode()) ;
			skuMap.put("attrName", sku.getAttrAndAttrValues()) ;
			skuMap.put("skuPrice", sku.getSkuPrice()) ;
			skuMap.put("barCode", sku.getBarCode()) ;
			listMap.add(skuMap) ;
		}
		mapPage.setTotalCount(skuPage.getTotalCount());
		mapPage.setPageNo(skuPage.getPageNo());
		mapPage.setPageCount(skuPage.getPageCount());
		mapPage.setPageSize(skuPage.getPageSize());
		mapPage.setResult(listMap);
		return JsonUtil.toJson(mapPage);
	}
	
	/**
	 * 选择仓库
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getWareHouseSearch",method = RequestMethod.GET)
	public String getWareHouseSearch(HttpServletRequest request){
		String name= ServletRequestUtils.getStringParameter(request, "name", "") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo",1) ;
		Page<WareHouse> wareHousePage = new Page<WareHouse>();
		wareHousePage.setPageNo(pageNo);
		String hql =" from WareHouse s  where 1=1 ";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(name)){
			hql +=" and   s.corporateName like ? ";
			params.add("%"+name+"%");
			
		}
		
		wareHousePage = wareHouseService.page(wareHousePage, hql,params.toArray());
		
		Page<Map<String,Object>> mapPage = new Page<Map<String,Object>>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>() ;
		for(WareHouse wh :wareHousePage.getResult() ) {
			Map<String,Object> wareHouseMap = new HashMap<String,Object>() ;
			wareHouseMap.put("id", wh.getId()) ;
			wareHouseMap.put("name", wh.getCorporateName()) ;
			wareHouseMap.put("linkMan", wh.getLinkman()) ;
			wareHouseMap.put("adress", wh.getAddress()) ;
			wareHouseMap.put("typeName",wh.getTypeName());
			String hqls = " from WarehLocation where wareHouseId =? " ;
			List<WarehLocation> list = warehLocationService.list(hqls ,wh.getId()) ;
			wareHouseMap.put("wareHouseLocations", list) ;
			listMap.add(wareHouseMap) ;
		}
		mapPage.setTotalCount(wareHousePage.getTotalCount());
		mapPage.setPageNo(wareHousePage.getPageNo());
		mapPage.setPageCount(wareHousePage.getPageCount());
		mapPage.setPageSize(wareHousePage.getPageSize());
		mapPage.setResult(listMap);
		return JsonUtil.toJson(mapPage);
	}
	
	/**
	 * 通过仓库ID 获取库位信息
	 * 
	 * @param wareHouseId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLocationsByWareId",method = RequestMethod.GET)
	public String getLocationsByWareId(Integer wareHouseId ) {
		String hqls = " from WarehLocation where wareHouseId =? " ;
		List<WarehLocation> list = warehLocationService.list(hqls ,wareHouseId) ;
		return ResultUtil.getData(0, "成功", list) ;
		
	}
}
