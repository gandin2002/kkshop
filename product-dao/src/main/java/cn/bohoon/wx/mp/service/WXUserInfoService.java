package cn.bohoon.wx.mp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.wx.mp.dao.WXUserInfoDao;
import cn.bohoon.wx.mp.entity.WXUserInfo;

@Service
@Transactional
public class WXUserInfoService extends BaseService<WXUserInfo,Integer>{

	@Autowired
	WXUserInfoDao wXUserInfoDao;

    @Autowired
    WXUserInfoService(WXUserInfoDao wXUserInfoDao){
        super(wXUserInfoDao);
    }
    
}
