package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.SkuAttrDao;
import cn.bohoon.product.entity.SkuAttr;

@Service
@Transactional
public class SkuAttrService extends BaseService<SkuAttr,Integer>{

	@Autowired
	SkuAttrDao skuAttrDao;

    @Autowired
    SkuAttrService(SkuAttrDao skuAttrDao){
        super(skuAttrDao);
    }
    
}