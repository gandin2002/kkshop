package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;

import cn.bohoon.order.dao.MymanenceCompanyDao;
import cn.bohoon.order.entity.MymanenceCompany;
@Service
@Transactional
public class MymainCompanyService  extends BaseService<MymanenceCompany,String>{
	 @Autowired
	    MymanenceCompanyDao mymanenceCompanyDao;
	    @Autowired
	    MymainCompanyService (MymanenceCompanyDao   mymanenceCompanyDao){
	        super(mymanenceCompanyDao);
	    }
}
