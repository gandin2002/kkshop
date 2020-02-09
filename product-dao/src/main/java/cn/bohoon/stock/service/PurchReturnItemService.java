package cn.bohoon.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.PurchReturnItemDao;
import cn.bohoon.stock.entity.PurchReturnItem;

@Service
@Transactional
public class PurchReturnItemService extends BaseService<PurchReturnItem, Integer> {

	@Autowired
	PurchReturnItemDao purchReturnItemDao;

	@Autowired
	PurchReturnItemService(PurchReturnItemDao purchReturnItemDao) {
		super(purchReturnItemDao);
	}

}
