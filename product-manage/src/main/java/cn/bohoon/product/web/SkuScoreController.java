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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuScore;
import cn.bohoon.product.service.SkuScoreService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.util.ResultUtil;

@Controller
@RequestMapping(value="skuScore")
public class SkuScoreController {

	@Autowired
    SkuService skuService;
	@Autowired
	SkuScoreService skuScoreService ;
	
	/**
	 * 积分商品列表
	 * 
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "list")
    public String list(Model model){
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest() ;
    	Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
    	Page<Sku> skuPage = new Page<Sku>();
    	skuPage.setPageNo(pageNo);
    	String hql = " select s from Sku s ,SkuScore sc where s.id = sc.skuId ";
    	List<Object> params = new ArrayList<>();
    	 
    	skuPage = skuService.page(skuPage, hql, params.toArray());
    	Map<Sku, SkuScore> skuMap = new HashMap<>() ;
    	for(Sku sku :skuPage.getResult()) {
    		String hqls = " from SkuScore where skuId=?" ;
    		SkuScore sc = skuScoreService.select(hqls, sku.getId()) ;
    		skuMap.put(sku, sc) ;
    	}
    	model.addAttribute("skuPage", skuPage);
    	model.addAttribute("skuMap", skuMap);
    	return "productScore/skuScoreList";
    }
    
    /**
     * 去新增积分兑换 sku
     * @return
     */
    @RequestMapping(value="addSkuScore",method=RequestMethod.GET)
    public String addSkuScore() {
    	
    	return "productScore/addSkuScore";
    }
    
    /**
     * 保存新增积分兑换 sku
     * @return
     */
    @RequestMapping(value="addSkuScore",method=RequestMethod.POST)
    @ResponseBody
    public String addSkuScorePost(SkuScore item) {
    	skuScoreService.save(item);
    	return ResultUtil.getSuccessMsg();
    }
    
    
	/**
	 * 删除积分商品
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(Integer id) {
		skuScoreService.delete(id);
		return ResultUtil.getSuccessMsg();
	}
	
    
    /**
	 * 选择货品
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getSkuInfo",method = RequestMethod.GET)
	public String getSkuInfo(HttpServletRequest request){
		String code = ServletRequestUtils.getStringParameter(request, "code", "") ;
		String name= ServletRequestUtils.getStringParameter(request, "name", "") ;
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo",1) ;
		Page<Sku> skuPage = new Page<Sku>();
		skuPage.setPageNo(pageNo);
		String hql =" select s from Sku s,Product p  where s.productId = p.id and s.id not in (select skuId from SkuScore) ";
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(code)){
			hql +=" and  (s.code like ? ";
			params.add("%"+code+"%");
			
			hql +=" or p.name like ? ) ";
			params.add("%"+name+"%");
		}
		
		skuPage = skuService.page(skuPage, hql,params.toArray());
		
		Page<Map<String,Object>> mapPage = new Page<Map<String,Object>>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>() ;
		for(Sku sku :skuPage.getResult() ) {
			Map<String,Object> skuMap = new HashMap<String,Object>() ;
			skuMap.put("id", sku.getId()) ;
			skuMap.put("name", sku.getProduct().getName()) ;
			skuMap.put("imageUrl", sku.getImageUrl()) ;
			skuMap.put("code", sku.getCode()) ;
			skuMap.put("attrName", sku.getAttrAndAttrValues()) ;
			skuMap.put("skuPrice", sku.getSkuPrice()) ;
			skuMap.put("barCode", sku.getBarCode()) ;
			skuMap.put("productId", sku.getProductId()) ;
			listMap.add(skuMap) ;
		}
		mapPage.setTotalCount(skuPage.getTotalCount());
		mapPage.setPageNo(skuPage.getPageNo());
		mapPage.setPageCount(skuPage.getPageCount());
		mapPage.setPageSize(skuPage.getPageSize());
		mapPage.setResult(listMap);
		return JsonUtil.toJson(mapPage);
	}
}
