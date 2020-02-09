package cn.bohoon.userInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.userInfo.dao.UserInRateDao;
import cn.bohoon.userInfo.entity.UserInRate;

@Service
@Transactional
public class UserInRateService extends BaseService<UserInRate, Integer> {

	@Autowired
	UserInRateDao userInRateDao;

	@Autowired
	UserInRateService(UserInRateDao userInRateDao) {
		super(userInRateDao);
	}

}
