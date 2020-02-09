package cn.bohoon.product.web;

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
import org.springframework.web.multipart.MultipartFile;

import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.system.service.UploadService;
import cn.bohoon.util.ResultUtil;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.CategoryRecommend;
import cn.bohoon.product.service.CategoryRecommendService;
import cn.bohoon.product.service.CategoryService;

@Controller
@RequestMapping(value = "categoryRecommend")
public class CategoryRecommendController {

	@Autowired
	CategoryRecommendService categoryRecommendService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	UploadService uploadService;
	
	@RequestMapping(value = "list")
	public String list(HttpServletRequest request, Model model) {
		int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<CategoryRecommend> pageCategoryRecommend = new Page<>();
		pageCategoryRecommend.setPageCount(pageNo);
		pageCategoryRecommend = categoryRecommendService.page(pageCategoryRecommend, "from CategoryRecommend");
		Map<CategoryRecommend, Category> categoryMap = new HashMap<>();
		for(CategoryRecommend categoryRecommend : pageCategoryRecommend.getResult()){
			Category category = categoryService.get(categoryRecommend.getCategoryId());
			categoryMap.put(categoryRecommend, category);
		}
		model.addAttribute("categoryMap", categoryMap);
		model.addAttribute("pageCategoryRecommend", pageCategoryRecommend);
		return "categoryRecommend/categoryRecommendList";
	}
	
	@RequestMapping(value = "add",method = RequestMethod.GET)
	public String addGet(Model model) {
		List<Category> categoryList = categoryService.list();
		model.addAttribute("categoryList", categoryList);
		return "categoryRecommend/categoryRecommendAdd";
	}
	
	@ResponseBody
	@RequestMapping(value = "add",method = RequestMethod.POST)
	public String addPost(MultipartFile file, Model model, CategoryRecommend categoryRecommend) throws Exception {
		if(null != file){
			String imageUrl = uploadService.handleFileUpload(file, "categoryRecommend");
			categoryRecommend.setImage(imageUrl);
		}
		categoryRecommendService.save(categoryRecommend);
		return ResultUtil.getSuccessMsg();
	}
	
	@RequestMapping(value = "edit",method = RequestMethod.GET)
	public String editGet(Model model,Integer id) {
		List<Category> categoryList = categoryService.list();
		CategoryRecommend categoryRecommend = categoryRecommendService.get(id);
		model.addAttribute("categoryRecommend", categoryRecommend);
		model.addAttribute("categoryList", categoryList);
		return "categoryRecommend/categoryRecommendEdit";
	}
	
	/**
	 * 编辑商品分类推荐
	 * 
	 * @param file
	 * @param model
	 * @param categoryRecommend
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public String editPost(MultipartFile file, Model model, CategoryRecommend categoryRecommend) throws Exception {
		if(null != file){
			String imageUrl = uploadService.handleFileUpload(file, "categoryRecommend");
			categoryRecommend.setImage(imageUrl);
		}
		categoryRecommendService.update(categoryRecommend);
		return ResultUtil.getSuccessMsg();
	}
	
	/**
	 * 删除商品分类推荐
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws CheckException
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(HttpServletRequest request, Model model, Integer id) throws CheckException{
		categoryRecommendService.delete(id);
		return ResultUtil.getSuccessMsg();
	}
}
