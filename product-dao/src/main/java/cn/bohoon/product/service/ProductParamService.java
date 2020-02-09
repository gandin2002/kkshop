package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.ProductParamDao;
import cn.bohoon.product.entity.ProductParam;
/**
 * 属性信息   服务层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ProductParamService extends BaseService<ProductParam,Integer>{

	@Autowired
	ProductParamDao productParamDao;

    @Autowired
    ProductParamService(ProductParamDao productParamDao){
        super(productParamDao);
    }
    
    public ProductParam getProductParam(Integer productId){
    	String hql="from ProductParam where productId=?";
    	return productParamDao.select(hql,productId );
    }
    /**
	 * 客户端首页展示
	 * 
	 * @param id
	 * @return
	 */
}