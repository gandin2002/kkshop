package cn.bohoon.userInfo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.userInfo.dao.ExpLogDao;
import cn.bohoon.userInfo.domain.ExpType;
import cn.bohoon.userInfo.entity.ExpLog;
import cn.bohoon.userInfo.entity.UserInfo;

@Service
@Transactional
public class ExpLogService extends BaseService<ExpLog,Integer>{

	@Autowired
	ExpLogDao expLogDao;

	@Autowired
	UserInfoService userInfoService;
	
    @Autowired
    ExpLogService(ExpLogDao expLogDao){
        super(expLogDao);
    }
    
    Logger logger = LoggerFactory.getLogger(getClass());
    
	/**
	 * 修改经验值
	 * @param id 用户ID
	 * @param exptype 修改类型
	 * @param exp 修改值
	 */
    public void updateExp(String id,ExpType exptype,Integer exp){
    	
    	UserInfo userInfo = userInfoService.get(id);

		ExpLog explog = new ExpLog();
		explog.setMemberId(userInfo.getId());
		explog.setExpType(exptype);
		explog.setExp(exp);

		if (exptype.equals(ExpType.MANUALLY_ADD)) {
			userInfo.setExp(userInfo.getExp() + exp);
			explog.setNote("手动增加  ：" + exp + " 经验值");
		}else if (exptype.equals(ExpType.MANUALLY_SUB)) {
			if (userInfo.getExp() < exp) {
				userInfo.setExp(0);
				explog.setExp(0);
				explog.setNote("超出可减经验值 设置为： 0  经验值");
			} else {
				userInfo.setExp(userInfo.getExp() - exp);
				explog.setNote("手动减少经验 ：" + exp + " 经验值");
			}
		}else if (exptype.equals(ExpType.MANUALLY_NEW)) {
			userInfo.setExp(exp);
			explog.setNote("设置 ：" + exp + " 为新经验值");
		}else if(exptype.equals(ExpType.ORDER)){
			userInfo.setExp(userInfo.getExp() + exp);
			explog.setNote("订单支付增加  ：" + exp + " 经验值");
		}
		save(explog);
		userInfoService.update(userInfo);
		logger.info("---------成功 {}经验值:{} 用户:{}----------",exptype.getName(),exp,id);
    }

}
