package cn.bohoon.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.ShoppingCartDao;
import cn.bohoon.order.entity.ShoppingCart;
import cn.bohoon.util.ConvertUtils;

@Service
@Transactional
public class ShoppingCartService extends BaseService<ShoppingCart,Integer>{

	@Autowired
	ShoppingCartDao shoppingCartDao;

    @Autowired
    ShoppingCartService(ShoppingCartDao shoppingCartDao){
        super(shoppingCartDao);
    }

    public synchronized void addCart(ShoppingCart entity) {
    	ShoppingCart model = select(" from ShoppingCart where userId=? and skuId=?", entity.getUserId(),entity.getSkuId()) ;
    	if(null == model ) {
    		shoppingCartDao.save(entity) ;
    	} else {
    		model.setQuantity(entity.getQuantity()+model.getQuantity());
    		shoppingCartDao.save(model) ;
    	}
    }

	public void delete(String userId, String skuIds) {
		Integer[] ids = ConvertUtils.parseIntArr(skuIds) ;
		for(Integer skuId : ids) {
			delete(userId,skuId) ;
		}
		
	}
	
	public void delete(String userId ,Integer skuId) {
		shoppingCartDao.execute(" delete from ShoppingCart where userId=? and skuId=?", userId,skuId) ;
	}

	public ShoppingCart findCart(String userId, Integer id) {
		ShoppingCart model = select(" from ShoppingCart where userId=? and skuId=?", userId,id) ;
		return model ;
	}
	
	public List<ShoppingCart> findCarts(String userId) {
		List<ShoppingCart> result = list(" from ShoppingCart where userId=? ", userId) ;
		return result ;
	}
}
