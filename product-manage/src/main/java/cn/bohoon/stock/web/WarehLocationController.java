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
import cn.bohoon.stock.entity.WarehLocation;
import cn.bohoon.stock.entity.WarehouseType;
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
@RequestMapping(value = "warehLocation")
public class WarehLocationController {
	
	@Autowired
	WarehouseTypeService typeService;
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
	 * 仓库库位列表
	 * 
	 * @param request
	 * @param model
	 * @param corporateName
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		String name = ServletRequestUtils.getStringParameter(request, "name","") ;
		String wareHouseName = ServletRequestUtils.getStringParameter(request, "wareHouseName","") ;
		String typeId = ServletRequestUtils.getStringParameter(request, "typeId","") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<WarehLocation> _thisPage = new Page<WarehLocation>();
		_thisPage.setPageNo(pageNo);
		String hql = "from WarehLocation d where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(name)) {
			hql = hql + " and d.name like ? ";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
		if (!StringUtils.isEmpty(wareHouseName)) {
			hql = hql + " and d.wareHouseName like ? ";
			params.add('%' + wareHouseName + '%');
			model.addAttribute("wareHouseName", wareHouseName);
		}
		if (!StringUtils.isEmpty(typeId)) {
			hql = hql + " and d.typeId = ? ";
			params.add(typeId+"");
			model.addAttribute("typeId", typeId);
		}
		_thisPage = warehLocationService.page(_thisPage, hql, params.toArray());
		model.addAttribute("_thisPage", _thisPage);
		
		String hqls = " from WarehouseType where status = 1 " ;
		List<WarehouseType> typeList = typeService.list(hqls);
		
		model.addAttribute("typeList", typeList);
		return "stock/warehLocationList";
	}

	/**
	 * 新增仓库库位
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		String hql = " from WarehouseType where status = 1 " ;
		List<WarehouseType> typeList = typeService.list(hql);
		
		model.addAttribute("typeList", typeList);
		return "stock/warehLocationAdd";
	}

	/**
	 * 保存仓库库位
	 * 
	 * @param request
	 * @param delivery
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, WarehLocation warehLocation) throws Exception {
		warehLocationService.save(warehLocation);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增库位信息:"+warehLocation.getName());
		return ResultUtil.getSuccessMsg();
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
		WarehLocation warehLocation = warehLocationService.get(id);
		model.addAttribute("item", warehLocation);

		String hql = " from WarehouseType where status = 1 " ;
		List<WarehouseType> typeList = typeService.list(hql);
		model.addAttribute("typeList", typeList);
		
		return "stock/warehLocationEdit";
	}

	/**
	 * 删除仓库库位
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, Integer id) throws Exception {
		String hql = "select count(1) from SkuWareLocation where locationId= ?" ;
		Long skwL = skuWareLocationService.select(hql,Long.class, id) ;
		if(skwL>0 ) {
			return ResultUtil.getError("该库位有货品，不能删除！") ;
		}
		warehLocationService.execute("delete from WarehLocation where id=?", id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除库位信息:id"+id.toString());
		return ResultUtil.getSuccessMsg();
	}

}