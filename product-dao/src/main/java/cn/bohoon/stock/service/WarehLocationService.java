package cn.bohoon.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.WarehLocationDao;
import cn.bohoon.stock.entity.WarehLocation;

@Service
@Transactional
public class WarehLocationService extends BaseService<WarehLocation,Integer>{

	@Autowired
	WarehLocationDao warehLocationDao;

    @Autowired
    WarehLocationService(WarehLocationDao warehLocationDao){
        super(warehLocationDao);
    }

}
