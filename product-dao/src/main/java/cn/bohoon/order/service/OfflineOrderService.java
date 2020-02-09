package cn.bohoon.order.service;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OfflineOrderDao;
import cn.bohoon.order.entity.OfflineOrder;
@Service
@Transactional
public class OfflineOrderService extends BaseService<OfflineOrder, String>{
	List<OfflineOrder> result = Collections.emptyList();
	
    @Autowired
    OfflineOrderDao  OfflineOrderDao;
    @Autowired
    OfflineOrderService(OfflineOrderDao  offlineOrderDao){
        super(offlineOrderDao);
    }
	public List<OfflineOrder> getResult() {
		return result;
	}
	public void setResult(List<OfflineOrder> result) {
		this.result = result;
	}
    
	
}