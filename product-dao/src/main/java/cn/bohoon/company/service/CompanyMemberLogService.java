package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.CompanyMemberLogDao;
import cn.bohoon.company.entity.CompanyMemberLog;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CompanyMemberLogService  extends BaseService<CompanyMemberLog,Integer> {

	@Autowired
	CompanyMemberLogDao companyMemberLogDao;
	
	@Autowired
	CompanyMemberLogService(CompanyMemberLogDao companyMemberLogDao) {
		super(companyMemberLogDao);
	}
}
