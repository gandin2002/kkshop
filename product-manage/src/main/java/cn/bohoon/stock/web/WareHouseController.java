package cn.bohoon.stock.web;

import java.util.ArrayList;
import java.util.List;

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
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.service.SkuWareLocationService;
import cn.bohoon.product.service.SkuWareService;
import cn.bohoon.stock.entity.WareHouse;
import cn.bohoon.stock.entity.WareHouseImage;
import cn.bohoon.stock.entity.WarehouseType;
import cn.bohoon.stock.service.PurchaseService;
import cn.bohoon.stock.service.WareHouseImageService;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.stock.service.WarehLocationService;
import cn.bohoon.stock.service.WarehouseTypeService;
import cn.bohoon.util.ResultUtil;

/**
 * 仓库管理
 * 
 * @author dujianqiao
 *
 */
@Controller
@RequestMapping(value = "wareHouse")
public class WareHouseController {

	@Autowired
	SkuWareService skuWareService ;
	@Autowired
	PurchaseService purchaseService ;
	@Autowired
	WarehouseTypeService typeService;
	@Autowired
	WareHouseService wareHouseService ;
	@Autowired
	WarehLocationService warehLocationService ;
	@Autowired
	SkuWareLocationService skuWareLocationService ;
	@Autowired
	WareHouseImageService wareHouseImageService;
	@Autowired
	OperatorService operatorService;

    @Autowired
    OperatorLogService operatorLogService;
  


	/**
	 * 仓库列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String corporateName = ServletRequestUtils.getStringParameter(request, "corporateName","") ;
		String linkman= ServletRequestUtils.getStringParameter(request, "linkman","") ;
      
		Page<WareHouse> pageDelivery = new Page<WareHouse>();
		pageDelivery.setPageNo(pageNo);
		String hql = "from WareHouse d where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(corporateName)) {
			hql = hql + " and d.corporateName like ? ";
			params.add('%' + corporateName + '%');
			model.addAttribute("corporateName", corporateName);
		}
		if (!StringUtils.isEmpty(linkman)) {
			hql = hql + " and d.linkman like ? ";
			params.add('%' + linkman + '%');
			model.addAttribute("linkman", linkman);
		}
		
		pageDelivery = wareHouseService.page(pageDelivery, hql, params.toArray());
		
		model.addAttribute("pageDelivery", pageDelivery);
		return "stock/wareHouseList";
	}

	/**
	 * 新增仓库
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		String hql = " from WarehouseType where status = 1 " ;
		List<WarehouseType> typeList = typeService.list(hql);
		
		List<Operator> operatorlist = operatorService.list(); 
		model.addAttribute("operatorlist", operatorlist);
		model.addAttribute("typeList", typeList);
		return "stock/wareHouseAdd";
	}

	/**
	 * 保存仓库
	 * 
	 * @param request
	 * @param delivery
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request,WareHouse wareHouse,String houseName,String operatorId) throws Exception {
		
		String[] str = request.getParameterValues("prodImage.imageUrl");
		String trimName = houseName.trim();
		 wareHouse.setHouseName(trimName);
		 if(wareHouse.getIsDefault()==true){
			 List<WareHouse> WareHouseList=	wareHouseService.list();
			 	for (WareHouse wareHouse2 : WareHouseList) {
					 if(wareHouse2.getIsDefault()==true)
					 {
						 wareHouse2.setIsDefault(false);
						 wareHouseService.save(wareHouse2);
					 }
				}
		 }
		 	
		if(!StringUtils.isEmpty(wareHouse.getId())) {
			WareHouse entity = wareHouseService.get(wareHouse.getId()) ;
			String name = wareHouse.getCorporateName() ;
			Integer id = wareHouse.getId() ;
			if(!entity.getCorporateName().equals(name)) {
				//涉及到的信息 调整
				purchaseService.execute("update Purchase set wareHouseName=? where wareHouseId=?", name,id) ;
				warehLocationService.execute("update WarehLocation set wareHouseName=? where wareHouseId=?", name,id) ;
				skuWareService.execute("update SkuWare set wareHouseName=? where wareHouseId=?", name,id) ;
				skuWareLocationService.execute("update SkuWareLocation set wareHouseName=? where wareHouseId=?", name,id) ;
			}
		}
		wareHouseService.save(wareHouse);
		
		
		
    	if(null != str && str.length > 0){
    		wareHouseService.execute("delete from WareHouseImage where wareHouseId = ?", wareHouse.getId());
    		List<WareHouseImage> lsProdImage = new ArrayList<>();
    		for(String imageUrl : str){
    			WareHouseImage wareHouseImage = new WareHouseImage();
    			wareHouseImage.setWareHouseId(wareHouse.getId());
    			wareHouseImage.setImageUrl(imageUrl);
    			lsProdImage.add(wareHouseImage);
    		}
    		wareHouseImageService.saveBatch(lsProdImage, lsProdImage.size());
    	}
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增仓库信息:"+wareHouse.getCorporateName());
			
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 去编辑仓库
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		WareHouse wareHouse = wareHouseService.get(id);
		model.addAttribute("item", wareHouse);
		String hql = " from WarehouseType where status = 1 " ;
		List<WarehouseType> typeList = typeService.list(hql);
		List<Operator> operatorlist = operatorService.list(); 
		for(Operator operat : operatorlist){
			if(null!=operat.getUsername()){
				if(wareHouse.getHouseName().contains(operat.getUsername())){
				     operat.setMultiSelected(2);
				}
			}
			
		}
		if(!StringUtils.isEmpty(wareHouse.getHouseName())){
			String houseName = wareHouse.getHouseName();
			String[] split = houseName.split(" ");
			model.addAttribute("split",split);
		}
	   
		//System.out.println("创库名"+wareHouse.getHouseName());
		List<WareHouseImage> lsProdImage = wareHouseImageService.getWareHouseImage(id);
		model.addAttribute("operatorlist",operatorlist);
		model.addAttribute("typeList", typeList);
		model.addAttribute("lsProdImage",lsProdImage);
		
		return "stock/wareHouseEdit";
	}

	/**
	 * 删除仓库
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, Integer id) throws Exception {
		String hql = "select count(1) from SkuWare where wareHouseId= ?";
		Long skw = skuWareService.select(hql, Long.class, id) ;
		String hql2 = "select count(1) from SkuWareLocation where wareHouseId= ?" ;
		Long skwL = skuWareLocationService.select(hql2,Long.class, id) ;
		if(skw >0 || skwL>0 ) {
			return ResultUtil.getError("仓库使用中，不能删除！") ;
		}
		wareHouseService.execute("delete from WareHouse where id=?", id) ;
		warehLocationService.execute(" delete from WarehLocation where wareHouseId=?", id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除仓库信息:id"+id.toString());
		return ResultUtil.getSuccessMsg();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getWaresByTypeId")
	public String getWaresByTypeId(HttpServletRequest request, String typeId) {
		String hql = " from WareHouse where typeId=? " ;
		List<WareHouse> wareHouses = wareHouseService.list(hql,typeId) ;
		return ResultUtil.getData(0, "成功！", wareHouses) ;
	}

}