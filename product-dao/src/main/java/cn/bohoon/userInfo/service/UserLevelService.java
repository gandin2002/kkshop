package cn.bohoon.userInfo.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.userInfo.dao.UserLevelDao;
import cn.bohoon.userInfo.entity.UserLevel;

@Service
@Transactional
public class UserLevelService extends BaseService<UserLevel,Integer>{

	@Autowired
	UserLevelDao memberLevelDao;

    @Autowired
    UserLevelService(UserLevelDao userLevelDao){
        super(userLevelDao);
    }
    
   // @Cacheable(value="MemberLevelCache", key = "#root.targetClass + #root.methodName" )
    public 	List<UserLevel>  listUserLevelCache(){
    	List<UserLevel> List=this.list(" from UserLevel where state = ?  ORDER  BY exp DESC", true);
    	return List;
    }
    
    public  UserLevel getUserLevel(int exp){
    	List<UserLevel> list = listUserLevelCache();
    	UserLevel level = new UserLevel() ;
    	level.setLevel("1");
    	level.setName("商城会员");
    	level.setState(true);
    	for (UserLevel memberLevel : list) {
    		if(exp>=memberLevel.getExp()) 
    			return memberLevel;
		}
    	return level ;
    }
    
    
    public UserLevel getUserLevelByName(String name) {
    	List<UserLevel> list = listUserLevelCache();
    	for(UserLevel userLevel : list) {
    		if(name.equals(userLevel.getName())) {
    			return userLevel ;
    		}
    	}
    	return null ;
    }
    
  //  @CacheEvict(value="MemberLevelCache",allEntries= true)
    public void save(UserLevel memberLevel){
    	super.save(memberLevel);
    }

}
