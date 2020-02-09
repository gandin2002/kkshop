package cn.bohoon.distribution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.distribution.dao.ExpfeeOrderDao;
import cn.bohoon.distribution.entity.ExpfeeOrder;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class ExpfeeOrderService extends BaseService<ExpfeeOrder,Integer>{

	@Autowired
	ExpfeeOrderDao expfeeOrderDao;

    @Autowired
    ExpfeeOrderService(ExpfeeOrderDao expfeeOrderDao){
        super(expfeeOrderDao);
    }

}
