package cn.bohoon.express.web;
/*
 * 地区管理
 */
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

import cn.bohoon.company.entity.City;
import cn.bohoon.company.service.CityService;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "city")
public class CityController {

	    @Autowired
	    CityService cityService;

	    @Autowired
	    OperatorLogService operatorLogService;
	    @Autowired
	    OperatorService operatorService;

	    @RequestMapping(value = "list", method = RequestMethod.GET)
	    public String list(HttpServletRequest request,Model model) throws Exception  {
	        List<City> citys =cityService.listAll();
	        model.addAttribute("citys", citys);
	        return "city/cityList";
	    }
	    
	    @RequestMapping(value = "add", method = RequestMethod.GET)
	    public String addGet(HttpServletRequest request,Model model)  {
	    	Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
	        City city=cityService.get(id);
	        model.addAttribute("item",city);
	        return "city/cityAdd";
	    }
	    
	    @ResponseBody
	    @RequestMapping(value = "add", method = RequestMethod.POST)
	    public String addPost(HttpServletRequest request,City city) throws Exception{
	    	Integer cityId = city.getCityCode();
	    	if(!StringUtils.isEmpty(cityId)){
	    		City city1 = cityService.select(" from City where cityCode = ? ",cityId);
	    		if(city1!=null){
	    			return ResultUtil.getError("温馨提示：地区编码重复");
	    		}
	    	}
		    cityService.save(city);
			//保存日志,HttpServletRequest request
       		LoginUser userif= UserSession.getLoginUser(request);
			Operator operator = operatorService.findUserByUsername(userif.getUsername());
			operatorLogService.addUserLog(operator, request, "新增地区:"+city.getName());
			return ResultUtil.getSuccessMsg();
	    }
	    
	    @RequestMapping(value = "edit", method = RequestMethod.GET)
	    public String editGet(HttpServletRequest request,Model model) {
	        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
	        City city=cityService.get(id);
	        model.addAttribute("item",city);
	        return "city/cityEdit";
	    }

		@ResponseBody
	    @RequestMapping(value = "edit", method = RequestMethod.POST)
	    public String editPost(City city,HttpServletRequest request) throws Exception {
	        cityService.save(city);
	      //保存日志,HttpServletRequest request
       		LoginUser userif= UserSession.getLoginUser(request);
			Operator operator = operatorService.findUserByUsername(userif.getUsername());
			operatorLogService.addUserLog(operator, request, "修改地区:"+city.getName());
	        return ResultUtil.getSuccessMsg();
	    }

		@ResponseBody
	    @RequestMapping(value = "/delete")
	    public String delete(HttpServletRequest request,Integer id) throws Exception {
	        cityService.delete(id);
	      //保存日志,HttpServletRequest request
       		LoginUser userif= UserSession.getLoginUser(request);
			Operator operator = operatorService.findUserByUsername(userif.getUsername());
			operatorLogService.addUserLog(operator, request, "删除地区:id"+id.toString());
	        return ResultUtil.getSuccessMsg();
	    }
		
		/**
		 * 根据父节点 获得子集
		 * @param pid
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value="getAddress",method=RequestMethod.GET)
		public String city(Integer pid){
			List<City> listCity = cityService.findChildList(pid);
			return JsonUtil.toJson(listCity);
		}
}