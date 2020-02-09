package cn.bohoon.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.page.dao.PlaceADDao;
import cn.bohoon.page.entity.PlaceAD;

@Service
@Transactional
public class PlaceADService extends BaseService<PlaceAD,Integer>{

	@Autowired
	PlaceADDao placeADDao;

    @Autowired
    PlaceADService(PlaceADDao placeADDao){
        super(placeADDao);
    }
    
    
}