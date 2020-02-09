package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.GatheringConditionDao;
import cn.bohoon.order.entity.GatheringCondition;

/**
 * 付款条款
 * @author HJ
 * 2018年7月5日,下午2:50:35
 */
@Service
@Transactional
public class GatheringConditionService extends BaseService<GatheringCondition, Integer> {

	@Autowired
	GatheringConditionDao gatheringConditionDao;

	@Autowired
	GatheringConditionService(GatheringConditionDao gatheringConditionDao) {
		super(gatheringConditionDao);
	}
	
}
