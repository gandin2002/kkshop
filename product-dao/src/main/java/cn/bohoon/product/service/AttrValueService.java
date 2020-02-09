package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.AttrValueDao;
import cn.bohoon.product.entity.AttrValue;

@Service
@Transactional
public class AttrValueService extends BaseService<AttrValue,Integer>{

	@Autowired
	AttrValueDao attrValueDao;

    @Autowired
    AttrValueService(AttrValueDao attrValueDao){
        super(attrValueDao);
    }
}