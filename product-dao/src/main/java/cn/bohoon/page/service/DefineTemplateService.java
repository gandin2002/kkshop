package cn.bohoon.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.page.dao.DefineTemplateDao;
import cn.bohoon.page.entity.DefineTemplate;

@Service
@Transactional
public class DefineTemplateService extends BaseService<DefineTemplate,String>{

	@Autowired
	DefineTemplateDao defineTemplateDao;

    @Autowired
    DefineTemplateService(DefineTemplateDao defineTemplateDao){
        super(defineTemplateDao);
    }

}
