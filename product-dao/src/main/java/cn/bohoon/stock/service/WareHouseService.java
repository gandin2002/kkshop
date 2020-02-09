package cn.bohoon.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.WareHouseDao;
import cn.bohoon.stock.entity.WareHouse;

@Service
@Transactional
public class WareHouseService extends BaseService<WareHouse,Integer>{

	@Autowired
	WareHouseDao wareHouseDao;

    @Autowired
    WareHouseService(WareHouseDao wareHouseDao){
        super(wareHouseDao);
    }

}
