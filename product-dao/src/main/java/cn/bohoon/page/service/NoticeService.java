package cn.bohoon.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.page.dao.NoticeDao;
import cn.bohoon.page.entity.Notice;

@Service
@Transactional
public class NoticeService extends BaseService<Notice,Integer>{

	@Autowired
	NoticeDao noticeDao;

    @Autowired
    NoticeService(NoticeDao noticeDao){
        super(noticeDao);
    }
      
}