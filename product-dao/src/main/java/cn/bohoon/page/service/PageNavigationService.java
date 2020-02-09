package cn.bohoon.page.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.page.dao.PageNavigationDao;
import cn.bohoon.page.entity.PageNavigation;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.util.ConvertUtils;

@Service
@Transactional
public class PageNavigationService extends BaseService<PageNavigation,Integer>{

	@Autowired
	BrandService brandService ;
	@Autowired
    ProductService productService;
	@Autowired
	CategoryService categoryService ;
	@Autowired
	PageNavigationDao pageNavigationDao;
	

    @Autowired
    PageNavigationService(PageNavigationDao pageNavigationDao){
        super(pageNavigationDao);
    }

    /**
     * 管理端首页
     * 
     * @param page
     * @param hql
     * @param values
     * @return
     */
	public Page<PageNavigation> pageSort(Page<PageNavigation> page, String hql,Object... values) {
		page = page(page, hql,values) ;
		List<PageNavigation> list = new ArrayList<PageNavigation>() ;
		List<PageNavigation> pageList = page.getResult() ;
		for (PageNavigation pa1 : pageList) {
			if (pa1.getLevel() == 1) {
				list.add(pa1);
				for (PageNavigation pa2 : pageList) {
					if (pa2.getPid().equals(pa1.getId())) {
						list.add(pa2);
						for (PageNavigation pa3 : pageList) {
							if (pa3.getPid().equals(pa2.getId())) {
								list.add(pa3);
							}
						}
					}
				}
			}
		}
		page.setResult(list);
		return page ;
	}
	
	public Page<Product> productPage(Page<Product>productPage ,Integer id) {
		PageNavigation item= get(id);
		String content = item.getContent() ;
    	String queryString = " from Product where flag=1 " ;
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
	       /* if(json.containsKey("tags")) {
	        	JSONArray jsarray = json.getJSONArray("tags") ;
	        	for(int i=0 ;i<jsarray.size() ; i++ ) {
	        		String tag = jsarray.getString(i) ;
	        		queryString += " and lables  like '%"+ tag+ "%' " ;
	        	}
	        }*/
	        String tag="推荐";
	        queryString += " and lables  like '%"+ tag+ "%' " ;
	        if(json.containsKey("price")) {
	        	JSONObject jsprice = json.getJSONObject("price") ;
	        	queryString += " or  ( salesPrice > "+jsprice.getBigDecimal("start") ;
	        	queryString += " and salesPrice < "+jsprice.getBigDecimal("end") +" )"  ;
	        	queryString += "  order by  showSort asc,presaleNums desc"  ;
	        }
        } else {
        	queryString += " and categoryId ='' " ;
        	queryString += "  order by  showSort asc,presaleNums desc"  ;

        }
      
        productPage = productService.page(productPage, queryString);
		return productPage ;
	}
	
	
	/**
	 * 小程序端的议价专区获取议价的
	 * @param productPage
	 * @param id
	 * @param queryString 查询语句
	 * @return
	 */
	public Page<Product> productsPage(Page<Product>productPage ,Integer id,String queryString,List params_s) {
		PageNavigation item= get(id);
		String content = item.getContent() ;
//    	String queryString = " from Product where flag=1 " ;
        if(null != content && !"".equals(content)) {
        	JSONObject json = JSON.parseObject(content) ;
	        if(json.containsKey("category")) {
	        	JSONArray jsarray = json.getJSONArray("category") ;
	        	String categoryIds = "" ;
	        	for(int i=0 ;i<jsarray.size() ; i++ ) {
	        		categoryIds += "'"+jsarray.getString(i)+"'," ;
	        	}
	        	categoryIds = categoryIds.substring(0,categoryIds.length()-1) ;
	        	queryString += " and p.categoryId in("+categoryIds+") " ;
	        }
	        if(json.containsKey("tags")) {
	        	JSONArray jsarray = json.getJSONArray("tags") ;
	        	for(int i=0 ;i<jsarray.size() ; i++ ) {
	        		String tag = jsarray.getString(i) ;
	        		queryString += " and p.lables  like '%"+ tag+ "%' " ;
	        	}
	        }
	        if(json.containsKey("price")) {
	        	JSONObject jsprice = json.getJSONObject("price") ;
	        	queryString += " or  ( p.salesPrice > "+jsprice.getBigDecimal("start") ;
	        	queryString += " and p.salesPrice < "+jsprice.getBigDecimal("end") +" )"  ;
	        }
        } else {
        	queryString += " and p.categoryId ='' " ;
        }
        
        queryString += " order by p.id";
      
        productPage = productService.page(productPage, queryString,params_s.toArray());
		return productPage ;
	}
	
	
	/**
	 * 客户端首页展示
	 * 
	 * @param id
	 * @return
	 */
//	@Cacheable(value = "pageBrandNavigationCache", key = "#root.targetClass + #root.methodName + #id")
	public List<Brand> queryBrandsByNavigation(Integer id) {
		PageNavigation item = get(id) ;
		if(null != item) {
			String content = item.getContent() ;
	    	
	        if(null != content && !"".equals(content)) {
	        	JSONObject json = JSON.parseObject(content) ;
		        if(json.containsKey("category")) {
		        	JSONArray jsarray = json.getJSONArray("category") ;
		        	List<String> categoryIds = JSONArray.parseArray(jsarray.toJSONString(), String.class) ;
		        	String hql = "select brandId from Product where categoryId IN :ids " ;
		        	Map<String,Object> params = new HashMap<String,Object>();
		        	params.put("ids", categoryIds);
		        	List<Integer> brandIds= productService.list(hql,Integer.class,params);
		        	Set<Integer> intBrandIds = new HashSet<Integer>() ;
		        	for(Integer brId : brandIds) {
		        		if(null == brId) {
		        			continue ;
		        		}
		        		intBrandIds.add(brId);
		        	}
		        	params.clear(); 
		        	if(intBrandIds.size()>0) {
			        	String  brandId= intBrandIds.toString();
			        	String  bString=brandId.substring(1,brandId.length()-1);
			        	List<Brand> brands = brandService.list("from Brand where id in ("+bString+") order by sort") ;
			        	return brands ;
		        	}
		        }
	        }
		}
		return null;
	}
	
//	/**
//	 * 客户端首页展示
//	 * 
//	 * @return
//	 */
//	@Cacheable(value = "pageproductParamNavigationCache", key = "#root.targetClass + #root.methodName + #id")
//	public List<ProductParam> queryproductParamByNavigation(Integer id) {
//		PageNavigation item = get(id) ;
//		if(null != item) {
//			String content = item.getContent() ;
//			Category
//	        if(null != content && !"".equals(content)) {
//	        	JSONObject json = JSON.parseObject(content) ;
//		        if(json.containsKey("category")) {
//		        	JSONArray jsarray = json.getJSONArray("category") ;
//		        	List<String> categoryIds = JSONArray.parseArray(jsarray.toJSONString(), String.class) ;
//		        	String hql = "select brandIds from Category where id IN :ids " ;
//		        	Map<String,Object> params = new HashMap<String,Object>();
//		        	params.put("ids", categoryIds);
//		        	List<String> brandIds= categoryService.list(hql,String.class,params);
//		        	Set<Integer> intBrandIds = new HashSet<Integer>() ;
//		        	for(String brId : brandIds) {
//		        		if(null == brId) {
//		        			continue ;
//		        		}
//		        		Integer[] intBrids = ConvertUtils.parseIntArr(brId) ;
//		        		intBrandIds.addAll(Arrays.asList(intBrids)) ;
//		        	}
//		        	params.clear(); 
//		        	if(intBrandIds.size()>0) {
//			        	params.put("ids", new ArrayList<Integer>(intBrandIds));
//			        	List<Brand> brands = brandService.list("from Brand where id in :ids",params) ;
//			        	return brands ;
//		        	}
//		        }
//	        }
//		}
//	
//		return null;
//	}
	
	/**
	 * 客户端首页展示
	 * 
	 * @return
	 */
	@Cacheable(value = "pageNavigationCache", key = "#root.targetClass + #root.methodName")
	public List<PageNavigation> listAll() {
		List<PageNavigation> list = list("from PageNavigation  order by sort asc");
		return list ;
	}
	
	
	public Map<Integer, PageNavigation> mapCategory(String loc,Integer level) {
		List<PageNavigation> listAll = listAll();
		Map<Integer, PageNavigation> map = new LinkedHashMap<>();
		for (PageNavigation c : listAll) {
			if(c.getLevel().equals(level) && c.getLoc().equals(loc)) {
				map.put(c.getId(), c);
			}
		}
		return map;
	}

	
	public List<Map<String, Object>> pageCata(String loc) {
		Map<Integer, PageNavigation> mapCategory1 = mapCategory(loc,1);
		Map<Integer, PageNavigation> mapCategory2 = mapCategory(loc,2);
		Map<Integer, PageNavigation> mapCategory3 = mapCategory(loc,3);

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		for (PageNavigation category1 : mapCategory1.values()) {
			if (category1.getSort() < 0)
				continue;
			Map<String, Object> map1 = getCategoryMap(category1);
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			for (PageNavigation category2 : mapCategory2.values()) {
				if (category2.getSort() < 0)
					continue;
				if (category2.getPid().equals(category1.getId())) {
					Map<String, Object> map2 = getCategoryMap(category2);
					List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
					for (PageNavigation category3 : mapCategory3.values()) {
						if (category3.getSort() < 0)
							continue;
						if (category3.getPid().equals(category2.getId())) {
							Map<String, Object> map3 = getCategoryMap(category3);
							list3.add(map3);
						}
					}
					map2.put("list", list3);
					list2.add(map2);
				}
			}
			map1.put("list", list2);
			list1.add(map1);
		}
		return list1 ;
	}
	
	
	public Map<String, Object> getCategoryMap(PageNavigation category) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", category.getId());
		map.put("name", category.getName());
		map.put("pid", category.getPid());
		return map;
	}
	  
	
	 
	
	@CacheEvict(value="pageNavigationCache" , allEntries=true)
	public void add(PageNavigation entity) {
		pageNavigationDao.save(entity) ;
	}

	@CacheEvict(value="pageNavigationCache" , allEntries=true)
	public void del(Integer id) {
		pageNavigationDao.delete(id);
	}
}
