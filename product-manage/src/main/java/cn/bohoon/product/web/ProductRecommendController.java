package cn.bohoon.product.web;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductRecommend;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductRecommendService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.UnitService;

@Controller
@RequestMapping(value = "productRecommend")
public class ProductRecommendController {

    @Autowired
    ProductRecommendService productRecommendService;
    @Autowired
    ProductService productService;
    @Autowired
    UnitService unitService;
    @Autowired
    CategoryService categoryService;
    
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, Model model) throws Exception{
    	int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	Page<ProductRecommend> pageProductRecommend = new Page<ProductRecommend>();
    	pageProductRecommend.setPageNo(pageNo);
    	pageProductRecommend = productRecommendService.page(pageProductRecommend, "from ProductRecommend");
    	Map<ProductRecommend, Product> productMap = new HashMap<>();
    	Map<ProductRecommend, Category> categoryMap = new HashMap<>();
    	for(ProductRecommend pr : pageProductRecommend.getResult()){
    		Product product = productService.get(pr.getProductId());
    		productMap.put(pr, product);
    		Category category = categoryService.get(product.getCategoryId());
    		categoryMap.put(pr, category);
    	}
    	model.addAttribute("productMap", productMap);
    	model.addAttribute("categoryMap", categoryMap);
    	model.addAttribute("pageProductRecommend", pageProductRecommend);
    	return "productRecommend/productRecommendList";
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addGet(HttpServletRequest request, Model model){
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	Page<Product> pageProduct = new Page<Product>();
    	pageProduct.setPageNo(pageNo);
    	pageProduct = productService.page(pageProduct, "from Product where flag = 1");
    	model.addAttribute("pageProduct", pageProduct);
    	return "productRecommend/productRecommendAdd";
    }
    
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request, Integer[] productId) 
    		throws Exception {
    	if(StringUtils.isEmpty(productId)) {
    		return ResultUtil.getError("先选择推荐商品") ;
    	}
    	List<ProductRecommend> productRecommendList = new ArrayList<>();
    	for(Integer productId1 : productId){
    		ProductRecommend productRecommend= productRecommendService
    				.select("from ProductRecommend where productId = ?", productId1);
    		if(null != productRecommend) {
    			Product product = productService.get(productId1);
    			return ResultUtil.getError("编号为："+product.getCode()+" 的商品已经推荐") ;
    		}
    		productRecommend = new ProductRecommend();
    		productRecommend.setProductId(productId1);
    		productRecommend.setFlag(false);
    		productRecommendList.add(productRecommend);
    	}
    	productRecommendService.saveBatch(productRecommendList, productRecommendList.size());
        return ResultUtil.getSuccessMsg();
    }
    
    @RequestMapping(value = "search")
    public String search(Model model, HttpServletRequest request, String codeOrName){
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	Page<Product> pageProduct = new Page<Product>();
    	pageProduct.setPageNo(pageNo);
    	String hql = " from Product p where flag = 1 ";
    	List<Object> params = new ArrayList<>();
    	if(!StringUtils.isEmpty(codeOrName)){
    		hql = hql + " and p.searchWords like ? ";
    		params.add('%'+codeOrName+'%');
    	}
    	pageProduct = productService.page(pageProduct, hql, params.toArray());
    	model.addAttribute("pageProduct", pageProduct);
    	return "productRecommend/searchResults";
    }
     
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request,Integer id){
    	productRecommendService.delete(id);
    	return ResultUtil.getSuccessMsg();
    }
    
    @ResponseBody
    @RequestMapping(value = "modifiedStatus", method = RequestMethod.GET)
    public String modifiedStatus(HttpServletRequest request, Integer id){
    	ProductRecommend productRecommend = productRecommendService.get(id);
    	Boolean flag = productRecommend.getFlag();
    	productRecommend.setFlag(!flag);
		productRecommendService.update(productRecommend);
		return  ResultUtil.getSuccessMsg();
    }
    
}
