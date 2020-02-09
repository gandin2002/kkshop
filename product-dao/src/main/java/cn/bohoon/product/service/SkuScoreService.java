package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.SkuScoreDao;
import cn.bohoon.product.entity.SkuScore;

/**
 * 积分商品sku 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class SkuScoreService extends BaseService<SkuScore,Integer>{

	@Autowired
	SkuScoreDao skuScoreDao;
	

    @Autowired
    SkuScoreService(SkuScoreDao skuScoreDao){
        super(skuScoreDao);
    }
    
}