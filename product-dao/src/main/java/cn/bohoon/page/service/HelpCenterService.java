package cn.bohoon.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.page.dao.HelpCenterDao;
import cn.bohoon.page.entity.HelpCenter;

@Service
@Transactional
public class HelpCenterService extends BaseService<HelpCenter,Integer>{

	@Autowired
	HelpCenterDao helpCenterDao;

    @Autowired
    HelpCenterService(HelpCenterDao helpCenterDao){
        super(helpCenterDao);
    }

}
