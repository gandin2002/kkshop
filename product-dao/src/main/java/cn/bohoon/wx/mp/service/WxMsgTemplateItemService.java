package cn.bohoon.wx.mp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.wx.mp.dao.WxMsgTemplateItemDao;
import cn.bohoon.wx.mp.entity.WxMsgTemplateItem;

@Service
@Transactional
public class WxMsgTemplateItemService extends BaseService<WxMsgTemplateItem,Integer> {
	
	@Autowired
	WxMsgTemplateItemDao wxMsgTemplateItemDao;


	@Autowired
	WxMsgTemplateItemService(WxMsgTemplateItemDao wxMsgTemplateItemDao) {
		 super(wxMsgTemplateItemDao);
	}
 
	public WxMsgTemplateItem findTemplateItem(String templateId, String name) {
		return wxMsgTemplateItemDao.select(" from WxMsgTemplateItem where templateId= ?  and name = ? ", templateId,
				name);
	}

	public List<WxMsgTemplateItem> findTemplateItem(String templateId) {
		List<WxMsgTemplateItem> list = new ArrayList<WxMsgTemplateItem>();
		list = wxMsgTemplateItemDao.findAll(" from WxMsgTemplateItem where templateId= ? ", templateId);
		return list;
	}
}
