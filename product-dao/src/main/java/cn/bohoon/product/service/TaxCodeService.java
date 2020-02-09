package cn.bohoon.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.TaxCodeDao;
import cn.bohoon.product.entity.TaxCode;

@Service
@Transactional
public class TaxCodeService extends BaseService<TaxCode,Integer>{

	@Autowired
	TaxCodeDao taxCodeDao;

    @Autowired
    TaxCodeService(TaxCodeDao taxCodeDao){
        super(taxCodeDao);
    }
    
    /**
     * 获取默认税码
     * @return
     */
    public TaxCode findDefaultTax(){
    	List<TaxCode> TaxCodeArray =  taxCodeDao.findAll(" from TaxCode where state = 1");
    	if(!TaxCodeArray.isEmpty()){
    		return TaxCodeArray.get(TaxCodeArray.size()-1);
    	}
    	return null;
    }

}
