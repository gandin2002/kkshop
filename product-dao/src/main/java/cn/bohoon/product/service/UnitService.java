package cn.bohoon.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.UnitDao;
import cn.bohoon.product.entity.Unit;

@Service
@Transactional
public class UnitService extends BaseService<Unit, Integer> {
	@Autowired
	UnitDao unitDao;
	
	@Autowired
	UnitService(UnitDao unitDao){
		super(unitDao);
	}
	
}
