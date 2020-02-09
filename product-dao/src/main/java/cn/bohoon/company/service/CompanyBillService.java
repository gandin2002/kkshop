package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.CompanyBillDao;
import cn.bohoon.company.entity.CompanyBill;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CompanyBillService extends BaseService<CompanyBill,String>{

	@Autowired
	CompanyBillDao companyBillDao;

    @Autowired
    CompanyBillService(CompanyBillDao companyBillDao){
        super(companyBillDao);
    }

}
