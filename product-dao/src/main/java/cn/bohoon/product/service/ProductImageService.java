package cn.bohoon.product.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.ProductImageDao;
import cn.bohoon.product.entity.ProductImage;

/**
 * 商品图片 服务层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ProductImageService extends BaseService<ProductImage,Integer> {
	
	@Autowired
	ProductImageDao productImageDao;
	
    @Autowired
    ProductImageService(ProductImageDao ProductImageDao){
        super(ProductImageDao);
    }
    
    //@Cacheable(value="productImageCache",key="#id",unless="#result.size() == 0" )
    public List<ProductImage> getProductImage(Integer id){
    	Page<ProductImage> page = new Page<>();
		return page(page,"from ProductImage where productId= ?",id).getResult();
    }
    
    //@CacheEvict(value="productImageCache",key="#list.get(0).id")
    @Override
    public void saveBatch(List<ProductImage> list, Integer count) {
		super.saveBatch(list, count);
	}
    
	
}
