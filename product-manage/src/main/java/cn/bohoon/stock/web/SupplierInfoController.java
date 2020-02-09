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
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.stock.entity.SupplierInfo;
import cn.bohoon.stock.entity.SupplierType;
import cn.bohoon.stock.service.SupplierInfoService;
import cn.bohoon.stock.service.SupplierTypeService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "supplierInfo")
public class SupplierInfoController {

	@Autowired
	SupplierTypeService supplierTypeService;
	@Autowired
	SupplierInfoService supplierInfoService ;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	
	/**
	 * 供应商管理
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Integer typeId = ServletRequestUtils.getIntParameter(request, "typeId", 0);
		
		String name = ServletRequestUtils.getStringParameter(request, "name","") ;
		Page<SupplierInfo> _thisPage = new Page<SupplierInfo>();
		_thisPage.setPageNo(pageNo);
		String hql = "from SupplierInfo s where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(name)) {
			hql = hql + " and s.name like ? ";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
		if(typeId != 0 ) {
			hql = hql + " and s.typeId = ? ";
			params.add(typeId);
			model.addAttribute("typeId", typeId);
		}
		_thisPage = supplierInfoService.page(_thisPage, hql,params.toArray());
		model.addAttribute("_thisPage", _thisPage);
		
		List<SupplierType> stypes = supplierTypeService.list() ;
		model.addAttribute("typeList", stypes) ;
		
		return "supplier/supplierInfoList";
	}

	/**
	 * 去新增供应商
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		List<SupplierType> stypes = supplierTypeService.list() ;
		model.addAttribute("typeList", stypes) ;
		return "supplier/supplierInfoAdd";
	}
	
	/**
	 * 去编辑供应商类型
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		SupplierInfo supplierInfo = supplierInfoService.get(id);
		model.addAttribute("supplierInfo", supplierInfo);
		
		List<SupplierType> stypes = supplierTypeService.list() ;
		model.addAttribute("typeList", stypes) ;
		
		return "supplier/supplierInfoEdit";
	}

	/**
	 * 新增保存供应商
	 * 
	 * @param request
	 * @param supplierInfo
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, SupplierInfo supplierInfo) throws Exception {
		String id = supplierInfo.getId() ;
		if (!StringUtils.isEmpty(id)) {
			String name = supplierInfo.getName() ;
			SupplierInfo entity = supplierInfoService.get(id);
			if (null != entity && !name.equals(entity.getName())) {
				String hql = "update Purchase set supplierName=? where supplierId=?" ;
				supplierInfoService.execute(hql, name,id) ;
				String hql2 = "update PurchaseReturn set supplierName=? where supplierId=?" ;
				supplierInfoService.execute(hql2, name,id) ;
			}
		}
		
		supplierInfoService.addSupplier(supplierInfo);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增供应商:"+supplierInfo.getName());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 编辑供应商类型
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, String id) throws Exception {
		supplierInfoService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除供应商:id"+id);
		return ResultUtil.getSuccessMsg();
	}

	
	/**
	 * 选择供应商
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getSupplierSearch",method = RequestMethod.GET)
	public String getWareHouseSearch(HttpServletRequest request){
		String name= ServletRequestUtils.getStringParameter(request, "name", "") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo",1) ;
		Page<SupplierInfo> superPage = new Page<SupplierInfo>();
		superPage.setPageNo(pageNo);
		String hql =" from SupplierInfo s  where 1=1 ";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(name)){
			hql +=" and   s.name like ? ";
			params.add("%"+name+"%");
			
		}
		
		superPage = supplierInfoService.page(superPage, hql,params.toArray());
		return JsonUtil.toJson(superPage);
	}
}
