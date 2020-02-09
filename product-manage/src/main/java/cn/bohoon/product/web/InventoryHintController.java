package cn.bohoon.product.web;

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

import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.product.entity.InventoryHint;
import cn.bohoon.product.service.InventoryHintService;

@Controller
@RequestMapping(value = "inventoryHint")
public class InventoryHintController {

	@Autowired
	InventoryHintService inventoryHintService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<InventoryHint> pageInventoryHint = new Page<InventoryHint>();
		pageInventoryHint.setPageNo(pageNo);
		pageInventoryHint = inventoryHintService.page(pageInventoryHint, "from InventoryHint i order by i.min asc");
		model.addAttribute("pageInventoryHint", pageInventoryHint);
		return "inventoryHint/inventoryHintList";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		return "inventoryHint/inventoryHintAdd";
	}

	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, InventoryHint inventoryHint) throws Exception  {
		try {
			this.checkMinAndMax(inventoryHint.getMin(),inventoryHint.getMax(),null);
		} catch (CheckException e) {
			return ResultUtil.getError(e.getMessage()) ;
		}
		inventoryHintService.save(inventoryHint);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增库存提示:id"+inventoryHint.getId().toString());
		return ResultUtil.getSuccessMsg();
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editGet(HttpServletRequest request, Model model) {
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		InventoryHint inventoryHint = inventoryHintService.get(id);
		model.addAttribute("inventoryHint", inventoryHint);
		return "inventoryHint/inventoryHintEdit";
	}

	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editPost(InventoryHint inventoryHint,HttpServletRequest request) throws Exception {
		try {
			this.checkMinAndMax(inventoryHint.getMin(),inventoryHint.getMax(),inventoryHint.getId());
		} catch (CheckException e) {
			return ResultUtil.getError(e.getMessage()) ;
		}
		inventoryHintService.update(inventoryHint);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改库存提示:id"+inventoryHint.getId().toString());
		return ResultUtil.getSuccessMsg();
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, Integer id) throws Exception {
		inventoryHintService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除库存提示:id"+id.toString());
		return ResultUtil.getSuccessMsg();
	}
	
	private void checkMinAndMax(Integer min, Integer max, Integer id) throws CheckException{
		if(max < min){
			throw new CheckException("库存区间最大值不能小于最小值！");
		}
		List<InventoryHint> iList = new ArrayList<>();
		StringBuffer str = new StringBuffer("from InventoryHint i");
		if(StringUtils.isEmpty(id)){
			str.append(" where (i.min <= ? and i.max > ?) or (i.min < ? and i.max > ?)");
			iList = inventoryHintService.list(str.toString(),min, min, max, max);
		}else{
			str.append(" where (i.min <= ? and i.max > ? and i.id <> ?) or (i.min < ? and i.max > ? and i.id <> ?)");
			iList = inventoryHintService.list(str.toString(),min, min, id, max, max, id);
		}
		if (!iList.isEmpty()) {
			throw new CheckException("已存在此库存显示区间，不能重复设置！");
		}
	}

}