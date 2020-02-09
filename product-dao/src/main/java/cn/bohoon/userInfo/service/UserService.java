package cn.bohoon.userInfo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.util.CipherUtil;
import cn.bohoon.userInfo.dao.UserDao;
import cn.bohoon.userInfo.domain.ScoreType;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.entity.UserInfo;

@Service
@Transactional
public class UserService extends BaseService<User, Integer> {

	@Autowired
	UserDao userDao;
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SysParamService sysParamService;
	
	@Autowired
	ScoreLogService scoreLogService;
	
	public String encodePwd(){
		SysParam sysParam = sysParamService.findParam(SysParamHelper.USER_DEFAULT_PASSWORD, SysParamType.PLATFORM_PARAM);
		if(!StringUtils.isEmpty(sysParam.getValue())){
			return CipherUtil.md5(sysParam.getValue());
		}
		return CipherUtil.md5("123456");
	}
	
	

	@Autowired
	UserService(UserDao userDao) {
		super(userDao);
	}
//	/**
//	 * 
//	 * @param
//	 */
//	
//	public void registerByComany(User user){
//		UserInfo userInfo = new UserInfo();
//		userInfo.setPhone(user.getMobile());
//		userInfo.setNickname(user.getMobile());
//		userInfoService.save(userInfo);
//		user.setUserInfoId(userInfo.getId());
//		userDao.save(user);
//	}
	
	/**
	 * 
	 * @param user
	 */
	public void registerMobile(User user,String commendFriendId) {
		UserInfo userInfo = new UserInfo();
		userInfo.setPhone(user.getMobile());
		userInfo.setNickname(user.getMobile());
		if(!StringUtils.isEmpty(commendFriendId)){
			int cfId=Integer.parseInt(commendFriendId);
			userInfo.setCommendFriendId(cfId);
		}
		userInfoService.save(userInfo);
		user.setUserInfoId(userInfo.getId());
		UserInfo UserInfo=userInfoService.select("from UserInfo where phone="+user.getMobile());
		//手机号注册奖励积分
		SysParam SysParam=sysParamService.findParam("PHONE_VERIFY_SCORE", SysParamType.SCORE_PARAM);
		scoreLogService.updateScore(UserInfo.getId(),Integer.parseInt(SysParam.getValue()),ScoreType.PHONE_SUBMIT); 
		userDao.save(user);
	}

	public User getUserById(String userId) {
		String hql = " from User u where u.userInfoId=?  ";
		User user = select(hql, userId);
		return user;
	}

	public User findUserByLoginName(String identifier) {
		List<Object> params = new ArrayList<Object>();
		params.add(identifier);
		params.add(identifier);
		params.add(identifier);
		params.add(identifier);
		params.add(identifier);
		params.add(identifier);
		String hql = " from User u where u.username=? or u.mobile=? or u.email=? or u.wxminiOpenid=? or u.companyId=? or userInfoId=?";
		List<User> list = list(hql, params.toArray());
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 重置密码
	 * @param userId
	 */
	public void reinstall(String userId) {
		this.execute(" update User set Password = ?  where  userInfoId = ? ",encodePwd(), userId);
	}


}
