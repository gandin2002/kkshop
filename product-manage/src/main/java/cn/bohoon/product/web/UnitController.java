package cn.bohoon.product.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.util.IDUtil;
import cn.bohoon.product.entity.Unit;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.UnitService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "/unit")
public class UnitController {
	@Autowired
	UnitService unitService;
	@Autowired
	ProductService productService;
	@Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	
	/**
	 * 计量单位列表
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Model model, HttpServletRequest request) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		String name = ServletRequestUtils.getStringParameter(request, "name","");
		String hql =  " from Unit ";
		if(!StringUtils.isEmpty(name)){
			hql = hql.concat(" where name like '%"+name+"%'");
		}
		model.addAttribute("name", name);
		Page<Unit> pageUnit = new Page<Unit>();
		pageUnit.setPageNo(pageNo);
		pageUnit = unitService.page(pageUnit,hql);
		model.addAttribute("pageUnit", pageUnit);
		return "unit/unitList";
	}

	/**
	 * 去新增计量单位页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addGet(Model model) {
		return "unit/unitAdd";
	}
	
	/**
	 * 新增保存计量单位信息
	 * 
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add",method=RequestMethod.POST)
	@ResponseBody
	public String addPost(Unit unit,HttpServletRequest request) throws Exception{
		List<Unit> uList = unitService.list(" from Unit where name = ? ", unit.getName());
		if(uList.size() > 0) {
			return ResultUtil.getError("名称重复") ;
		}
		unit.setCode(IDUtil.getMemberId());
    	List<Unit> utArray = unitService.list(" from Unit order by  sort ");
    	if(!utArray.isEmpty()){
    		unit.setSort(utArray.get(utArray.size()-1).getSort()+1);
    	}
		unitService.save(unit);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增单位:"+unit.getName());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 去编辑计量单位信息
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String editGet(HttpServletRequest request,Model model,Integer id){
		Unit unit = unitService.get(id);
		model.addAttribute("unit", unit);
		return "unit/unitEdit";
	}
	
	/**
	 * 编辑保存计量单位
	 * 
	 * @param request
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/edit",method=RequestMethod.POST)
	public String editPost(HttpServletRequest request, Unit unit) throws Exception{
		String hql = "from Unit where name = ? and id <> ?" ;
		Unit reu = unitService.select(hql, unit.getName(),unit.getId());
		if( null != reu ) {
			return ResultUtil.getError("名称或编码重复") ;
		}
		unitService.update(unit);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改单位:"+unit.getName());
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 删除计量单位信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/delete" , method=RequestMethod.GET)
	public String deleteGet(Integer id,HttpServletRequest request) throws Exception{
		long num = productService.select("select count(1) from Product where unitId = ?", Long.class, id);
		if(num > 0){
			return ResultUtil.getError("计量单位已被商品引用，不能删除") ;
		}
		unitService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除单位id:"+id.toString());
		return ResultUtil.getSuccessMsg();
	}

}
