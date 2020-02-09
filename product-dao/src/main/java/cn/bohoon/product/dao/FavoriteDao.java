package cn.bohoon.product.dao;

import org.springframework.stereotype.Repository;
import cn.bohoon.framework.orm.jpa.AbstractDao;
import cn.bohoon.product.entity.Favorite;

/**
 * @author admin
 * 货品收藏
 */
@Repository
public class FavoriteDao extends AbstractDao<Favorite, Integer>{

}