package cn.bohoon.userInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.userInfo.dao.TempUserDao;
import cn.bohoon.userInfo.entity.TempUser;

@Service
@Transactional
public class TempUserService extends BaseService<TempUser, Integer>{
	
	
	

	@Autowired
	TempUserDao tempUserDao;

	@Autowired
	TempUserService(TempUserDao tempUserDao) {
		super(tempUserDao);
	}
	
	
	
	// 通过手机号码获取临时用户
	public TempUser getMobile(String mobile){
		
		TempUser tempUser = this.select("from TempUser where mobile=?", mobile);
		
		return tempUser;
	}

}
