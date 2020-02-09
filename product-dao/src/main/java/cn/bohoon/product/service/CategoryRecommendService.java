package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.CategoryRecommendDao;
import cn.bohoon.product.entity.CategoryRecommend;

@Service
@Transactional
public class CategoryRecommendService extends BaseService<CategoryRecommend, Integer> {
	@Autowired
	CategoryRecommendDao categoryRecommendDao;
	
	@Autowired
	public CategoryRecommendService(CategoryRecommendDao categoryRecommendDao) {
		super(categoryRecommendDao);
	}
	
	
}
