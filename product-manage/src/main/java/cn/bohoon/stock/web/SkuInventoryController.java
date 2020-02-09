package cn.bohoon.stock.web;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.bohoon.excel.util.ExcelWrite;
import cn.bohoon.excel.util.ProductExcel;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.util.ResultUtil;
import cn.bohoon.product.domain.SkuInventoryInputExcelMode;
import cn.bohoon.product.domain.SkuOperationType;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuInventoryExcelMode;
import cn.bohoon.product.entity.SkuWare;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareService;
import cn.bohoon.stock.entity.WareHouse;
import cn.bohoon.stock.service.WareHouseService;

/**
 * SKU 库存
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "skuInventory")
public class SkuInventoryController {

	@Autowired
	SkuService skuService;
	@Autowired
	SkuWareService skuWareService ;
	@Autowired
	ProductService productService;
	@Autowired
	WareHouseService wareHouseService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	/**
	 * 库存管理
	 * 
	 * @param model
	 * @param request
	 * @param code
	 * @param name
	 * @param flag
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String name = ServletRequestUtils.getStringParameter(request, "name", null);
		Integer wareHouseId = ServletRequestUtils.getIntParameter(request, "wareHouseId", 0);
		Page<Sku> pageSku = new Page<Sku>();
		pageSku.setPageNo(pageNo);

		String hql="";
		WareHouse wareHouse = null;
		List<Object> params = new ArrayList<>();
		
		
		if(!StringUtils.isEmpty(name) && 0 != wareHouseId) {
			hql="select s from Sku as s,SkuWare as sw where s.status=1 and s.id=sw.skuId  and s.name like ? and sw.wareHouseId=?";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
			params.add(wareHouseId);
			model.addAttribute("wareHouseId", wareHouseId);
			wareHouse = wareHouseService.get(wareHouseId) ;
		}else if(!StringUtils.isEmpty(name)){
			hql="from Sku as s where s.status=1  and s.name like ?";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}else if(0 != wareHouseId){
			hql="select s from Sku as s,SkuWare as sw where s.status=1 and s.id=sw.skuId and sw.wareHouseId=?";
			params.add(wareHouseId);
			model.addAttribute("wareHouseId", wareHouseId);
			wareHouse = wareHouseService.get(wareHouseId) ;
		}else{
			hql="from Sku where status=1";
		}
		pageSku = skuService.page(pageSku, hql, params.toArray());
		
		Map<Sku, Object> wareHouseMap = new HashMap<Sku, Object>();
		Map<Sku, Integer> inventoryMap = new HashMap<Sku, Integer>();
		
		for(Sku sku : pageSku.getResult()) {
			
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("skuId",sku.getId());
			//result所有仓库的名字
			List<String> result = skuWareService.list("select wareHouseName from SkuWare where skuId= :skuId",String.class,param);
			//当前货品所有的仓库名字
			wareHouseMap.put(sku, result.toString()) ;
			
			inventoryMap.put(sku, sku.getInventory()) ;
			//发货地址的ID  wareHouseID
			if(0 != wareHouseId) {
				String inventoryHql = " from SkuWare where wareHouseId=? and skuId=?" ;
				SkuWare skuWare = skuWareService.select(inventoryHql, wareHouseId,sku.getId()) ;
				if(null != skuWare) {
					inventoryMap.put(sku, skuWare.getQuantity()) ;
				}
			}
		}
		for(Sku sku : pageSku.getResult()) {
			if(null != wareHouse) {
				wareHouseMap.put(sku, wareHouse.getCorporateName()) ;
				continue ;
			}
		}
		
		List<WareHouse> wareHouses = wareHouseService.list() ;
		model.addAttribute("pageSku", pageSku);
		model.addAttribute("wareHouses", wareHouses);
    	model.addAttribute("wareHouseMap", wareHouseMap);
    	model.addAttribute("inventoryMap", inventoryMap);
		return "skuIventory/skuInventoryList";
	}
	
	/**
	 * 修改积分，修改库存，修改价格，修改库存预警 get
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"editScore","editInventory","editPrice","editInventoryHint"},method=RequestMethod.GET)
	public String editScore(@RequestParam(value="id[]",required=true)Integer id[],Model model,HttpServletRequest request){
		model.addAttribute("id",StringUtils.join(id,","));
		String name ="积分百分比";
		String formUrl ="/skuInventory/editScore";
		if(request.getRequestURI().equals("/skuInventory/editInventory")){
			name= "库存" ;
			formUrl= "/skuInventory/editInventory";
		}else if(request.getRequestURI().equals("/skuInventory/editPrice")){
			name="售价";
			formUrl= "/skuInventory/editPrice";
		}else if(request.getRequestURI().equals("/skuInventory/editInventoryHint")){
			name="库存预警";
			formUrl= "/skuInventory/editInventoryHint";
		}
		model.addAttribute("name",name);
		model.addAttribute("formUrl",formUrl);

		return "/skuIventory/editOperation";
	}
	
	/**
	 * 修改库存位
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="editStorageLocation",method=RequestMethod.GET)
	public String editStorageLocation(@RequestParam(value="id[]",required=true)Integer id[],Model model){
		model.addAttribute("id",StringUtils.join(id,","));
		return "/skuIventory/editStorageLocation";
	}
	
	/**
	 * 修改库存
	 * @param type
	 * @param number
	 * @param idArray
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="editInventory",method=RequestMethod.POST)
	public @ResponseBody String editInventory(@RequestParam(name = "type", required = true) SkuOperationType type,
			@RequestParam(value = "number", required = true) Integer number,
			  Integer idArray[],HttpServletRequest request) throws Exception {
		if(SkuOperationType.MANUALLY_ADD.equals(type)){ //增加库存
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				Integer ity= null != sku.getInventory() ? sku.getInventory() : 0 ;
				sku.setInventory(number+ity);
				skuService.update(sku);
			}
		}else if(SkuOperationType.MANUALLY_SUB.equals(type)){ //减少库存
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				 Integer ity= null != sku.getInventory() ? sku.getInventory() : 0 ;
				if(ity <= number){
					sku.setInventory(0);
				}else{
					sku.setInventory(ity-number);
				}
				skuService.update(sku);
			}
		}else if(SkuOperationType.MANUALLY_NEW.equals(type)){ //setup new  inventory
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				sku.setInventory(number);
				skuService.update(sku);
			}
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改sku库存");
		return ResultUtil.getSuccess();
	}
	
  
	/**
	 * 修改价格
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "editPrice", method = RequestMethod.POST)
	public @ResponseBody String editPrice(@RequestParam(name = "type", required = true) SkuOperationType type,
			@RequestParam(value = "number", required = true) Integer number, Integer idArray[],HttpServletRequest request) throws Exception {
		if(SkuOperationType.MANUALLY_ADD.equals(type)){ //增加库存
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				sku.setSkuPrice(sku.getSkuPrice().add(new BigDecimal(number)));
				skuService.update(sku);
			}
		}else if(SkuOperationType.MANUALLY_SUB.equals(type)){ //减少库存
			BigDecimal bdl= new BigDecimal(number);
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				BigDecimal ity= sku.getSkuPrice();
				if(ity.compareTo(bdl) == -1){
					sku.setSkuPrice(new BigDecimal(0));;
				}else{
					sku.setSkuPrice(ity.subtract(bdl));
				}
				skuService.update(sku);
			}
		}else if(SkuOperationType.MANUALLY_NEW.equals(type)){ //setup new  inventory
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				sku.setSkuPrice(new BigDecimal(number));
				skuService.update(sku);
			}
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改sku库存价格");
		return ResultUtil.getSuccess();
	}
	/**
	 * 修改积分
	 * @param type
	 * @param number
	 * @param idArray
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "editScore", method = RequestMethod.POST)
	public @ResponseBody String editScore(@RequestParam(name = "type", required = true) SkuOperationType type,
			@RequestParam(value = "number", required = true) Integer number, Integer idArray[],HttpServletRequest request) throws Exception {
		if (SkuOperationType.MANUALLY_ADD.equals(type)) { // 增加库存
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				sku.setScore(number + sku.getScore());
				skuService.update(sku);
			}
		} else if (SkuOperationType.MANUALLY_SUB.equals(type)) { // 减少库存
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				Integer ity = sku.getScore();
				if (ity <= number) {
					sku.setScore(0);
				} else {
					sku.setScore(ity - number);
				}
				skuService.update(sku);
			}
		} else if (SkuOperationType.MANUALLY_NEW.equals(type)) { // setup new
																	// inventory
			for (Integer integer : idArray) {
				Sku sku = skuService.get(integer);
				sku.setScore(number);
				skuService.update(sku);
			}
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改sku库存积分");
		return ResultUtil.getSuccess();
	}
	/**
	 * 修改商品 spu 预警量
	 * @param type
	 * @param number
	 * @param idArray
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "editInventoryHint", method = RequestMethod.POST)
	public @ResponseBody String editInventoryHint(@RequestParam(name = "type", required = true) SkuOperationType type,
			@RequestParam(value = "number", required = true) Integer number, Integer idArray[],HttpServletRequest request) throws Exception{
		Set<Integer> set = new HashSet<>();
		for (Integer integer : idArray) {
			 Integer productId= skuService.get(integer).getProductId();
			if(!set.contains(productId)){
				set.add(productId);
			}
		}
		if (SkuOperationType.MANUALLY_ADD.equals(type)) { // 增加库存
			for (Integer integer : set) {
				 Product product= productService.get(integer);
				 product.setInventoryHint(product.getInventoryHint() + number);
				 productService.update(product);
			}
		} else if (SkuOperationType.MANUALLY_SUB.equals(type)) { // 减少库存
			for (Integer integer : set) {
				Product product= productService.get(integer);
				Integer ityht = product.getInventoryHint();
				if (ityht <= number) {
					product.setInventoryHint(0);
				} else {
					product.setInventoryHint(ityht-number);
				}
				productService.update(product);
			}
		} else if (SkuOperationType.MANUALLY_NEW.equals(type)) { // setup new inventory
			for (Integer integer : set) {
				Product product= productService.get(integer);
				product.setInventoryHint(number);
				productService.update(product);
			}
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改sku库存预警量");
		return  ResultUtil.getSuccess();
	}
	/**
	 * 导出货品库存excel
	 * @param response
	 * @param pid
	 * @throws Exception
	 */
	@RequestMapping(value="downloadExcel",method=RequestMethod.POST)
	public  @ResponseBody void downloadExcel(HttpServletResponse response, HttpServletRequest request) throws Exception{
		String name = ServletRequestUtils.getStringParameter(request, "name", "");
		Integer wareHouseId = ServletRequestUtils.getIntParameter(request, "wareHouseId", 0);
		String filename = URLEncoder.encode("博宏商城货品SKU", "UTF-8"); // 设置字符编码为UTF-8
		response.setHeader("Content-Disposition", "attachment;filename="+filename+System.currentTimeMillis()+".xlsx");//名字加时间戳
		ServletOutputStream sos=response.getOutputStream();
		List<SkuInventoryExcelMode> skulist =new ArrayList<SkuInventoryExcelMode>();
		if(wareHouseId==0){//为空 读出全部
			List<Sku> list=skuService.list("from Sku where status=1");
			for (Sku sku : list) {
				
				SkuInventoryExcelMode skuInventoryExcelMode=new SkuInventoryExcelMode();
				skuInventoryExcelMode.setParams(sku);
				skulist.add(skuInventoryExcelMode);		
			}	
		}else{
			String hql="select  s from Sku s where status=1 ";
			List<Object> params = new ArrayList<>();
			if(!StringUtils.isEmpty(name)) {
				hql = hql + " and s.name like ? ";
				params.add('%' + name + '%');
			
			}
			
			if(0 != wareHouseId) {
				hql += " and s.id in (select skuId from SkuWare where wareHouseId=? ) " ;
				params.add(wareHouseId);
			}
			List<Sku> list=skuService.list(hql,params.toArray());
			for (Sku sku : list) {
				SkuInventoryExcelMode skuInventoryExcelMode=new SkuInventoryExcelMode();
				skuInventoryExcelMode.setParams(sku);
				SkuWare skuWare=skuWareService.select("from SkuWare  where skuId=? and wareHouseId=?", sku.getId(),wareHouseId);
				skuInventoryExcelMode.setQuantity(skuWare.getQuantity().toString());
				skuInventoryExcelMode.setWareHouse(skuWare.getWareHouseName());
				skulist.add(skuInventoryExcelMode);	
			}
		}
		ExcelWrite.writeExcel(sos,skulist);
		sos.close();	
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "导出SKU库存");
	}
	
	 /**
     * 批量导入页面
     * 
     * @return
     */
    @RequestMapping(value="betch",method=RequestMethod.GET)
    public String betchGet() {
    	
    	return "skuIventory/skuinputbetch" ;
    }
    
    
    @RequestMapping(value="betch",method=RequestMethod.POST)
    public  String betchPost(HttpServletRequest request,Model model) throws Exception {
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		List<SkuInventoryInputExcelMode> result =ProductExcel.getWarehouseExcel(file.getInputStream()) ;
		Map<String,List<SkuInventoryInputExcelMode>> productMap = ProductExcel.NewimportDataWarehouse(result);
		model.addAttribute("productMap", productMap);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "批量导入SKU库存");
    	return "skuIventory/productImportTable";
    }
	
    
    
}
