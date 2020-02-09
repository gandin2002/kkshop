package cn.bohoon.distribution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.distribution.dao.DistributeDao;
import cn.bohoon.distribution.entity.Distribute;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class DistributeService extends BaseService<Distribute,Integer>{

	@Autowired
	DistributeDao distributeDao;

    @Autowired
    DistributeService(DistributeDao distributeDao){
        super(distributeDao);
    }

}
