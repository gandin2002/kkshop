package cn.bohoon.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.entity.ShoppingCart;
import cn.bohoon.product.dao.ProductCollectionDao;
import cn.bohoon.product.entity.ProductCollection;

@Service
@Transactional
public class ProductCollectionService extends BaseService<ProductCollection,Integer> {
	
	@Autowired
	ProductCollectionDao productCollectionDao;
	
	 @Autowired
	 public ProductCollectionService(ProductCollectionDao productCollectionDao) {
		 super(productCollectionDao);
	 }
	 
	 public String getProductCollection(String userId,Integer productId){
		 List<ProductCollection> list = getProductCollections(userId,productId);
		 if(list.size()>0){
			 return "ProductCollection";
		 }
	 
		 return "";
	 }
	 
	 public synchronized void addCart(ProductCollection productCollection) {
	    	ProductCollection model = select(" from ProductCollection where userId=? and productId=?", productCollection.getUserId(),productCollection.getProductId()) ;
	    	if(null == model ) {
	    		productCollectionDao.save(productCollection) ;
	    	} else {
	    		model.setProductnum(productCollection.getProductnum()+model.getProductnum());
	    		productCollectionDao.save(model) ;
	    	}
	    }
	 
	 public List<ProductCollection> getProductCollections(String userId,Integer productId) {
			String hql = "from ProductCollection where userId=? and productId=?" ;
			List<ProductCollection> list = list(hql,userId,productId) ;
			return list ;
		}
//	 @CacheEvict(value="ProductCollectionCache" ,key = "#userId")
//	 public void sava(String userId,Integer productId){
//		 Inte	Integer.parseInt(userId);
//		 ProductCollection	entity	=new ProductCollection(userId,productId);
//		 productCollectionDao.save();
//	 }
	 
}
