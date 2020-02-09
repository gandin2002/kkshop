package cn.bohoon.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.company.dao.CompanyBankDao;
import cn.bohoon.company.entity.CompanyBank;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class CompanyBankService extends BaseService<CompanyBank,Integer>{

	@Autowired
	CompanyBankDao companyBankDao;

    @Autowired
    CompanyBankService(CompanyBankDao companyBankDao){
        super(companyBankDao);
    }
    /**
     * 保存并修改默认
     * @param companyBank
     */
	public void saveAndSetDefault(CompanyBank companyBank) {
		 boolean isdefault = companyBank.getIsDefault();
		 if(isdefault && !StringUtils.isEmpty(companyBank.getCompanyId())){ 
			 companyBankDao.execute(" update  CompanyBank set isDefault = 0  where companyId = ? ",companyBank.getCompanyId());
		 }
		 companyBankDao.save(companyBank);
	}
}
