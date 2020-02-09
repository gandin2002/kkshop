package cn.bohoon.product.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductLabel;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductLabelService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "productWarehouse")
public class ProductWarehouseController {

	@Autowired
	SkuService skuService;
	
	@Autowired
	ProductService productService;
	
	
	@Autowired
	BrandService brandService;

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductLabelService productLabelService;
	

    @Autowired
    OperatorLogService operatorLogService;
    @Autowired
    OperatorService operatorService;
	
	
	/**
	 * 商品列表
	 * 
	 * @param model
	 * @param request
	 * @param code
	 * @param name
	 * @param flag
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "list")
	public String list(Model model, HttpServletRequest request, String code, String name, String product, String categoryId,
			Integer brandId, String label) throws ParseException {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<Product> pageProduct = new Page<Product>();
		pageProduct.setPageNo(pageNo);
		
		String hql = "  from Product as prt where 1 = 1 and flag = 0 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(product)) {
			hql += " and (prt.name like '%" + product + "%' or prt.code like '%" + product + "%' )";
			model.addAttribute("product", product);
		}
		if (!StringUtils.isEmpty(categoryId)) {
			hql += " and prt.categoryId like  '" + categoryId + "%'";
			model.addAttribute("categoryId", categoryId);
		}
		if (!StringUtils.isEmpty(brandId)) {
			hql += " and prt.brandId =  " + brandId;
			model.addAttribute("brandId", brandId);
		}
		if (!StringUtils.isEmpty(label)) {
			hql += " and prt.lables like '%" + label + "%'";
			model.addAttribute("labels", label);
		}
		pageProduct = productService.page(pageProduct, hql, params.toArray());
		Map<Product, Object> inventoryMap = new HashMap<Product, Object>();
		String inventoryHql = "select sum(inventory) from Sku where productId = ? and status = 1 " ;
    	for (Product prod : pageProduct.getResult()) {
    		Long inventory3 = skuService.select(inventoryHql,Long.class,prod.getId());
    		inventoryMap.put(prod, inventory3);
		}
    	
		List<Brand> brandList = brandService.list();
		List<Category> categoryList = categoryService.list(" from Category where  level  = 1");
		List<ProductLabel> pLabelList = productLabelService.list(" from ProductLabel where status= 1");
		model.addAttribute("pLabelList", pLabelList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("brandList", brandList);
		
    	model.addAttribute("flag", 0);
    	model.addAttribute("inventoryMap", inventoryMap);
    	model.addAttribute("resultUrl", request.getRequestURI());
		model.addAttribute("pageProduct", pageProduct);
		return "productWarehouse/productWarehouseList";
	}
	
	
	
	/**
	 * 删除仓库中的商品
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public String delete(HttpServletRequest request, Model model, Integer id) throws Exception {
		productService.delete(id);
		skuService.execute("delete from Sku where productId = ?", id) ;
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除仓库中的商品id:"+id.toString());
		return ResultUtil.getSuccessMsg();
	}
	/**
	 * 商家product
	 * @param pIdArray
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "putaway")
	public String putaway(@RequestParam(name="pIdArray[]")Integer pIdArray[],HttpServletRequest request) throws Exception{
		for (Integer Pid : pIdArray) {
			productService.putaway(Pid);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "仓库中的商品批量上架id:"+org.apache.commons.lang.StringUtils.join(pIdArray,","));
		return ResultUtil.getSuccessMsg();
	}
	/**
	 * 上架Sku
	 * @param sIdArray
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "putawaySku")
	public String putawaySku(@RequestParam(name="sIdArray[]")Integer sIdArray[],HttpServletRequest request) throws Exception{
		for (Integer  sId : sIdArray) {
			skuService.putaway(sId);
		}
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "仓库中的SKU批量上架id:"+org.apache.commons.lang.StringUtils.join(sIdArray,","));
		return ResultUtil.getSuccessMsg();
	}
	
}
