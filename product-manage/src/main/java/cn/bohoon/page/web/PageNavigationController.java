package cn.bohoon.page.web;



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
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.page.entity.PageNavigation;
import cn.bohoon.page.service.PageNavigationService;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.product.service.ProductService;

@Controller
@RequestMapping(value = "pageNavigation")
public class PageNavigationController {

	@Autowired
	ProductLabelService labelService ;
	
	@Autowired
	CategoryService categoryService ;
	
	@Autowired
    ProductService productService;
    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	
    @Autowired
    PageNavigationService pageNavigationService;

    @RequestMapping(value = "pcWeb", method = RequestMethod.GET)
    public String pcWeb(HttpServletRequest request,Model model)  throws Exception {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String name =ServletRequestUtils.getStringParameter(request, "name");
        Page<PageNavigation> page =new Page<PageNavigation>(1000);
        page.setPageNo(pageNo);
        String hql = "from PageNavigation where loc='pcWeb'" ;
        List<Object> params = new ArrayList<>();
        if(!StringUtils.isEmpty(name)){
        	hql +=  " and name like ? ";
    		params.add('%'+name+'%');
    		model.addAttribute("name", name);
    	}
        hql += " order by level ,sort asc" ;
        page=pageNavigationService.pageSort(page, hql ,params.toArray());

        model.addAttribute("pagePageNavigation", page);
        return "pageNavigation/pcNavigationList";
    }
    
    /**
     * 微信端目录
     * 
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "wechat", method = RequestMethod.GET)
    public String wechat(HttpServletRequest request,Model model) throws Exception {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        String name =ServletRequestUtils.getStringParameter(request, "name");
        Page<PageNavigation> page=new Page<PageNavigation>(1000);
        page.setPageNo(pageNo);
        String hql = "from PageNavigation where loc='wechat' " ;
        List<Object> params = new ArrayList<>();
        if(!StringUtils.isEmpty(name)){
        	hql +=  " and name like ? ";
    		params.add('%'+name+'%');
    		model.addAttribute("name", name);
    	}
        hql += " order by level ,sort" ;
        page=pageNavigationService.pageSort(page, hql ,params.toArray());

        model.addAttribute("pagePageNavigation", page);
        return "pageNavigation/wechatNavigationList";
    }
    
    /**
     * 查看目录下的商品
     * 
     * @return
     */
    @RequestMapping(value = "showProduct", method = RequestMethod.GET)
    public String showProduct(HttpServletRequest request,Model model) throws Exception {
    	Integer id = ServletRequestUtils.getIntParameter(request, "id") ;
    	String loc = ServletRequestUtils.getStringParameter(request, "loc") ;
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	String code =ServletRequestUtils.getStringParameter(request, "code") ;
    	String name =ServletRequestUtils.getStringParameter(request, "name") ; 
    	PageNavigation item=pageNavigationService.get(id);
    	String content = item.getContent() ;
    	String queryString = " from Product where 1=1 " ;
        if(null != content && !"".equals(content)) {
        	JSONObject json = JSON.parseObject(content) ;
	        if(json.containsKey("category")) {
	        	JSONArray jsarray = json.getJSONArray("category") ;
	        	String categoryIds = "" ;
	        	for(int i=0 ;i<jsarray.size() ; i++ ) {
	        		categoryIds += "'"+jsarray.getString(i)+"'," ;
	        	}
	        	categoryIds = categoryIds.substring(0,categoryIds.length()-1) ;
	        	queryString += " and categoryId in("+categoryIds+") " ;
	        }
	        if(json.containsKey("tags")) {
	        	JSONArray jsarray = json.getJSONArray("tags") ;
	        	for(int i=0 ;i<jsarray.size() ; i++ ) {
	        		String tag = jsarray.getString(i) ;
	        		queryString += " and lables  like '%"+ tag+ "%' " ;
	        	}
	        }
	        if(json.containsKey("price")) {
	        	JSONObject jsprice = json.getJSONObject("price") ;
	        	queryString += " or  ( salesPrice > "+jsprice.getBigDecimal("start") ;
	        	queryString += " and salesPrice < "+jsprice.getBigDecimal("end") +" )"  ;
	        }
        }
        List<Object> params = new ArrayList<>();
    	if(!StringUtils.isEmpty(code)){
    		queryString = queryString + " and code = ? ";
    		params.add(code);
    		model.addAttribute("code", code);
    	}
    	if(!StringUtils.isEmpty(name)){
    		queryString = queryString + " and name like ? ";
    		params.add('%'+name+'%');
    		model.addAttribute("name", name);
    	}
    	Page<Product> pageProduct = new Page<Product>();
    	pageProduct.setPageNo(pageNo);
    	pageProduct = productService.page(pageProduct, queryString, params.toArray());

        model.addAttribute("id", id) ;
        model.addAttribute("loc", loc) ;
        model.addAttribute("pageProduct", pageProduct);
        
    	return "pageNavigation/pageProduct"; 
    }
    
    /**
     * 去添加页面目录
     * 
     * @param request
     * @param model
     * @param loc
     * @param pid
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model,String loc,Integer pid)  {
    	if(null == pid) {
    		model.addAttribute("pid", 0) ;
    		model.addAttribute("level", 1) ;
    	} else {
    		PageNavigation entity = pageNavigationService.get(pid) ;
    		model.addAttribute("pid", pid) ;
    		model.addAttribute("level", entity.getLevel()+1) ;
    	}
    	model.addAttribute("loc", loc) ;
        return "pageNavigation/pageNavigationAdd";
    }
    
    /**
     * 查询内容
     * 
     * @param request
     * @param model
     * @param flag
     * @param flagtag
     * @param flagprice
     * @param id
     * @return
     */
    @RequestMapping(value = "queryContent",method = RequestMethod.GET)
    public String queryContent(HttpServletRequest request,Model model,Boolean flag,Boolean flagtag,Boolean flagprice,Integer id)  {
    	model.addAttribute("flag", flag) ;
    	model.addAttribute("flagtag", flagtag) ;
    	if(flagtag) {
    		List<ProductLabel> labels = labelService.list() ;
    		model.addAttribute("labels", labels) ;
    	}
    	if(null != id) {
    		PageNavigation item=pageNavigationService.get(id);
    		String content = item.getContent() ;
	        if(null != content && !"".equals(content)) {
	        	JSONObject json = JSON.parseObject(content) ;
		        if(json.containsKey("category")) {
		        	model.addAttribute("category", json.getJSONArray("category")) ;
		        }
		        if(json.containsKey("tags")) {
		        	model.addAttribute("tags", json.getJSONArray("tags")) ;
		        }
		        if(json.containsKey("price")) {
		        	model.addAttribute("price", json.getJSONObject("price")) ;
		        }
	        }
    	}
    	model.addAttribute("flagprice", flagprice) ;
        return "pageNavigation/queryContent" ;
    }
    
	/**
	 * 获取子分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTree", method = RequestMethod.GET)
	public List<Category> getAsTree(String categoryId) {
		List<Category> categoryList = categoryService.categorysSorting() ;
		return categoryList;
	}
	
    /**
     * 新增保存
     * 
     * @param request
     * @param entity
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,PageNavigation entity) throws Exception {
    	String content = makeContent(request,entity) ;
    	entity.setContent(content);
    	entity.setCreateTime(new Date());
        pageNavigationService.add(entity);

 		//保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增页面目录:"+entity.getName());
		return ResultUtil.getSuccessMsg();
    }
    
    /**
     * 构造内容
     * 
     * @param request
     * @param entity
     * @return
     * @throws Exception
     */
    private String makeContent(HttpServletRequest request,PageNavigation entity) throws Exception {
    	String categoryId = ServletRequestUtils.getStringParameter(request, "categoryId") ;
    	String tags = ServletRequestUtils.getStringParameter(request, "tags") ;
    	Double minPrice = ServletRequestUtils.getDoubleParameter(request, "minPrice") ;
    	Double maxPrice = ServletRequestUtils.getDoubleParameter(request, "maxPrice") ;
    	
    	String content = "{" ;
    	if(!StringUtils.isEmpty(categoryId)) {
    		if(categoryId.endsWith(",")) {
    			categoryId = categoryId.substring(0, categoryId.length()-1) ;
    		}
    		content = content+ "\"category\":["  ;
    		String ids = "" ;
    		for(String id :categoryId.split(",")) {
    			if(!StringUtils.isEmpty(id)) {
    				ids = ids+ "\""+id+"\"," ;
    			}
    		}
    		if(ids.endsWith(",")) {
    			ids = ids.substring(0, ids.length()-1) ;
    		}
    		content = content+ ids+"]," ;
    	}
    	if(!StringUtils.isEmpty(tags)) {
    		if(tags.endsWith(",")) {
    			tags = tags.substring(0, tags.length()-1) ;
    		}
    		content =content+  "\"tags\":[" ;
    		String ids = "" ;
    		for(String id :tags.split(",")) {
    			if(!StringUtils.isEmpty(id)) {
    				ids = ids+ "\""+id+"\"," ;
    			}
    		}
    		if(ids.endsWith(",")) {
    			ids = ids.substring(0, ids.length()-1) ;
    		}
    		content = content+ ids+"]," ;
    	}
    	if(null != entity.getType() && entity.getType().contains("price")) {
    		content =content+  "\"price\":{\"start\":"+minPrice+",\"end\":"+maxPrice+"}" ;
    	}
    	if(content.endsWith(",")) {
    		content = content.substring(0, content.length()-1) ;
    	}
    	content = content+"}" ;
    	
    	return content ;
    }
    
    /**
     * 去编辑
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model) {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        PageNavigation item=pageNavigationService.get(id);
        if(null != item.getType() ) {
        	List<String> types = Arrays.asList(item.getType().split(","));
    		model.addAttribute("types",types);
        }
        model.addAttribute("item",item);
        return "pageNavigation/pageNavigationEdit";
    }

    /**
     * 编辑保存
     * 
     * @param request
     * @param entity
     * @return
     * @throws Exception
     */
	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(HttpServletRequest request,PageNavigation entity) throws Exception {
		String content = makeContent(request,entity) ;
    	entity.setContent(content);
        pageNavigationService.add(entity);
        //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "保存页面目录:"+entity.getName());
        return ResultUtil.getSuccessMsg();
    }

	/**
	 * 删除  
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request,Integer id) throws Exception {
        pageNavigationService.del(id);
      //保存日志,HttpServletRequest request
       	LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除页面目录:id"+id.toString());
		return ResultUtil.getSuccessMsg();
    }

}