package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.CompanyIndustryDao;
import cn.bohoon.company.entity.CompanyIndustry;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CompanyIndustryService extends BaseService<CompanyIndustry,Integer>{

	@Autowired
	CompanyIndustryDao companyIndustryDao;

    @Autowired
    CompanyIndustryService(CompanyIndustryDao companyIndustryDao){
        super(companyIndustryDao);
    }

}
