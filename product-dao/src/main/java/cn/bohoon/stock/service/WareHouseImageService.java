package cn.bohoon.stock.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bohoon.framework.orm.domain.Page;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.entity.ProductImage;
import cn.bohoon.stock.dao.WareHouseImageDao;
import cn.bohoon.stock.entity.WareHouseImage;

/**
 * 用于门店的图片，显示层
 * @author Administrator
 *
 */
@Service
@Transactional
public class WareHouseImageService extends BaseService<WareHouseImage,Integer>{
	
	
	@Autowired
	WareHouseImageDao wareHouseImageDao;

	@Autowired
	 WareHouseImageService(WareHouseImageDao wareHouseImageDao) {
		super(wareHouseImageDao);
	}
	
    public List<WareHouseImage> getWareHouseImage(Integer id){
    	Page<WareHouseImage> page = new Page<>();
		return page(page,"from WareHouseImage where wareHouseId= ?",id).getResult();
    }
    
    @Override
    public void saveBatch(List<WareHouseImage> list, Integer count) {
		super.saveBatch(list, count);
	}
    

	
	
	

}
