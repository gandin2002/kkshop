package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.SkuWareLocationDao;
import cn.bohoon.product.entity.SkuWareLocation;

/**
 * 货品库位 服务层
 * 
 * @author dujianqiao
 *
 */
@Service
@Transactional
public class SkuWareLocationService extends BaseService<SkuWareLocation,Integer>{

	@Autowired
	SkuWareLocationDao skuWareLocationDao;

    @Autowired
    SkuWareLocationService(SkuWareLocationDao skuWareLocationDao){
        super(skuWareLocationDao);
    }
    
    public	SkuWareLocation getSkuWareLocation(Integer skuId){
    String hql="from SkuWareLocation where skuId=?";
    return skuWareLocationDao.select(hql,skuId);
    }
    
}