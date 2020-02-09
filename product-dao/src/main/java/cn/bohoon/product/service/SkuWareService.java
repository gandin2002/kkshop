package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.SkuWareDao;
import cn.bohoon.product.entity.SkuWare;

/**
 * 货品仓库信息 服务层
 * 
 * @author dujianqiao
 *
 */
@Service
@Transactional
public class SkuWareService extends BaseService<SkuWare,Integer>{

	@Autowired
	SkuWareDao skuWareDao;

    @Autowired
    SkuWareService(SkuWareDao skuWareDao){
        super(skuWareDao);
    }
    
}