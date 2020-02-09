package cn.bohoon.product.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.UploadService;
import cn.bohoon.main.util.IDUtil;
import cn.bohoon.product.entity.AttrGroup;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.AttrGroupService;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryRecommendService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "category")
public class CategoryController {

	@Autowired
	BrandService brandService ;
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	@Autowired
	OperatorService operatorService;
	@Autowired
	AttrGroupService attrGroupService;
	@Autowired
	OperatorLogService operatorLogService;
	@Autowired
	UploadService uploadService;
	@Autowired
	CategoryRecommendService categoryRecommendService;
	@Autowired
	SkuService skuService;


	/**
	 * 分类列表
	 * 
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model,String name,Integer attrGoupId ,Boolean displays, String startTime, String endTime) throws ParseException {
		List<Object> params = new ArrayList<Object>();
		String hql ="from Category  where 1=1";
		if(!StringUtils.isEmpty(name)){
			hql +=" and name like ? ";
			params.add("%"+name+"%");
			model.addAttribute("name", name);
		}
		if(!StringUtils.isEmpty(attrGoupId)){
			hql +=" and attrGoupId = ? ";
			params.add(attrGoupId);
			model.addAttribute("attrGoupId", attrGoupId);
		}
		if(!StringUtils.isEmpty(displays)){
			hql +=" and display = ? ";
			params.add(displays);
			model.addAttribute("displays", displays);
		}
		if (!StringUtils.isEmpty(startTime)) {
			hql += " and  createDate >=  ?";
			params.add(DateUtil.switchStringToDate(startTime, "yy-MM-dd"));
			model.addAttribute("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			hql += " and  createDate <  ? ";
			params.add(DateUtil.getNDayAfter(endTime, 1));
			model.addAttribute("endTime", endTime);
		}
		hql = hql.concat(" order by level,sort");
        //查询所有分类
		Map<String,Object>  categoryMap = categoryService.categorysSortingS(hql,params.toArray());
		
		
		// 新增子类的状态
//		HashMap child_hashMap = new HashMap();
//		HashMap del_hashMap = new HashMap();
//		List<Category> child_category = (List<Category>) categoryMap.get("resultDate");
		
		
//		for (Category cg : child_category){
//			for (Product product : proByCategoryList) {
//				if(cg.getId().equals(product.getCategoryId())){
//					child_hashMap.put(cg, "1");  // 则说明该分类有商品  ,也不能删除
//					del_hashMap.put(cg, "1");  
//				}else{
//					String str = categoryService.getChildId(cg.getId());
//					if (str != null){
//						del_hashMap.put(cg, str);
//					}
//				}
//			}
//		}
//
//		model.addAttribute("del_hashMap",del_hashMap);
//		model.addAttribute("child_hashMap",child_hashMap);
		//查询一级分类
		List<Category>  cyList1 = categoryService.list(" from Category where level = 1");		
		model.addAttribute("cyList1",cyList1);
		//关联类型
		model.addAttribute("attrGroupList",attrGroupService.list());
		model.addAttribute("categoryMap", categoryMap);
		return "category/categoryList";
	}

	/**
	 * 新增商品分类页面
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model, String id) {
		if (!StringUtils.isEmpty(id)) {
			Category category = categoryService.get(id);
			model.addAttribute("category", category);
		}
		List<AttrGroup> attrGroups = attrGroupService.list();
		model.addAttribute("attrGroups", attrGroups);
		
		List<Brand> brandList = brandService.list() ;
		model.addAttribute("brandList", brandList);
		return "category/categoryAdd";
	}

	/**
	 * 新增保存商品分类
	 * 
	 * @param file
	 * @param category
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String addAdd(MultipartFile file, Category category,HttpServletRequest request) throws Exception {
		if (StringUtils.isEmpty(category.getLevel())) {
			category.setLevel(1);
		} else if (category.getLevel()==1) {
			category.setLevel(2);
		} else {
			category.setLevel(3);
		}
		if(category.getLevel()!=1){
			String hql = " from Category where  pid='"+ category.getPid()+"' and level = "+category.getLevel()+"  ORDER BY sort ";
			List<Category> categoryList = categoryService.list(hql);
			category.setSort(1);
			category.setId(category.getPid()+"."+1);
			if(!categoryList.isEmpty()){
				Category source = categoryList.get(categoryList.size()-1);
				String[] str = source.getId().split("\\.");
				String id = source.getPid()+"."+(Integer.parseInt(str[str.length-1])+1);
				category.setId(id);
				category.setSort(source.getSort()+1);
			}
		}else{
			category.setId(IDUtil.getOperatorId("cd"));
			List<Category> categoryList = categoryService.list(" from Category where level = 1 ORDER BY sort ");
			Category source = categoryList.get(categoryList.size()-1);
			category.setSort(source.getSort()+1);
		}
		categoryService.save(category);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增商品分类:"+category.getName());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 去编辑商品分类
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(Model model, String id) {
		Category category = categoryService.get(id);
		Integer level = category.getLevel();
		if (level.equals(2)) {
			Category category1 = categoryService.get(category.getPid());
			model.addAttribute("category1", category1);
		} else if (level.equals(3)) {
			Category category2 = categoryService.get(category.getPid());
			model.addAttribute("category2", category2);
		}
		List<AttrGroup> attrGroups = attrGroupService.list();
		model.addAttribute("attrGroups", attrGroups);
		
		List<Brand> brandList = brandService.list() ;
		model.addAttribute("brandList", brandList);
		
		List<Category> list = categoryService.list(" from Category where pid = ? ",id);
		if(list.isEmpty()){
			model.addAttribute("isChilds","true");
		}
		if(!StringUtils.isEmpty(category.getBrandIds())){
			List<String> brandIds = Arrays.asList(category.getBrandIds().split(","));
			model.addAttribute("brandIds",brandIds);
		}
		
		// 需要判断该分类中有没有商品
		List<Product> product = productService.list("from Product where categoryId=?", id);
		
		if (product != null && product.size() > 0){
			
			model.addAttribute("products","1");
		}
		
		model.addAttribute("category", category);
		return "category/categoryEdit";
	}

	/**
	 * 编辑保存商品分类
	 * 
	 * @param file
	 * @param category
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(MultipartFile file, Category category,HttpServletRequest request) throws Exception {
		if (StringUtils.isEmpty(category.getSort())) {
			category.setSort(0);
		}
		
		if ("".equals(category.getBrandIds())){
			
			category.setBrandIds(null);
		}
		
		
		// 同时给所有该分类的商品添加百分比
		List<Product> products = productService.list("from Product where categoryId=?",category.getId());
		
		for (Product p : products){
			
			// 通过商品获取sku
			List<Sku> list = skuService.list("from Sku where productId=?", p.getId());
			
			if (null != list && list.size()>0){
				
				for (Sku s : list){
					
					s.setScore(category.getScorePercentage());
					
					skuService.update(s);
				}
			}
		
		}
		
		
		categoryService.update(category);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改商品分类:"+category.getName());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 删除商品分类
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public String delete(HttpServletRequest request, Model model, String id) throws Exception  {
		Long num = categoryService.select("select count(1) from Category where pid = ?", Long.class, id);
		if (num > 0) {
			return ResultUtil.getError("存在子类，不能删除");
		}
		Long num1 = productService.select("select count(1) from Product where categoryId = ? or lables like ?",
				Long.class, id, id);
		Long num2 = categoryRecommendService.select("select count(1) from CategoryRecommend where categoryId = ?",
				Long.class, id);
		if (num1 > 0 || num2 > 0) {
			return ResultUtil.getError("类别已被引用，不能删除");
		}
		categoryService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除商品分类id:"+id);
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 获取子分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getChildrenCategory", method = RequestMethod.GET)
	public List<Category> getCategory(String categoryId) {
		List<Category> categoryList = categoryService.list("from Category c where c.pid = ? ", categoryId);
		return categoryList;
	}
	
	/**
	 * 获取分类信息
	 */
	@ResponseBody
	@RequestMapping(value = "/getCategory", method = RequestMethod.GET)
	public Integer getCategorys(String categoryId) {
		Category cg = categoryService.get(categoryId);
		return cg.getScorePercentage();
	}
	
	
	/**
	 * 修改状态
	 * @param ciArray
	 * @param state
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/editState",method=RequestMethod.POST)
	public @ResponseBody String editState(@RequestParam(name="ciArray[]")String ciArray[],@RequestParam(name="state",required=true)Integer state,HttpServletRequest request) throws Exception{
		for (String categoryId : ciArray) {
			categoryService.modifyState(categoryId,state);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "批量修改商品分类状态:"+org.apache.commons.lang.StringUtils.join(ciArray,","));
		return ResultUtil.getSuccessMsg();
	}

}
