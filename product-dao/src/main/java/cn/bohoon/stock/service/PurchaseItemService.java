package cn.bohoon.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.PurchaseItemDao;
import cn.bohoon.stock.entity.PurchaseItem;

@Service
@Transactional
public class PurchaseItemService extends BaseService<PurchaseItem, Integer> {

	@Autowired
	PurchaseItemDao purchaseItemDao;

	@Autowired
	PurchaseItemService(PurchaseItemDao purchaseItemDao) {
		super(purchaseItemDao);
	}

}
