package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderItemDao;
import cn.bohoon.order.dao.PresetOrderItemDao;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.PresetOrderItem;

@Service
@Transactional
public class PresetOrderItemService extends BaseService<PresetOrderItem,Integer>{

	@Autowired
	PresetOrderItemDao presetOrderItemDao;

    @Autowired
    PresetOrderItemService(PresetOrderItemDao presetOrderItemDao){
        super(presetOrderItemDao);
    }

}
