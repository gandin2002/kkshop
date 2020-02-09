package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.ProductCollectionSortDao;
import cn.bohoon.product.entity.ProductCollectionSort;

@Service
@Transactional
public class ProductCollectionSortService extends BaseService<ProductCollectionSort,Integer>{

	@Autowired
	ProductCollectionSortDao ProductCollectionSortDao;
}
