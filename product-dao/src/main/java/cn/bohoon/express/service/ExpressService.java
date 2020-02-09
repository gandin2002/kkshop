package cn.bohoon.express.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.express.dao.ExpressDao;
import cn.bohoon.express.entity.Express;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class ExpressService extends BaseService<Express,Integer>{

	@Autowired
	ExpressDao expressDao;

    @Autowired
    ExpressService(ExpressDao expressDao){
        super(expressDao);
    }

}
