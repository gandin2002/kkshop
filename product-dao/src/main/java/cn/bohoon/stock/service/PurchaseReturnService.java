package cn.bohoon.stock.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.PurchaseReturnDao;
import cn.bohoon.stock.domain.Purch;
import cn.bohoon.stock.entity.PurchReturnItem;
import cn.bohoon.stock.entity.Purchase;
import cn.bohoon.stock.entity.PurchaseReturn;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Service
@Transactional
public class PurchaseReturnService extends BaseService<PurchaseReturn, String> {

	@Autowired
	PurchReturnItemService service;
	@Autowired
	PurchaseService purchaseService ;
	@Autowired
	PurchaseReturnDao purchaseReturnDao;

	@Autowired
	PurchaseReturnService(PurchaseReturnDao purchaseReturnDao) {
		super(purchaseReturnDao);
	}

	public String save(Purch purch) {
		PurchaseReturn purchaseReturn = purch.getPurchaseReturn() ;
		if(StringUtils.isEmpty(purchaseReturn.getPurchaseId())) {
			return ResultUtil.getError("请将信息填写完整！") ;
		}
		Purchase purchase = purchaseService.get(purchaseReturn.getPurchaseId()) ;
		purchaseReturn.setSupplierId(purchase.getSupplierId());
		purchaseReturn.setSupplierName(purchase.getSupplierName());
		purchaseReturn.setWareHouseId(purchase.getWareHouseId());
		purchaseReturn.setWareHouseName(purchase.getWareHouseName());
		
		String id = purchaseReturn.getId();
		if (StringUtils.isEmpty(id)) {
			id = IDUtil.getInstance().getIdByDb(this, PurchaseReturn.class, "TG", null);
			purchaseReturn.setId(id);
		}
		List<PurchReturnItem> items = purch.getItemReturn() ;

		List<PurchReturnItem> saveItems = new ArrayList<PurchReturnItem>();
		int totalNum = 0;
		BigDecimal total = new BigDecimal(0);
		for (PurchReturnItem item : items) {
			if (StringUtils.isEmpty(item.getQuantity()) || item.getQuantity() == 0) {
				continue;
			}
			totalNum += item.getQuantity();
			total = total.add(item.getFactPrice().multiply(new BigDecimal(item.getQuantity())));
			item.setPurchaseId(id);
			saveItems.add(item);
		}
		int len = saveItems.size();
		if (len == 0) {
			return ResultUtil.getError("请将信息填写完整！");
		}
		service.saveBatch(saveItems, len) ;
		purchaseReturn.setTotalNum(totalNum);
		purchaseReturn.setTotal(total);
		purchaseReturnDao.save(purchaseReturn);
		return ResultUtil.getSuccessMsg();
	}

}
