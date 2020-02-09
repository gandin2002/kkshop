package cn.bohoon.userInfo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.userInfo.dao.CreditLogDao;
import cn.bohoon.userInfo.entity.CreditLog;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CreditLogService extends BaseService<CreditLog,Integer>{

	@Autowired
	CreditLogDao creditLogDao;

    @Autowired
    CreditLogService(CreditLogDao creditLogDao){
        super(creditLogDao);
    }

}
