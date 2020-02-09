package cn.bohoon.serve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.serve.dao.SaleDao;
import cn.bohoon.serve.entity.Sale;

@Service
@Transactional
public class SaleService extends BaseService<Sale,Integer>{

	@Autowired
	SaleDao saleDao;

    @Autowired
    SaleService(SaleDao saleDao){
        super(saleDao);
    }

}
