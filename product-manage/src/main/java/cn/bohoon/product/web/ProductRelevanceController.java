package cn.bohoon.product.web;



import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.entity.ProductRelevance;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.product.service.ProductRelevanceService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.util.ConvertUtils;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "productRelevance")
public class ProductRelevanceController {

	@Autowired
    ProductService productService;
    @Autowired
    ProductRelevanceService productRelevanceService;
	@Autowired
	BrandService brandService;

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductLabelService productLabelService;
	
	@Autowired
	SkuService skuService;
	
    @Autowired
    OperatorLogService operatorLogService;
    
    @Autowired
    OperatorService operatorService;
	
    /**
     * 关联商品列表
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request,Model model,Integer id)  {
        Integer pageNo=ServletRequestUtils.getIntParameter(request, "pageNo",1);
        
        Page<ProductRelevance> pageProductRelevance=new Page<ProductRelevance>();
        pageProductRelevance.setPageNo(pageNo);
        String hql = " from ProductRelevance ";
        if(!StringUtils.isEmpty(id)){
        	hql +="  where  productIds like '%"+id+"%'";
        }
        pageProductRelevance=productRelevanceService.page(pageProductRelevance, hql);
        
        model.addAttribute("id", id);
        model.addAttribute("pageProductRelevance", pageProductRelevance);
        return "productRelevance/productRelevanceList";
    }
    
    /**
     * 去新增关联商品
     * 
     * @param request
     * @param model
     * @return
     * @throws ParseException 
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request,Model model, String product,  String categoryId,
			Integer brandId, String label, Integer sales, Integer sales2, BigDecimal price, BigDecimal price2,
			  String startTime, String endTime) throws ParseException  {
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	String code = ServletRequestUtils.getStringParameter(request, "code", "");
    	String name = ServletRequestUtils.getStringParameter(request, "name", "");
    	String productIds = ServletRequestUtils.getStringParameter(request, "productIds", "");
    	
		Page<Product> pageProduct = new Page<Product>();
		pageProduct.setPageNo(pageNo);
		String hql = " from Product p where p.flag = 1 ";
		List<Object> params = new ArrayList<Object>();
		
		
		
		if (!StringUtils.isEmpty(product)) {
			hql += " and (p.name like '%" + product + "%' or p.code like '%" + product + "%' )";
			model.addAttribute("product", product);
		}
		if (!StringUtils.isEmpty(categoryId)) {
			hql += " and p.categoryId like  '" + categoryId + "%'";
			model.addAttribute("categoryId", categoryId);
		}
		if (!StringUtils.isEmpty(brandId)) {
			hql += " and p.brandId =  " + brandId;
			model.addAttribute("brandId", brandId);
		}
		if (!StringUtils.isEmpty(label)) {
			hql += " and p.lables like '%" + label + "%'";
			model.addAttribute("labels", label);
		}
		if (!StringUtils.isEmpty(sales)) {
			hql += " and p.salesNum >= " + sales;
			model.addAttribute("sales", sales);
		}
		if (!StringUtils.isEmpty(sales2)) {
			hql += " and p.salesNum <=" + sales2;
			model.addAttribute("sales2", sales2);
		}
		if (!StringUtils.isEmpty(price)) {
			hql += " and p.salesPrice >=" + price;
			model.addAttribute("price", price);
		}
		if (!StringUtils.isEmpty(price2)) {
			hql += " and p.salesPrice <= " + price2;
			model.addAttribute("price2", price2);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql += " and p.createDate >=  ?   ";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql += " and p.createDate <  ? ";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		
		
		if (!StringUtils.isEmpty(code)) {
			hql = hql + " and p.code = ? ";
			params.add(code);
			model.addAttribute("code", code);
		}
		if (!StringUtils.isEmpty(name)) {
			hql = hql + " and p.name like ? ";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
		
		
		pageProduct = productService.page(pageProduct, hql, params.toArray());
		
		Map<Integer, Object> inventoryMap = new HashMap<Integer, Object>();
		String inventoryHql = "select sum(inventory) from Sku where productId = ? and status = 1 ";
		for (Product prod : pageProduct.getResult()) {
			Long inventory1 = skuService.select(inventoryHql, Long.class, prod.getId());
			inventoryMap.put(prod.getId(), inventory1);
		}
	    model.addAttribute("inventoryMap", inventoryMap);
	
		Set<String> set = (Set<String>) request.getSession().getAttribute("relevance_pids") ;
		if(null == set) {
			set = new HashSet<String>() ;
		}
		if(!StringUtils.isEmpty(productIds)) {
			set.add(productIds) ;
		}
		Integer pageNox = ServletRequestUtils.getIntParameter(request, "pageNo", 0);
		if(StringUtils.isEmpty(productIds) && pageNox == 0){
			set = new HashSet<String>() ;
		}
		request.getSession().setAttribute("relevance_pids", set);

		
		List<Brand> brandList = brandService.list();
		List<Category> categoryList = categoryService.list(" from Category where  level  = 1");
		List<ProductLabel> pLabelList = productLabelService.list(" from ProductLabel where status= 1");
		model.addAttribute("pLabelList", pLabelList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("brandList", brandList);
		
		
		model.addAttribute("selectpIdsLen", set.size());
		model.addAttribute("selectpIds", set);
		model.addAttribute("pageProduct", pageProduct);
        return "productRelevance/productRelevanceAdd";
    }
    
    /**
     * 去新增关联商品
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "addSure", method = RequestMethod.GET)
    public String addSureGet(HttpServletRequest request,Model model)  {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
		Set<String> set = (Set<String>) request.getSession().getAttribute("relevance_pids") ;
		Map<String,Object> params = new HashMap<String,Object>();
		List<Integer> pIds = new ArrayList<Integer>() ;
		Iterator<String> iter = set.iterator() ;
		while(iter.hasNext()) {
			pIds.add(ConvertUtils.parseInteger(iter.next())) ;
		}
		if(pIds.size() == 0) {
			pIds.add(0) ;
		}
    	params.put("ids",pIds);
    	List<Product> result = productService.list(" from Product where id IN :ids",params);
		model.addAttribute("pageProduct", result);
		if(id != -1) {
			model.addAttribute("id", id);
		}
        return "productRelevance/productRelevanceAddSure";
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
    @RequestMapping(value = "/deleteSure")
    public String deleteSure(HttpServletRequest request,String productId) throws Exception {
		Set<String> set = (Set<String>) request.getSession().getAttribute("relevance_pids") ;
		set.remove(productId) ;
		request.getSession().setAttribute("relevance_pids", set);
		
		return ResultUtil.getSuccessMsg();
    }
    
    /**
     * 新增保存
     * 
     * @param request
     * @param productRelevance
     * @return
     * @throws Exception 
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request,ProductRelevance productRelevance) throws Exception  {
    	if(StringUtils.isEmpty(productRelevance.getProductIds())){
    		return ResultUtil.getError("请选择需要关联的商品！");
    	}
    	productRelevance.setTotal(productRelevance.getProductIds().split(",").length);
    	
        productRelevanceService.save(productRelevance);
        request.getSession().removeAttribute("relevance_pids");
      //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增商品关联");
		return ResultUtil.getSuccessMsg();
    }

    /**
     * 去编辑页面
     * 
     * @param request
     * @param model
     * @return
     * @throws ParseException 
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editGet(HttpServletRequest request,Model model, String product,  String categoryId,
			Integer brandId, String label, Integer sales, Integer sales2, BigDecimal price, BigDecimal price2,
			  String startTime, String endTime) throws ParseException {
        Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
        ProductRelevance productRelevance = productRelevanceService.get(id);
        Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	String code = ServletRequestUtils.getStringParameter(request, "code", "");
    	String name = ServletRequestUtils.getStringParameter(request, "name", "");
    	String productIds = ServletRequestUtils.getStringParameter(request, "productIds", "");
    	
    	
    	
    	
		Page<Product> pageProduct = new Page<Product>();
		pageProduct.setPageNo(pageNo);
		String hql = " from Product p where p.flag = 1 ";
		List<Object> params = new ArrayList<>();
		
		if (!StringUtils.isEmpty(product)) {
			hql += " and (p.name like '%" + product + "%' or p.code like '%" + product + "%' )";
			model.addAttribute("product", product);
		}
		if (!StringUtils.isEmpty(categoryId)) {
			hql += " and p.categoryId like  '" + categoryId + "%'";
			model.addAttribute("categoryId", categoryId);
		}
		if (!StringUtils.isEmpty(brandId)) {
			hql += " and p.brandId =  " + brandId;
			model.addAttribute("brandId", brandId);
		}
		if (!StringUtils.isEmpty(label)) {
			hql += " and p.lables like '%" + label + "%'";
			model.addAttribute("labels", label);
		}
		if (!StringUtils.isEmpty(sales)) {
			hql += " and p.salesNum >= " + sales;
			model.addAttribute("sales", sales);
		}
		if (!StringUtils.isEmpty(sales2)) {
			hql += " and p.salesNum <=" + sales2;
			model.addAttribute("sales2", sales2);
		}
		if (!StringUtils.isEmpty(price)) {
			hql += " and p.salesPrice >=" + price;
			model.addAttribute("price", price);
		}
		if (!StringUtils.isEmpty(price2)) {
			hql += " and p.salesPrice <= " + price2;
			model.addAttribute("price2", price2);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql += " and p.createDate >=  ?   ";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql += " and p.createDate <  ? ";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		
		
		if (!StringUtils.isEmpty(code)) {
			hql = hql + " and p.code = ? ";
			params.add(code);
			model.addAttribute("code", code);
		}
		if (!StringUtils.isEmpty(name)) {
			hql = hql + " and p.name like ? ";
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
		
		pageProduct = productService.page(pageProduct, hql, params.toArray());
		model.addAttribute("pageProduct", pageProduct);
		
		Map<Integer, Object> inventoryMap = new HashMap<Integer, Object>();
		String inventoryHql = "select sum(inventory) from Sku where productId = ? and status = 1 ";
		for (Product prod : pageProduct.getResult()) {
			Long inventory1 = skuService.select(inventoryHql, Long.class, prod.getId());
			inventoryMap.put(prod.getId(), inventory1);
		}
		 model.addAttribute("inventoryMap", inventoryMap);
		Set<String> set = (Set<String>) request.getSession().getAttribute("relevance_pids") ;
		Integer pageNox = ServletRequestUtils.getIntParameter(request, "pageNo", 0);
		if(StringUtils.isEmpty(productIds) && pageNox == 0){
			set = new HashSet<String>() ;
		}
		if(null == set || set.size() == 0) {
			set = new HashSet<String>() ;
			if(!StringUtils.isEmpty(productRelevance.getProductIds())) {
				String []ids = productRelevance.getProductIds().split(",") ;
				for(String pid : ids) {
					set.add(pid) ;
				}
			}
		}
		
		if(!StringUtils.isEmpty(productIds)) {
			set.add(productIds) ;
		} 
		List<Brand> brandList = brandService.list();
		List<Category> categoryList = categoryService.list(" from Category where  level  = 1");
		List<ProductLabel> pLabelList = productLabelService.list(" from ProductLabel where status= 1");
		model.addAttribute("pLabelList", pLabelList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("brandList", brandList);
		
		
		request.getSession().setAttribute("relevance_pids", set);
		model.addAttribute("selectpIdsLen", set.size());
		model.addAttribute("selectpIds", set);
		model.addAttribute("productRelevance",productRelevance);
		
        return "productRelevance/productRelevanceEdit";
    }

    /**
     * 编辑保存
     * 
     * @param productRelevance
     * @return
     * @throws Exception 
     */
	@ResponseBody
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editPost(ProductRelevance productRelevance,HttpServletRequest request) throws Exception {
    	if(StringUtils.isEmpty(productRelevance.getProductIds())){
    		return ResultUtil.getError("请选择需要关联的商品！");
    	}
    	productRelevance.setTotal(productRelevance.getProductIds().split(",").length);
    	
        productRelevanceService.update(productRelevance);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改关联商品");
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
        productRelevanceService.delete(id);
        //保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除关联商品");
		return ResultUtil.getSuccessMsg();
    }
	/**
	 * 修改状态
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/editState")
	public @ResponseBody String editState( HttpServletRequest request,Integer id) throws Exception{
		productRelevanceService.execute("update ProductRelevance set state = (case state when 1 then 0 WHEN 0 then 1 end) where id = ? ", id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改关联商品状态");
		return ResultUtil.getSuccessMsg();
	}
	
	
}