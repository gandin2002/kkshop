package cn.bohoon.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.product.dao.CategoryDao;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;

@Service
@Transactional
public class CategoryService extends BaseService<Category, String> {

	@Autowired
	CategoryDao categoryDao;
	@Autowired
	ProductService productService;

	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	CategoryService(CategoryDao categoryDao) {
		super(categoryDao);
	}

	
	@Cacheable(value = "categoryCache", key = "#root.targetClass + #root.methodName")
	public List<Category> listAll(){
		String hql = "from Category order by level,sort" ;
		return list(hql) ;
	}

	/**
	 * 列表排序
	 * 
	 * @return
	 */
	public List<Category> categorysSorting() {
		List<Category> clist = listAll() ;
		List<Category> list = new ArrayList<>();
		for (Category category1 : clist) {
			if (category1.getLevel() == 1) {
				list.add(category1);
				for (Category category2 : clist) {
					if (category2.getPid().equals(category1.getId())) {
						list.add(category2);
						for (Category category3 : clist) {
							if (category3.getPid().equals(category2.getId())) {
								list.add(category3);
							}
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 可条件查询
	 * @param hql
	 * @param objects
	 * @return
	 */
	public Map<String,Object> categorysSortingS(String hql,Object...objects) {
		
		List<Category> clist =this.list(hql,objects);
		//一级分类
		List<Category> clistLv1 =new ArrayList<>();
		//二级分类
		List<Category> clistLv2 =new ArrayList<>();
		//三级分类
		List<Category> clistLv3 =new ArrayList<>();
		for (Category category : clist) {
			if(null!=category.getLevel() && category.getLevel().equals(1)){
				clistLv1.add(category);
			}else if(null!=category.getLevel() && category.getLevel().equals(2)){
				clistLv2.add(category);
			}else if(null!=category.getLevel() && category.getLevel().equals(3)){
				clistLv3.add(category);
			}
		}
	
		List<Category> list = new ArrayList<>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("resultDate", list);
		for (Category category1 : clistLv1) {
			if (category1.getLevel() == 1) {
				list.add(category1);
				Integer num = 0;
				for (Category category2 : clistLv2) {
					if (null != category2.getPid() && category2.getPid().equals(category1.getId())) {
						num++;
						list.add(category2);
						for (Category category3 : clistLv3) {
							if (null != category3.getPid() && category3.getPid().equals(category2.getId())) {
								num++;
								list.add(category3);
							}
						}
					}
				}
				map.put(category1.getId(), num);
			}
		}
		return map;
	}
	
	public String categorys() {
		Map<String, Category> mapCategory1 = mapCategory(1);
		Map<String, Category> mapCategory2 = mapCategory(2);
		Map<String, Category> mapCategory3 = mapCategory(3);

		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Category category1 : mapCategory1.values()) {
			if (category1.getSort() < 0)
				continue;
			Map<String, Object> map1 = getCategoryMap(category1);
			List<Map<String, Object>> list2 = new ArrayList<>();
			for (Category category2 : mapCategory2.values()) {
				if (category2.getSort() < 0)
					continue;
				if (category2.getPid().equals(category1.getId())) {
					Map<String, Object> map2 = getCategoryMap(category2);
					List<Map<String, Object>> list3 = new ArrayList<>();
					for (Category category3 : mapCategory3.values()) {
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
		return JsonUtil.toJson(list1);
	}

	public Map<String, Object> getCategoryMap(Category category) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", category.getId());
		map.put("name", category.getName());
		map.put("pid", category.getPid());
		return map;
	}
	
	public Map<String, Category> mapCategory(Integer level) {
		List<Category> listAll = listAll() ;
		Map<String, Category> map = new LinkedHashMap<>();
		for (Category c : listAll) {
			if(c.getLevel().equals(level)) {
			map.put(c.getId(), c);
			}
		}
		return map;
	}


	/**
	 * 修改分类状态
	 * @param categoryId 
	 * @param state 前台显示  0-不显示    1-显示
	 */
	public void modifyState(String categoryId, Integer state) {
		logger.info("----------分类改变状态:{}----------",state);
		categoryDao.execute(" update Category set display = "+state+" where  id = '"+categoryId+"'");
	}
	
	
	/**
	 * 递归----》 获取子分类的所有商品，如果子分类有商品，则显示返回状态1,
	 *          如果子分类都没有商品返回状态null
	 */
	
	public String getChildId(String categoryId){
		
		// 如果该分类没有商品，则判断该分类的子分类有没有商品，如果有，则不能删除
		String d_hql = "from Category where pid=?";
		
		
		// 先判断分类有没有商品
		
		List<Category> list = this.list(d_hql,categoryId);
		if (null != list && list.size() > 0){
			
			// 有子分类判断子分类中有没有商品
			for (Category c : list){
				
//				String ch = getChildId(c.getId());
				String c_hql = "from Product where categoryId=?";
				List pro = productService.list(c_hql, c.getId());
				
				if (null != pro && pro.size() > 0){
					return "1";
				}
				
				String ch = getChildId(c.getId());
				if ("1".equals( ch )){
					return ch;
				}
				
			}
		}else{
			
			return null;
		}
		
		return null;
	}
	

	 

}