package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.PlaceProductDao;
import cn.bohoon.product.entity.PlaceProduct;

/**
 * 商品推荐 服务层
 * @author Administrator
 *
 */
@Service
@Transactional
public class PlaceProductService extends BaseService<PlaceProduct,Integer>{

	@Autowired
	PlaceProductDao placeProductDao;

    @Autowired
    PlaceProductService(PlaceProductDao placeProductDao){
        super(placeProductDao);
    }
    
}