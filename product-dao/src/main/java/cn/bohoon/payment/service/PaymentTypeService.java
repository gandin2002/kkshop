package cn.bohoon.payment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.payment.dao.PaymentTypeDao;
import cn.bohoon.payment.entity.PaymentType;

@Service
@Transactional
public class PaymentTypeService extends BaseService<PaymentType,Integer>{

	@Autowired
	PaymentTypeDao paymentTypeDao;

    @Autowired
    PaymentTypeService(PaymentTypeDao paymentTypeDao){
        super(paymentTypeDao);
    }

    public PaymentType selectByCode(String code) {
    	String hql = " from PaymentType where code=? " ;
    	List<PaymentType> pays = paymentTypeDao.findAll(hql, code) ;
    	if(pays.size() > 0 ) {
    		return pays.get(0) ;
    	}
    	return null ;
    } 
}
