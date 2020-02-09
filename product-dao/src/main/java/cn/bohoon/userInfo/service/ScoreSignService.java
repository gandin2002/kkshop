package cn.bohoon.userInfo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.userInfo.dao.ScoreSignDao;
import cn.bohoon.userInfo.domain.ScoreType;
import cn.bohoon.userInfo.domain.SignStatus;
import cn.bohoon.userInfo.entity.ScoreLog;
import cn.bohoon.userInfo.entity.ScoreSign;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.entity.UserInfo;

@Service
@Transactional
public class ScoreSignService extends BaseService<ScoreSign,Integer>{

	@Autowired
	ScoreSignDao scoreSignDao;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SysParamService sysparamService;
	
	@Autowired
	ScoreLogService scoreLogService;
	
	 @Autowired
	 ScoreSignService(ScoreSignDao scoreLogDao){
		 
	        super(scoreLogDao);
	    }
	 
	 
	 // 领取积分
	 public JSONObject  userScoreSign(User user,String signStatus) {
		 
			// 1. 获取用户是否创建签到表	
			List<ScoreSign> list = this.list("from ScoreSign t where t.u_id=?",user.getId());
			
			
			JSONObject jb = new JSONObject();
			ScoreSign scoreSign;
			//获取今天凌晨时间
			Calendar last = Calendar.getInstance();
//			last.add(Calendar.DATE, 6);
			Calendar today = last;
			
			// 进行日期统计
			int month = today.get(Calendar.MONTH)  + 1;
			int day = today.get(Calendar.DATE);
					
			if (list == null || list.size() <= 0 ){
				
				scoreSign = new ScoreSign();
				// 1.1则说明用户没有创建签到表，创建签到表
				scoreSign.setU_id(user.getId());
				
				HashMap hash = new HashMap();
				List li = new ArrayList();
				hash.put(month, li);
				
				scoreSign.setMonth_days(JsonUtil.toJson(hash));
				scoreSign.setLast_time(new Date());
				
				this.save(scoreSign);
			}else{
				
				// 1.2 否则有签到表,则返回签到的数据
				scoreSign = list.get(0);
				
				Date last_time = scoreSign.getLast_time();
				
				Calendar last2 = Calendar.getInstance();
				last2.setTime(last_time);
				
				// 1.3 进行签到
				if (!StringUtils.isEmpty(signStatus) && SignStatus.RECEIVE_INTEGRAL.toString().equals(signStatus)){
					
					String monthDays = scoreSign.getMonth_days();
					
					HashMap hashMap = JsonUtil.parse(monthDays,HashMap.class);
					JSONArray arr = (JSONArray) hashMap.get(month);
					
					
						// 判断当前月份是否结束
						if (arr!= null){
							
							
							if (!arr.contains(day+"")){
								
								// 添加天数
								arr.add(day+"");
								hashMap.put(month, arr);
								
								scoreSign.setMonth_days(JsonUtil.toJson(hashMap));
									
									if (day - (last2.get(Calendar.DATE)) == 1){
										
										// 说明是连续签到
										scoreSign.setSign_times(scoreSign.getSign_times().add(new BigDecimal(1)) );
										
									}else{
										
										// 不是连续签到
										scoreSign.setSign_times(new BigDecimal(0));
									}
									
									
									// 签到完了后，状态改为已签到
									scoreSign.setStates(SignStatus.RECEIVED_INTEGRAL);
									
									// 获取签到次数对应的积分
									Integer signDay_score = signDay_score(scoreSign);								
									
									
									jb.put("signDay_score", signDay_score);
									
									
									// 2.2 将积分商城中插入签到积分的记录
									scoreLog_sign(signDay_score,user);
									
								
							}
							
							
						}else{
							
							// 当前月已经过完了，到了下个月，重新生成一个日期表
							HashMap hash = new HashMap();
							List li = new ArrayList();
							hash.put(month, li);
							
							scoreSign.setMonth_days(JsonUtil.toJson(hash));
							
						}
						
						
						scoreSign.setLast_time(last.getTime());
						
						
				}else{
					
					
					// 到了第二天，则已领取则变为未领取积分
					if (day - (last2.get(Calendar.DATE)) >= 1){
						
						// 领取积分的操作
						scoreSign.setStates(SignStatus.RECEIVE_INTEGRAL);
						
					}
					
				}
				
				this.update(scoreSign);
			}
			
			
			
			UserInfo userInfo = userInfoService.select(" from UserInfo where id = ?  ", user.getUserInfoId());
			
			jb.put("score", userInfo.getScore());
			// 1.2.1-->领取积分的状态
			jb.put("signStatus",scoreSign.getStates().toString());
			JSONObject hash = (JSONObject) JsonUtil.parse(scoreSign.getMonth_days(),JSONObject.class);
			jb.put("month_days", hash.get(month+""));
		 
		 return jb;
	 }
	 
	 
	 
	 // 获取签到天数对应的积分
	 private Integer  signDay_score(ScoreSign scoreSign){
		 
		 int  times = scoreSign.getSign_times().intValue();
		 
		 // 1.判断签到功能是否开启
		 SysParam switch_sign = sysparamService.list("from SysParam s where s.code=?","SWITCH_SIGN").get(0);
		 
		int i =  new Integer(switch_sign.getValue());
		 
		// 开启签到功能
		 if (i == 0){
			 
			 // 赠送积分设置
			 SysParam csScore = sysparamService.list("from SysParam s where s.code=?","CUSTON_SIGN_SCORE").get(0);
			 
			 int i2 =  new Integer(csScore.getValue());
			 
			 if (i2 == 0){				 
				 // 说明是自定义									
				 SysParam pssl = sysparamService.list("from SysParam s where s.code=?","PRESENT_SIGN_SCORE_LIST").get(0);
				 
				 
				 JSONArray array = JsonUtil.parse(pssl.getSysOption(), JSONArray.class);
				 
				 
				 
				 if ((times+1) > array.size()){
					 
					 // 当签到次数大于自定义积分的规定的次数时，则循环到第一天
					scoreSign.setSign_times(new BigDecimal(0));
					times = 0;
					 
				 }
					 
					 JSONObject hash = (JSONObject) array.get(times);
					 return (Integer) hash.get("value");
				 
				 
				 
				
				 
				 
				 
			 }else{
				 
				 
				 // 累加数
				 SysParam sns = sysparamService.list("from SysParam s where s.code=?","SUMMARY_NUMBER_SCORE").get(0);
				 
				 
				 Integer sum = new Integer(sns.getValue());
				 
				 // 初始值
				 SysParam init = sysparamService.list("from SysParam s where s.code=?","INITIAL_VALUE").get(0);
				 String init_value = init.getValue();
				 
				 if (times == 28){
					 // 当签到次数大于自定义积分的规定的次数时，则循环到第一天
					scoreSign.setSign_times(new BigDecimal(0));
					 times = 0;
				 }
				 
				 
				 // 则为第一次赋值为初始值
				 if (times == 0){
					 
					 return new Integer(init_value);
				 }else{
					 
					 Integer flag = new Integer(init_value);
					 for (int s=0; s<times; s++){
						 
						 flag += sum;
					 }
					 
					 return flag;
				 }
				 
			 }
			 
		 }else{
			 
			 return null;
		 }
		 
		 
		 
	 }

	 // 向积分商城中插入一条签到的数据
	 private void scoreLog_sign(Integer score,User user){
		 
		 
		 if (score == null || score.intValue() <=0 )
			 return;
		 
		 ScoreLog scoreLog = new ScoreLog();
		 scoreLog.setFlag(true);
		 scoreLog.setMemberId(user.getUserInfoId());
		 scoreLog.setNote("签到,获取积分！");
		 scoreLog.setScore(score);
		 scoreLog.setScoreType(ScoreType.SIGN_IN);
		 
		 
		 scoreLogService.save(scoreLog);
		 
		 
		 // 然后在用户信息表中将总的积分+签到积分
		 UserInfo userInfo = userInfoService.select(" from UserInfo where id = ?  ", user.getUserInfoId());
		 userInfo.setScore(userInfo.getScore() + score);
		 userInfoService.update(userInfo);
		 
		 
	 }
}
