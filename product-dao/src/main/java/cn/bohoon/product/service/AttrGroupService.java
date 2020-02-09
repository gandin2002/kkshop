package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.AttrGroupDao;
import cn.bohoon.product.entity.AttrGroup;

@Service
@Transactional
public class AttrGroupService extends BaseService<AttrGroup,Integer>{

	@Autowired
	AttrGroupDao productGroupDao;

    @Autowired
    AttrGroupService(AttrGroupDao productGroupDao){
        super(productGroupDao);
    }

}
