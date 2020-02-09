package cn.bohoon.distribution.web;



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

import cn.bohoon.distribution.entity.Distribute;
import cn.bohoon.distribution.service.DistributeService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "distribute")
public class DistributeController {

    @Autowired
    DistributeService distributeService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model,String deliveryWay)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);

        Page<Distribute> pageDistribute=new Page<Distribute>(5);
        pageDistribute.setPageNo(pageNo);
        
        String hql = " from Distribute s where 1 = 1 ";
        List<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(deliveryWay)) {
			hql = hql + " and s.deliveryWay like ? ";
			params.add('%' + deliveryWay + '%');	
		}
        pageDistribute=distributeService.page(pageDistribute, hql,params.toArray());
        model.addAttribute("deliveryWay", deliveryWay);
        model.addAttribute("pageDistribute", pageDistribute);
        return "distribute/distributeList";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
        return "distribute/distributeAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,Distribute distribute) throws Exception  {
        distributeService.save(distribute);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增配送方式:"+distribute.getTypeName());
		return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        Distribute distribute=distributeService.get(id);
        model.addAttribute("item",distribute);
        return "distribute/distributeEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(Distribute distribute,HttpServletRequest request) throws Exception {
        distributeService.save(distribute);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改配送方式:"+distribute.getTypeName());
        return ResultUtil.getSuccessMsg();
    }
	
	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
		distributeService.delete(id);
		   //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除配送方式:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }
}