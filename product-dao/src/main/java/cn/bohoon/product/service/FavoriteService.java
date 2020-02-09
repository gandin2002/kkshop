package cn.bohoon.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.FavoriteDao;
import cn.bohoon.product.entity.Favorite;

/**
 * 商品收藏 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class FavoriteService extends BaseService<Favorite, Integer> {

	@Autowired
	FavoriteDao favoriteDao;

	@Autowired
	FavoriteService(FavoriteDao favoriteDao) {
		super(favoriteDao);
	}
	
	
	
	
	public String getFavorite(String userId,Integer productId) {
		List<Favorite> list = getFavorites(userId,productId) ;
		if(list.size() > 0) {
			return "favorited" ;
		}
		return "" ;
	}
	
	public void removeListFavorite(String userId,String[] idss){
		for (String productId : idss) {
			List<Favorite> list = getFavorites(userId,Integer.parseInt(productId)) ;
			for (Favorite favorite2 : list) {
				favoriteDao.delete(favorite2);
			}
		}
	}
	
	
	public void removeFavorite(String userId,Integer productId) {
		List<Favorite> list = getFavorites(userId,productId) ;
		for(Favorite favorite : list) {
			favoriteDao.delete(favorite);
		}
	}
	
	public List<Favorite> getFavorites(String userId,Integer productId) {
		String hql = "from Favorite where userId=? and productId=?" ;
		List<Favorite> list = list(hql,userId,productId) ;
		return list ;
	}

	
	@Cacheable(value="favoriteCache" ,key = "#userId")
	public List<Favorite> getUserFavorites(String userId) {
		String hql = "from Favorite where userId=? " ;
		List<Favorite> list = list(hql,userId) ;
		return list ;
	}
	
	@CacheEvict(value="favoriteCache" ,key = "#userId")
	public void save(String userId,Integer productId) {
		Favorite entity = new Favorite(userId,productId) ;
		favoriteDao.save(entity);
	}
	 
	
	@CacheEvict(value="favoriteCache" , allEntries=true)
	public void delete(Integer userId,Integer productId) {
		String hql = "from Favorite where userId=? and productId=?" ;
		List<Favorite> list = list(hql,userId,productId) ;
		for(Favorite entity : list) {
			favoriteDao.delete(entity);
		}
	}
}