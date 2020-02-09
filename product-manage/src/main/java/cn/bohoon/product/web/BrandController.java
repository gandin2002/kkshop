package cn.bohoon.product.web;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.main.domain.LoginUser;
import cn.bohoon.main.domain.UserSession;
import cn.bohoon.main.system.entity.Operator;
import cn.bohoon.main.system.service.OperatorLogService;
import cn.bohoon.main.system.service.OperatorService;
import cn.bohoon.main.system.service.UploadService;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value = "/brand")
public class BrandController {

	@Autowired
	BrandService brandService;
	@Autowired
	UploadService uploadService;
	@Autowired
	OperatorLogService operatorLogService;
	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;
	
    @Autowired
    OperatorService operatorService;


	/**
	 * 品牌列表
	 * 
	 * @param request
	 * @param model
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model, String name) {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		Page<Brand> pageBrand = new Page<Brand>();
		pageBrand.setPageNo(pageNo);
		String hql = " from Brand where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(name)) {
			hql = hql.concat(" and name like ? ");
			params.add('%' + name + '%');
			model.addAttribute("name", name);
		}
		hql = hql.concat(" order by sort asc ");
		pageBrand = brandService.page(pageBrand, hql, params.toArray());
		model.addAttribute("pageBrand", pageBrand);
		return "brand/brandList";
	}

	/**
	 * 去新增品牌页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addGet(HttpServletRequest request, Model model) {
		return "brand/brandAdd";
	}

	/**
	 * 新增保存品牌
	 * 
	 * @param request
	 * @param brand
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String addPost(HttpServletRequest request, Brand brand) throws Exception {
		List<Brand> uList = brandService.list(" from Brand where name = ?", brand.getName());
		if (!uList.isEmpty()) {
			return ResultUtil.getError("品牌名称重复");
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		if (!StringUtils.isEmpty(file)) {
			String image = uploadService.handleFileUpload(file, "brand");
			brand.setImage(image);
		}
		List<Brand> tcArray = brandService.list(" from Brand order by  sort ");
    	if(!tcArray.isEmpty()){
    		brand.setSort(tcArray.get(tcArray.size()-1).getSort()+1);
    	}
		brand.setStatus(true);
		brandService.save(brand);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "新增品牌:"+brand.getName());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 去编辑品牌页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editGet(Model model, Integer id) {
		Brand brand = brandService.get(id);
		model.addAttribute("brand", brand);
 
		return "brand/brandEdit";
	}

	/**
	 * 编辑保存品牌信息
	 * 
	 * @param request
	 * @param id
	 * @param status
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editPost(HttpServletRequest request, Integer id, String name,Boolean status, Integer sort) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		Brand brand = brandService.get(id);
		if (!StringUtils.isEmpty(file)) {
			String image = uploadService.handleFileUpload(file, "brand");
			brand.setImage(image);
		}
		brand.setSort(sort);
		brand.setStatus(status);
		brand.setName(name);
		brandService.update(brand);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "修改品牌:"+brand.getName());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 删除品牌信息
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 * @throws CheckException
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, Integer id) throws Exception {
		Long num = productService.select("select count(1) from Product where brandId = ?", Long.class, id);
		if (num > 0) {
			return ResultUtil.getError("品牌已被商品引用，不能删除！");
		}
		brandService.delete(id);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "删除品牌id:"+id.toString());
		return ResultUtil.getSuccessMsg();
	}

	/**
	 * 品牌是否首页展示切换
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception 
	 * @throws CheckException
	 */
	@RequestMapping(value = "/switchStatus")
	@ResponseBody
	public String switchStatus(HttpServletRequest request, Integer id) throws Exception {
		Brand brand = brandService.get(id);
		brand.setStatus(!brand.getStatus());
		brandService.update(brand);
		//保存日志,HttpServletRequest request
   		LoginUser userif= UserSession.getLoginUser(request);
		Operator operator = operatorService.findUserByUsername(userif.getUsername());
		operatorLogService.addUserLog(operator, request, "切换首页展示品牌id:"+brand.getName());
		return ResultUtil.getSuccessMsg();
	}
}
