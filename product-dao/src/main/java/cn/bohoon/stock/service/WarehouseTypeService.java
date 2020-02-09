package cn.bohoon.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.WarehouseTypeDao;
import cn.bohoon.stock.entity.WarehouseType;

@Service
@Transactional
public class WarehouseTypeService extends BaseService<WarehouseType,String>{

	@Autowired
	WarehouseTypeDao sarehouseTypeDao;

    @Autowired
    WarehouseTypeService(WarehouseTypeDao sarehouseTypeDao){
        super(sarehouseTypeDao);
    }

}
