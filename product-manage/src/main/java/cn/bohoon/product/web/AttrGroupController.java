package cn.bohoon.product.web;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.product.entity.Attr;
import cn.bohoon.product.entity.AttrGroup;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.service.AttrGroupService;
import cn.bohoon.product.service.AttrService;
import cn.bohoon.product.service.CategoryService;

@Controller
@RequestMapping(value = "attrGroup")
public class AttrGroupController {
	
	@Autowired
    AttrService attrService;
	@Autowired
	CategoryService categoryService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;

    
    /**
     * 属性组列表
     * 
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model) throws Exception {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
        String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
		String name = ServletRequestUtils.getStringParameter(request, "name", "");
        Page<AttrGroup> pageAttrGroup=new Page<AttrGroup>();
        pageAttrGroup.setPageNo(pageNo);
        
        String hql = "from AttrGroup t where 1 =1 ";
        List<Object> params = new ArrayList<>();
        if (!StringUtils.isEmpty(name)) {
			hql += " and t.name like ?";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
        if (!StringUtils.isEmpty(startTime)) {
			hql = hql + " and t.createTime >= ?";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql = hql + " and t.createTime < ?";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		
        pageAttrGroup=attrGroupService.page(pageAttrGroup, hql,params.toArray());
        model.addAttribute("pageAttrGroup", pageAttrGroup);
        return "attrGroup/attrGroupList";
    }
    
    /**
     * 去新增属性组
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model)  {
    	List<Attr> attrs = attrService.list() ;
    	model.addAttribute("attrs", attrs) ;
        return "attrGroup/attrGroupAdd";
    }
    
    @RequestMapping(value = "aaaa", method = RequestMethod.GET)
    public void  demp(){
    			AttrGroup attrGroup	=attrGroupService.get(22);
    					JSONArray json=	(JSONArray) JSON.parse(attrGroup.getParamInfo());
    					if(json.size()>0){
    	    				for(int i=0;i<json.size();i++){  
    	    					 // 遍历 jsonarray 数组，把每一个对象转成 json 对象  
    	    					JSONObject job = json.getJSONObject(i);   
    	    					// 得到 每个对象中的属性值  
    	    					System.out.println(job.get("paramSearch")) ;  
    	    					System.out.println(job.get("paramName")) ;  
    	    					System.out.println(job.get("paramType")) ;  
    	    			}	
    }
    }
    
    
       
    /**
     * 新增保存属性组
     * 
     * @param request
     * @param attrGroup
     * @return
     * @throws Exception 
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,AttrGroup attrGroup) throws Exception  {
    	String paramInfo = JsonUtil.toJson(attrGroup.getParams()) ;
    	attrGroup.setCreateTime(new Date());
    	attrGroup.setParamInfo(paramInfo);
    	attrGroupService.save(attrGroup);
    	//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增类型:"+attrGroup.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    /**
     * 去编辑属性组
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        AttrGroup item=attrGroupService.get(id);
        model.addAttribute("item",item);
        List<Attr> attrs = attrService.list() ;
    	model.addAttribute("attrs", attrs) ;
    	
    	if(!StringUtils.isEmpty(item.getSpecsIds())){
			List<String> specsids = Arrays.asList(item.getSpecsIds().split(","));
			model.addAttribute("specsids",specsids);
		}
    	
        return "attrGroup/attrGroupEdit";
    }

    /**
     * 编辑保存属性组
     * 
     * @param attrGroup
     * @return
     * @throws Exception 
     */
	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(AttrGroup attrGroup,HttpServletRequest request,String params) throws Exception {
		String paramInfo = JsonUtil.toJson(attrGroup.getParams()) ;
    	attrGroup.setParamInfo(paramInfo);
		attrGroupService.update(attrGroup);
		JSONObject josn  =new JSONObject();
		JSONArray jsonArray = new JSONArray();
		josn.put("", "");
		josn.put("", jsonArray); 
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改类型:"+attrGroup.getName());
        return ResultUtil.getSuccessMsg();
    }

	/**
	 * 删除属性组
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
		String hql = " from Category where attrGoupId=?" ;
		List<Category> categorys = categoryService.list(hql , id) ;
		if(categorys.size() >0 ) {
			return ResultUtil.getError("该类型被分类引用，不能删除！") ;
		}
		attrGroupService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除类型id:"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}