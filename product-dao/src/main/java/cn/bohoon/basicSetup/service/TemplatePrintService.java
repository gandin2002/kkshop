package cn.bohoon.basicSetup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.basicSetup.dao.TemplatePrintDao;
import cn.bohoon.basicSetup.entity.TemplatePrint;
import cn.bohoon.framework.service.BaseService;

@Service
@Transactional
public class TemplatePrintService extends BaseService<TemplatePrint,Integer>{

	@Autowired
	TemplatePrintDao templatePrintDao;

    @Autowired
    TemplatePrintService(TemplatePrintDao templatePrintDao){
        super(templatePrintDao);
    }

}
