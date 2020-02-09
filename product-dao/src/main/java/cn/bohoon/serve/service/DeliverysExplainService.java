package cn.bohoon.serve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.serve.dao.DeliverysExplainDao;
import cn.bohoon.serve.entity.DeliverysExplain;

@Service
@Transactional
public class DeliverysExplainService extends BaseService<DeliverysExplain,Integer>{

	@Autowired
	DeliverysExplainDao deliverysExplainDao;

    @Autowired
    DeliverysExplainService(DeliverysExplainDao deliverysExplainDao){
        super(deliverysExplainDao);
    }

}
