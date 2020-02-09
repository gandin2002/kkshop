package cn.bohoon.order.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.MymaintenceDao;
import cn.bohoon.order.entity.Mymaintenance;

@Service
@Transactional
public class MymaintenanceService extends BaseService<Mymaintenance, String>{
	List<Mymaintenance> result = Collections.emptyList();
	
    @Autowired
    MymaintenceDao  mymaintenceDao;
    @Autowired
    MymaintenanceService(MymaintenceDao  mymaintenceDao){
        super(mymaintenceDao);
    }
	public List<Mymaintenance> getResult() {
		return result;
	}
	public void setResult(List<Mymaintenance> result) {
		this.result = result;
	}
    
    
}
