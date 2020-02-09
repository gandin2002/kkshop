package cn.bohoon.distribution.web;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.company.service.CityService;
import cn.bohoon.distribution.domain.ExpInfo;
import cn.bohoon.distribution.entity.ExpfeeCity;
import cn.bohoon.distribution.entity.ExpfeeOrder;
import cn.bohoon.distribution.entity.ExpfeeTemplate;
import cn.bohoon.distribution.service.ExpfeeCityService;
import cn.bohoon.distribution.service.ExpfeeOrderService;
import cn.bohoon.distribution.service.ExpfeeTemplateService;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.userInfo.entity.UserLevel;
import cn.bohoon.userInfo.service.UserLevelService;

@Controller
@RequestMapping(value = "expfeeTemplate")
public class ExpfeeTemplateController {
	
	@Autowired
	CityService cityService ;
	@Autowired
	UserLevelService userLevelService ;
	@Autowired
	ExpfeeCityService expfeeCityService ;
	@Autowired
	ExpfeeOrderService expfeeOrderService ;
    @Autowired
    ExpfeeTemplateService expfeeTemplateService;

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;


    /**
     * 快递运费模板
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model)  {
        Page<ExpfeeTemplate> page =new Page<ExpfeeTemplate>(20);
        page=expfeeTemplateService.page(page, "from ExpfeeTemplate order by updateTime desc");
        
        Map<Object,Object> orderFeeMap = new HashMap<Object,Object>() ;
        Map<Object,Object> cityFeeMap = new HashMap<Object,Object>() ;
        
        for(ExpfeeTemplate exp : page.getResult()) {
        	
        	if(exp.getCalcTye() == 0 ) {
        		String hql = " from ExpfeeOrder where efId =?" ;
        		List<ExpfeeOrder> orderFees = expfeeOrderService.list(hql,exp.getId()) ;
        		orderFeeMap.put(exp, orderFees) ;
        		
        	} else if(exp.getCalcTye() == 1) {
        		String hql = " from ExpfeeCity where efId =?" ;
            	List<ExpfeeCity> cityFees = expfeeCityService.list(hql,exp.getId()) ;
            	cityFeeMap.put(exp, cityFees) ;
        	}
	
        }
        
        // 设置默认为第一行的位置
        List<ExpfeeTemplate> exps = new ArrayList<ExpfeeTemplate>();
        for(ExpfeeTemplate exp : page.getResult()) {
        	
        	if (exp.getIsDefault()){
        		exps.add(0,exp);
        		model.addAttribute("cType", exp.getCalcTye());
        	}else{
        		
        		exps.add(exp);
        	}
        }        
        page.setResult(exps);
        	
        model.addAttribute("cityFeeMap", cityFeeMap);
        model.addAttribute("orderFeeMap", orderFeeMap);
        model.addAttribute("pageExpfeeTemplate", page);
        return "expfeeTemplate/expfeeTemplateList";
    }
    
    /**
     * 运费计算设置
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value="set", method = RequestMethod.GET)
    public String setGet(HttpServletRequest request,Model model) {
    	initData(model);
        ExpfeeTemplate template=expfeeTemplateService.getSet();
        model.addAttribute("item",template);
        if(null !=  template) {
        	/*String hql = " from ExpfeeOrder where efId =?" ;
    		List<ExpfeeOrder> orderFees = expfeeOrderService.list(hql,template.getId()) ;
    		model.addAttribute("orderFees",orderFees);*/
             	String hql = " from ExpfeeOrder where efId =?" ;
         		List<ExpfeeOrder> orderFees = expfeeOrderService.list(hql,template.getId()) ;
         		model.addAttribute("orderFees",orderFees);
             	String hql1 = " from ExpfeeCity where efId =?" ;
             	List<ExpfeeCity> cityFees = expfeeCityService.list(hql1,template.getId()) ;
             	model.addAttribute("cityFees",cityFees);
             
        } 
     
    	
        return "expfeeTemplate/expfeeTemplateSet" ;
    }
    
    
    
    
    /**
     * 去新增快递模板
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
    	initData(model);
        return "expfeeTemplate/expfeeTemplateAdd";
    }
    
    /**
     * 保存快递模板
     * 
     * @param request
     * @param info
     * @return
     * @throws Exception 
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,ExpInfo info) throws Exception  {
        expfeeTemplateService.save(info);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增运费计算模板:"+info.getTemplate().getName());
		return ResultUtil.getSuccessMsg();
    }
    
    /**
     * 初始化信息
     * 
     * @param model
     */
    public void initData(Model model) {
    	List<UserLevel> levels = userLevelService.list() ;
     	String names = "" ;
     	for(UserLevel ul : levels) {
     		names += ul.getName()+"," ;
     	}
     	if(names.contains(",")) {
     		names = names.substring(0,names.length()-1) ;
     	}
    
     	model.addAttribute("lvNames", names);
     	model.addAttribute("citys", cityService.listAllProvice()) ;
     	System.out.println(cityService.listAllProvice());
    }
    
    /**
     * 去编辑快递计费模板
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        
        initData(model);
        
        ExpfeeTemplate template=expfeeTemplateService.get(id);
        model.addAttribute("item",template);
        if(template.getCalcTye() == 0 ) {
        	String hql = " from ExpfeeOrder where efId =?" ;
    		List<ExpfeeOrder> orderFees = expfeeOrderService.list(hql,template.getId()) ;
    		model.addAttribute("orderFees",orderFees);
        } else {
        	String hql = " from ExpfeeCity where efId =?" ;
        	List<ExpfeeCity> cityFees = expfeeCityService.list(hql,template.getId()) ;
        	model.addAttribute("cityFees",cityFees);
        }
        return "expfeeTemplate/expfeeTemplateEdit";
    }

    /**
     * 编辑保存快递模板
     * 
     * @param expfeeTemplate
     * @return
     * @throws Exception 
     */
	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(ExpfeeTemplate expfeeTemplate,HttpServletRequest request) throws Exception {
		
		
        expfeeTemplateService.save(expfeeTemplate);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "保存运费计算模板:"+expfeeTemplate.getName());
        return ResultUtil.getSuccessMsg();
    }

	/**
	 * 删除快递计费模板
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        expfeeTemplateService.delete(id);
        expfeeCityService.execute("delete from ExpfeeCity where efId=?", id) ;
        expfeeOrderService.execute("delete from ExpfeeOrder where efId=?", id) ;
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除运费计算模板:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }
	
	/**
	 * 删除订单计费模板
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/deleteFeeOrder")
    public String deleteFeeOrder(HttpServletRequest request,Integer id) {
		if(!id.equals(0)){
			expfeeOrderService.delete(id);
		}
		
		return ResultUtil.getSuccessMsg();
    }
	
	/**
	 * 快递城市快递计费模板
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/deleteFeeCity")
    public String deleteFeeCity(HttpServletRequest request,Integer id) {
		if(!id.equals(0)){
			expfeeCityService.delete(id);
		}
		return ResultUtil.getSuccessMsg();
    }
	
	
	  /**
     * 默认运费模板
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "default", method = RequestMethod.GET)
    public String defaultValue(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        
        
        ExpfeeTemplate template=expfeeTemplateService.get(id);
       
        
        String hql = "from ExpfeeTemplate where isDefault=1";
        
        List<ExpfeeTemplate> list = expfeeTemplateService.list(hql);
        
        for (ExpfeeTemplate e : list){
        	
        	e.setIsDefault(false);
        	 expfeeTemplateService.update(e);
        }
        
        // 修改默认配置
        template.setIsDefault(true);
        expfeeTemplateService.update(template);
        
        
        return "redirect:/expfeeTemplate/list";
    }

}