package cn.bohoon.interfaces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.interfaces.dao.ErpLinkDao;
import cn.bohoon.interfaces.entity.ErpLink;

@Service
@Transactional
public class ErpLinkService extends BaseService<ErpLink,String>{

	@Autowired
	ErpLinkDao erpLinkDao;

    @Autowired
    ErpLinkService(ErpLinkDao erpLinkDao){
        super(erpLinkDao);
    }
    
    
}
