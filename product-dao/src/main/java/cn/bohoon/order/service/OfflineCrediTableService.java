package cn.bohoon.order.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;

import cn.bohoon.order.dao.OfflineCrediTableDao;
import cn.bohoon.order.entity.OfflineCrediTable;


@Service
@Transactional
public class OfflineCrediTableService extends BaseService<OfflineCrediTable, String>{
List<OfflineCrediTable> result = Collections.emptyList();
	
    @Autowired
   OfflineCrediTableDao  OfflineCrediTableDao;
    
    @Autowired
    OfflineCrediTableService(OfflineCrediTableDao  OfflineCrediTableDao){
        super(OfflineCrediTableDao);
    }
	public List<OfflineCrediTable> getResult() {
		return result;
	}
	public void setResult(List<OfflineCrediTable> result) {
		this.result = result;
	}

	
}
