package cn.bohoon.product.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.page.service.PageNavigationService;
import cn.bohoon.product.dao.BrandDao;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.util.ConvertUtils;

@Service
@Transactional
public class BrandService extends BaseService<Brand,Integer>{

	@Autowired
	BrandDao brandDao;
	
	@Autowired
	CategoryService categoryService ;
	
	@Autowired
	PageNavigationService pageNavigationService ;
	

    @Autowired
    BrandService(BrandDao brandDao){
        super(brandDao);
    }
    
    /**
     * 获取属性名称
     * 
     * @param ids
     * @return
     */
    public String getBrandNames(String ids) {
    	
    	Map<String,Object> params = new HashMap<String,Object>();
    	Integer [] intIds =  ConvertUtils.parseIntArr(ids) ;
    	params.put("ids", Arrays.asList(intIds));
    	List<String> names= list("select name from Brand where id IN :ids ",String.class,params);
    	return names.toString() ;
    	
    }
    
    
    public Map<String, List<Brand>>  getBrandsByCategory() {
		Map<String, List<Brand>> brandMap = new HashMap<>();
		List<Category> categorys = categoryService.list(" from Category where display=?",true) ;
		for(Category category : categorys ) {
			String hql = " select b from Brand b ,Product p where b.id = p.brandId and p.categoryId=? " ;
			List<Brand> brands = list(hql,category.getId())  ;
			brandMap.put(category.getId(), brands) ;
		}
		return brandMap;
	}

}