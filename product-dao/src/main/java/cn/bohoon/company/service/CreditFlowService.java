package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.CreditFlowDao;
import cn.bohoon.company.entity.CreditFlow;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CreditFlowService extends BaseService<CreditFlow,Integer>{

	@Autowired
	CreditFlowDao creditFlowDao;

    @Autowired
    CreditFlowService(CreditFlowDao creditFlowDao){
        super(creditFlowDao);
    }

}
