package cn.bohoon.userInfo.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.userInfo.dao.ScoreLogDao;
import cn.bohoon.userInfo.domain.ScoreType;
import cn.bohoon.userInfo.entity.ScoreLog;
import cn.bohoon.userInfo.entity.UserInfo;

@Service
@Transactional
public class ScoreLogService extends BaseService<ScoreLog,Integer>{

	@Autowired
	ScoreLogDao scoreLogDao;
	
	@Autowired
	UserInfoService userInfoService;

	Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    ScoreLogService(ScoreLogDao scoreLogDao){
        super(scoreLogDao);
    }
    
  
    
    public void updateScore(String id,Integer score,ScoreType type){
		UserInfo userInfo = userInfoService.get(id);

		ScoreLog scoreLog = new ScoreLog();
		scoreLog.setMemberId(userInfo.getId());
		scoreLog.setScoreType(type);
		scoreLog.setScore(score);

		if (type.equals(ScoreType.MANUALLY_ADD)) {
			userInfo.setScore(userInfo.getScore() + score);
			scoreLog.setNote("手动增加  ：" + score + " 积分");
		}else if (type.equals(ScoreType.MANUALLY_SUB)) {
			if (userInfo.getScore() < score) {
				userInfo.setScore(0);
				scoreLog.setScore(0);
				scoreLog.setNote("超出可减积分 设置为： 0  积分");
			} else {
				userInfo.setScore(userInfo.getScore() - score);
				scoreLog.setNote("手动减少 ：" + score + " 积分");
			}
		}else if (type.equals(ScoreType.MANUALLY_NEW)) {
			userInfo.setScore(score);
			scoreLog.setNote("设置为 ：" + score + " 新积分");
		}
		
		if(type.equals(ScoreType.COMMEND_PLACE_AN_ORDER)){
			userInfo.setScore(userInfo.getScore()+score);
			scoreLog.setNote("推荐用户下单积分");
			scoreLog.setFlag(true);
			
		}
		
		if(type.equals(ScoreType.COMMEND_REFUND_OR_SALES)){
			userInfo.setScore(userInfo.getScore() - score);
			scoreLog.setNote("推荐用户退款或退货扣除 -"+ score+" 积分");
		}
		
		if(type.equals(ScoreType.REFUND_OR_SALES)){
			userInfo.setScore(userInfo.getScore() - score);
			scoreLog.setNote("退款或退货扣除 -"+ score+" 积分");
		}
		
		if(type.equals(ScoreType.EMALI_SUBMIT)){
			userInfo.setScore(userInfo.getScore()+score);
			scoreLog.setNote("邮箱认证积分");
			scoreLog.setFlag(true);
		}
		
		if(type.equals(ScoreType.PHONE_SUBMIT)){
			userInfo.setScore(userInfo.getScore()+score);
			scoreLog.setNote("手机号认证积分");
			scoreLog.setFlag(true);
		}
		
		if(type.equals(ScoreType.FOLLOW_ON_WECHAT)){
			userInfo.setScore(userInfo.getScore()+score);
			scoreLog.setNote("首次关注公众号积分");
		}
		if(type.equals(ScoreType.BUY_GOODS)){
			userInfo.setScore(userInfo.getScore()+score);
			scoreLog.setNote("购物返积分");
			scoreLog.setFlag(true);
			
		}
		save(scoreLog);
		userInfoService.update(userInfo);
		logger.info("---------成功 {}积分:{} 用户:{}----------",type.getName(),score,id);
    }

}
