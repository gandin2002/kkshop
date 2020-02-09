package cn.bohoon.stock.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.stock.dao.PurchaseDao;
import cn.bohoon.stock.domain.Purch;
import cn.bohoon.stock.entity.Purchase;
import cn.bohoon.stock.entity.PurchaseItem;
import cn.bohoon.util.IDUtil;
import cn.bohoon.util.ResultUtil;

@Service
@Transactional
public class PurchaseService extends BaseService<Purchase,String>{

	@Autowired
	PurchaseDao purchaseDao;
	@Autowired
	PurchaseItemService purchaseItemService ;

    @Autowired
    PurchaseService(PurchaseDao purchaseDao){
        super(purchaseDao);
    }

	public String save(Purch purch) {
		Purchase purchase = purch.getPurchase() ;

		String id = purchase.getId() ;
		if(StringUtils.isEmpty(id)) {
			id = IDUtil.getInstance().getIdByDb(this, Purchase.class, "CG", null) ;
			purchase.setId(id);
		}
		purchaseItemService.execute("delete from PurchaseItem where purchaseId=? ", id) ;
		
		List<PurchaseItem> items = purch.getItem() ;
		List<PurchaseItem> saveItems = new ArrayList<PurchaseItem>() ;
		int totalNum = 0 ;
		BigDecimal total = new BigDecimal(0) ;
		for(PurchaseItem item : items ) {
			if(StringUtils.isEmpty(item.getQuantity()) || item.getQuantity() ==0 ) {
				continue ;
			}
			totalNum += item.getQuantity() ;
			total = total.add(item.getFactPrice().multiply(new BigDecimal(item.getQuantity()))) ;
			item.setPurchaseId(id);
			saveItems.add(item) ;
		}
		int len = saveItems.size() ;
		if(len == 0) {
			return ResultUtil.getError("请将信息填写完整！") ;
		}
		purchaseItemService.saveBatch(saveItems, len);
		purchase.setTotalNum(totalNum);
		purchase.setTotal(total);
		purchaseDao.save(purchase) ;
		return ResultUtil.getSuccessMsg() ;
	}

}
