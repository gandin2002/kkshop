package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.company.dao.LicenseDao;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.License;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class LicenseService extends BaseService<License,String>{

	@Autowired
	LicenseDao licenseDao;
	
	@Autowired
	CompanyService companyService;

    @Autowired
    LicenseService(LicenseDao licenseDao){
        super(licenseDao);
    }
    
    public void saveLiceseUpdateCompany(License license){
		String companyid = license.getCompanyid();
		Company  company= companyService.select(" from Company where id = ? ",companyid);
		execute(" delete License where companyid = ?  ", license.getCompanyid());
		 
		license.setId(license.getRegistration());
		company.setLicenseId(license.getRegistration());
		company.setLicenseImage(license.getImage());
		
        save(license);
        companyService.update(company);
    }

}
