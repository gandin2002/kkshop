package cn.bohoon.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.company.dao.CompanyDeptNoExamineDao;
import cn.bohoon.company.entity.CompanyDeptNoExamine;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.userInfo.entity.UserInfo;



@Service
@Transactional
public class CompanyDeptNoExamineService extends BaseService<CompanyDeptNoExamine,Integer>{

	@Autowired
	CompanyDeptNoExamineDao companyDeparmentAndRPDao;
	
	@Autowired
	CompanyDeptNoExamineService(CompanyDeptNoExamineDao companyDeparmentAndRPDao){
		
		super(companyDeparmentAndRPDao);
		
	}
	
	
}
