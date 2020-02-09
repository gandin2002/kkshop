package cn.bohoon.product.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.ProductRelevanceDao;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductRelevance;
import cn.bohoon.util.ConvertUtils;

@Service
@Transactional
public class ProductRelevanceService extends BaseService<ProductRelevance, Integer> {

	@Autowired
	ProductService productService ;
	@Autowired
	ProductRelevanceDao productRelevanceDao;

	@Autowired
	ProductRelevanceService(ProductRelevanceDao productRelevanceDao) {
		super(productRelevanceDao);
	}

	public List<Product> findRelevanceProducts(Integer id) {
		String hql1 = " from ProductRelevance where state = 1" ;
		List<ProductRelevance> list = list(hql1) ;
		Set<Integer> pIds = new HashSet<Integer>() ;
		for(ProductRelevance pr : list ) {
			Integer [] prIds = ConvertUtils.parseIntArr(pr.getProductIds()) ;
			Set<Integer> set = new HashSet<Integer>(Arrays.asList(prIds));
			if(set.contains(id)) {
				pIds.addAll(set) ;
			}
		}
		if(pIds.size() >0 ) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("ids", new ArrayList<Integer>(pIds));
			Page<Product> proPage = new Page<>() ;
			String hql = " from Product where id IN :ids and id!="+id ;
			return productService.page(proPage,hql,params).getResult() ;
		}
		return null;
	}

}
