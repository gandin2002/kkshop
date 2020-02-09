package cn.bohoon.order.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OfflineCreaditLogDao;
import cn.bohoon.order.entity.OfflineCreditLog;

@Service
@Transactional
public class OfflineCreaditLogService extends  BaseService<OfflineCreditLog, String>{
	List<OfflineCreditLog> result = Collections.emptyList();
	
    @Autowired
    OfflineCreaditLogDao  OfflineCreaditLogDao;
    
    @Autowired
    OfflineCreaditLogService(OfflineCreaditLogDao  OfflineCreaditLogDao){
        super(OfflineCreaditLogDao);
    }
	public List<OfflineCreditLog> getResult() {
		return result;
	}
	public void setResult(List<OfflineCreditLog> result) {
		this.result = result;
	}

}
