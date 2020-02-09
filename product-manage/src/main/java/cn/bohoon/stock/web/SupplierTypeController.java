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
import cn.bohoon.stock.entity.SupplierType;
import cn.bohoon.stock.service.SupplierInfoService;
import cn.bohoon.stock.service.SupplierTypeService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "supplierType")
public class SupplierTypeController {

	@Autowired
	SupplierTypeService service;
	@Autowired
	SupplierInfoService supplierInfoService;
	@Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	/**
	 * 供应商类型管理
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String name = ServletRequestUtils.getStringParameter(request, "name","") ;
		Page<SupplierType> _thisPage = new Page<SupplierType>();
		_thisPage.setPageNo(pageNo);
		String hql = "from SupplierType s where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(name)) {
			hql = hql + " and s.name like ? ";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
		hql += " order by sort asc";
		_thisPage = service.page(_thisPage, hql, params.toArray());
		model.addAttribute("_thisPage", _thisPage);
		return "supplier/supplierTypeList";
	}

	/**
	 * 去新增供应商类型
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		return "supplier/supplierTypeAdd";
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
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		SupplierType supplierType = service.get(id);
		model.addAttribute("supplierType", supplierType);
		return "supplier/supplierTypeEdit";
	}

	/**
	 * 新增保存供应商类型
	 * 
	 * @param request
	 * @param supplierType
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, SupplierType supplierType) throws Exception {
		Integer id = supplierType.getId() ;
		if (!StringUtils.isEmpty(id)) {
			String name = supplierType.getName() ;
			SupplierType entity = service.get(id);
			if (null != entity && !name.equals(entity.getName())) {
				String hql = "update SupplierInfo set typeName=? where typeId=?" ;
				supplierInfoService.execute(hql, name,id) ;
			}
		}
		service.save(supplierType);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增供应商类型:"+supplierType.getName());
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
	public String delete(HttpServletRequest request, Integer id) throws Exception {
		service.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除供应商类型:id"+id.toString());
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
	public String switchStatus(HttpServletRequest request, Integer id) throws Exception {
		SupplierType supplierType = service.get(id);
		supplierType.setStatus(!supplierType.getStatus());
		service.update(supplierType);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "供应商类型状态调整:"+supplierType.getName());
		return ResultUtil.getSuccessMsg();
	}
}
