package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.CreditDao;
import cn.bohoon.company.entity.Credit;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CreditService extends BaseService<Credit,Integer>{

	@Autowired
	CreditDao creditDao;

    @Autowired
    CreditService(CreditDao creditDao){
        super(creditDao);
    }

    //信用修改
    public void  save(Credit credit) {
		creditDao.save(credit);    	
	}
    
    
    
}
