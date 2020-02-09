package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.ProductInfoDao;
import cn.bohoon.product.entity.ProductInfo;

/**
 * 商品描述 服务层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ProductInfoService extends BaseService<ProductInfo,Integer>{

	@Autowired
	ProductInfoDao productInfoDao;

    @Autowired
    ProductInfoService(ProductInfoDao productInfoDao){
        super(productInfoDao);
    }
    
    @Cacheable(value="productInfoCache",key="#id" )
    @Override
    public ProductInfo get(Integer id){
    	return super.get(id);
    }
    
    @CacheEvict(value="productInfoCache",key="#productInfo.id" )
    @Override
    public void save(ProductInfo productInfo){
    	super.save(productInfo);
    }
    
}