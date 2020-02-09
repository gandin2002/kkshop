package cn.bohoon.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.FavoriteGourpLogDao;
import cn.bohoon.product.entity.FavoriteGroupLog;

/**
 * 收藏夹信息 服务层
 * @author Administrator
 *
 */
@Service
@Transactional
public class FavoriteGroupLogService extends BaseService<FavoriteGroupLog, Integer>{
	@Autowired
	FavoriteGourpLogDao favoriteGroupLogDao;
	@Autowired
	FavoriteGroupLogService(FavoriteGourpLogDao FavoriteGourpLogDao) {
		super(FavoriteGourpLogDao);
	}
	
	//删除分组中的数据
	public void delAll(List<FavoriteGroupLog> a){
		for (FavoriteGroupLog favoriteGroupLog : a) {
			favoriteGroupLogDao.delete(favoriteGroupLog);
		}
	}
	
	//想分组中添加商品收藏时 判断是否已经存在
	public boolean addIsExist(FavoriteGroupLog favoriteGroupLog){
		String hql="from FavoriteGroupLog where groupId=? and productId=?";
		FavoriteGroupLog favpLog=favoriteGroupLogDao.select(hql, favoriteGroupLog.getGroupId(),favoriteGroupLog.getProductId());
		if(favpLog!=null){
			return true;
		}
		return false;
	}
	
	
}
