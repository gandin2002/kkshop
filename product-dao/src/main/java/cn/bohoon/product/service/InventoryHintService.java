package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.InventoryHintDao;
import cn.bohoon.product.entity.InventoryHint;

@Service
@Transactional
public class InventoryHintService extends BaseService<InventoryHint,Integer>{

	@Autowired
	InventoryHintDao inventoryHintDao;

    @Autowired
    InventoryHintService(InventoryHintDao inventoryHintDao){
        super(inventoryHintDao);
    }

}
