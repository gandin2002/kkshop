package cn.bohoon.serve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.serve.dao.ServeDao;
import cn.bohoon.serve.entity.Serve;

@Service
@Transactional
public class ServeService extends BaseService<Serve,Integer>{

	@Autowired
	ServeDao serveDao;

    @Autowired
    ServeService(ServeDao serveDao){
        super(serveDao);
    }

}
