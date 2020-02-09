package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.CompanyLevelDao;
import cn.bohoon.company.entity.CompanyLevel;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CompanyLevelService extends BaseService<CompanyLevel,Integer>{

	@Autowired
	CompanyLevelDao companyLevelDao;

    @Autowired
    CompanyLevelService(CompanyLevelDao companyLevelDao){
        super(companyLevelDao);
    }

}
