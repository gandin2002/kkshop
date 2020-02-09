package cn.bohoon.page.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.page.dao.SmsLogDao;
import cn.bohoon.page.entity.SmsLog;

@Service
@Transactional
public class SmsLogService extends BaseService<SmsLog,Integer>{

	@Autowired
	SmsLogDao smsLogDao;

    @Autowired
    SmsLogService(SmsLogDao smsLogDao){
        super(smsLogDao);
    }

    public boolean canSendSms(String ipAdress) {
    	String hql = " from SmsLog where ipAdress=? and  visitDate>? and visitDate<?" ;
    	List<Object> params = new ArrayList<>();
    	params.add(ipAdress) ;
    	Date now = new Date() ;
    	Date before = DateUtil.getPreHoursBefore(now,1);
    	params.add(before) ;
    	params.add(now) ;
    	int n = list(hql ,params.toArray()).size() ;
    	//1个小时内最多可以发送5次验证码
    	if(n > 4) {
    		return true ;
    	}
    	return false ;
    }
}
