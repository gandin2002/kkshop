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
import cn.bohoon.stock.entity.WarehouseType;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.stock.service.WarehouseTypeService;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "warehouseType")
public class WarehouseTypeController {

	@Autowired
	WarehouseTypeService service;
	@Autowired
	WareHouseService wareHouseService ;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	/**
	 * 库存类型管理
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String name = ServletRequestUtils.getStringParameter(request, "name","") ;
		String sort = ServletRequestUtils.getStringParameter(request, "sort","asc") ;
		Page<WarehouseType> _thisPage = new Page<WarehouseType>();
		_thisPage.setPageNo(pageNo);
		String hql = "from WarehouseType s where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(name)) {
			hql = hql + " and s.name like ? ";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
		hql = hql + " order by s.sort "+sort ;
		model.addAttribute("sort", sort);
		_thisPage = service.page(_thisPage, hql,params.toArray());
		model.addAttribute("_thisPage", _thisPage);
		return "stock/warehouseTypeList";
	}

	/**
	 * 去新增库存类型
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		return "stock/warehouseTypeAdd";
	}
	
	/**
	 * 去编辑库存类型
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		WarehouseType warehouseType = service.get(id);
		model.addAttribute("warehouseType", warehouseType);
		return "stock/warehouseTypeEdit";
	}

	/**
	 * 新增保存库存类型
	 * 
	 * @param request
	 * @param warehouseType
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, WarehouseType warehouseType) throws Exception {
		if(StringUtils.isEmpty(warehouseType.getId())) {
			String id = IDUtil.getInstance().getCommonId(service, WarehouseType.class) ;
			warehouseType.setId(id);
		}
		service.save(warehouseType);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增仓库分类:"+warehouseType.getName());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 编辑库存类型
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, String id) throws Exception {
		service.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除仓库分类:id"+id);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 类型状态调整
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/switchStatus")
	@ResponseBody
	public String switchStatus(HttpServletRequest request, String id) throws Exception {
		WarehouseType warehouseType = service.get(id);
		warehouseType.setStatus(!warehouseType.getStatus());
		boolean state = false ;
		if(warehouseType.getStatus()) {
			state = true ;
		}
		
		service.save(warehouseType);
		String hql = " update  WareHouse set status=? where typeId=?" ;
		wareHouseService.execute(hql, state,id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "调整库存分类状态:"+warehouseType.getName());
		return ResultUtil.getSuccessMsg();
	}
}
