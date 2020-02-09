package cn.bohoon.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.page.dao.DefinePageDao;
import cn.bohoon.page.entity.DefinePage;

@Service
@Transactional
public class DefinePageService extends BaseService<DefinePage,Integer>{

	@Autowired
	DefinePageDao definePageDao;

    @Autowired
    DefinePageService(DefinePageDao definePageDao){
        super(definePageDao);
    }

}
