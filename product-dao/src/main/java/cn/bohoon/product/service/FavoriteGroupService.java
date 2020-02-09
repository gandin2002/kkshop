package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.FavoriteGourpDao;
import cn.bohoon.product.entity.FavoriteGroup;

/**
 * 
 * 收藏夹分类 服务层
 * @author Administrator
 *
 */
@Service
@Transactional
public class FavoriteGroupService extends BaseService<FavoriteGroup, Integer> {

	@Autowired
	FavoriteGourpDao favoriteGroupDao;
	@Autowired
	FavoriteGroupService(FavoriteGourpDao FavoriteGourpDao) {
		super(FavoriteGourpDao);
	}

	//public void savaFavor
	
}
