package cn.bohoon.express.web;

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

import cn.bohoon.express.entity.Logistics;
import cn.bohoon.express.service.LogisticsService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

/**
 * 物流公司管理
 * @author djq
 *
 */
@Controller
@RequestMapping(value = "logistics")
public class LogisticsController {

    @Autowired
    LogisticsService logisticsService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


    /**
     * 物流公司列表
     * 
     * @param request
     * @param model
     * @param companyname
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model,String companyname)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        Page<Logistics> pageLogistics=new Page<Logistics>(5);
        pageLogistics.setPageNo(pageNo);
        String hql = " from Logistics s where 1 = 1 ";
        List<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(companyname)) {
			hql = hql + " and s.companyname like ? ";
			params.add('%' + companyname + '%');	
		}
        
        
//        hql += " and s.status=?";
//        params.add("1");
        pageLogistics=logisticsService.page(pageLogistics, hql,params.toArray());
        model.addAttribute("companyname", companyname);
        model.addAttribute("pageLogistics", pageLogistics);
        return "logistics/logisticsList";
    }
    
    /**
     * 去添加物流公司
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
    	
        return "logistics/logisticsAdd";
    }
    
    /**
     * 新增保存物流公司
     * 
     * @param request
     * @param logistics
     * @return
     * @throws Exception 
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,Logistics logistics) throws Exception  {
    	logisticsService.saveExpressAddLogistics(logistics);
    	//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增配送物流公司:"+logistics.getCompanyname());
		return ResultUtil.getSuccessMsg();
    }
    
    
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        Logistics logistics=logisticsService.get(id);
        model.addAttribute("item",logistics);
        return "logistics/logisticsEdit";
    }

	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(Logistics logistics,HttpServletRequest request) throws Exception {
		logisticsService.save(logistics);

 		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改配送物流公司:"+logistics.getCompanyname());

        return ResultUtil.getSuccessMsg();
    }
	
	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
		logisticsService.delete(id);
		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除配送公司:id"+id);
		return ResultUtil.getSuccessMsg();
    }
	
    @ResponseBody
    @RequestMapping(value = "/modifiedStatus", method = RequestMethod.GET)
    public String modifiedStatus(HttpServletRequest request, Integer id) throws Exception{
    	
    	Logistics target = logisticsService.select(" from Logistics where defaultState = ? ",true);
    	if(target != null){
        	target.setDefaultState(false);
        	logisticsService.save(target);
    	}
    	Logistics logistics=logisticsService.get(id);
    	logistics.setDefaultState(!logistics.getDefaultState());
    	logisticsService.save(logistics);
    	//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改配送公司状态");
    	return  ResultUtil.getSuccessMsg();
    }
}