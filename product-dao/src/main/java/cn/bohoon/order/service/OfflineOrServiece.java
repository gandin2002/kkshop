package cn.bohoon.order.service;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.entity.OfflineOr;


@Service
@Transactional
public class OfflineOrServiece extends BaseService<OfflineOr, String>{
	List< OfflineOr> result = Collections.emptyList();
	
    @Autowired
    cn.bohoon.order.dao.OfflineOrDao  OfflineOrDao;
    @Autowired
    OfflineOrServiece(cn.bohoon.order.dao.OfflineOrDao  offlineOrderDao){
        super(offlineOrderDao);
    }
	public List<OfflineOr> getResult() {
		return result;
	}
	public void setResult(List< OfflineOr> result) {
		this.result = result;
	}
   
}
