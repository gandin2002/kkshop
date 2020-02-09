package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.ProductRecommendDao;
import cn.bohoon.product.entity.ProductRecommend;

/**
 * 推荐商品  服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class ProductRecommendService extends BaseService<ProductRecommend, Integer> {
	@Autowired
	ProductRecommendDao productRecommendDao;
	
	@Autowired
	public ProductRecommendService(ProductRecommendDao productRecommendDao) {
		super(productRecommendDao);
	}
	
}
