package cn.bohoon.distribution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.distribution.dao.ExpfeeCityDao;
import cn.bohoon.distribution.entity.ExpfeeCity;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class ExpfeeCityService extends BaseService<ExpfeeCity,Integer>{

	@Autowired
	ExpfeeCityDao expfeeCityDao;

    @Autowired
    ExpfeeCityService(ExpfeeCityDao expfeeCityDao){
        super(expfeeCityDao);
    }

}
