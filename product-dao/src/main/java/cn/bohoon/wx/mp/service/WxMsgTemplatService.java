package cn.bohoon.wx.mp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.bohoon.framework.service.BaseService;
import cn.bohoon.util.RegexUtil;
import cn.bohoon.util.VelocityHtmlUtils;
import cn.bohoon.wx.mp.connector.WxPmRequests;
import cn.bohoon.wx.mp.dao.WxMsgTemplateDao;
import cn.bohoon.wx.mp.domain.WxSendTemplateMsg;
import cn.bohoon.wx.mp.entity.WxMsgTemplate;
import cn.bohoon.wx.mp.entity.WxMsgTemplateItem;

@Service
@Transactional
public class WxMsgTemplatService extends BaseService<WxMsgTemplate,String> {

	@Autowired
	WxMsgTemplateDao wxMsgTemplateDao;

	@Autowired
	WxPmRequests wxPmRequests;
	
	@Autowired
	WxMsgTemplateItemService wxMsgTemplateItemService;
	
	@Autowired
	WxMsgTemplatService(WxMsgTemplateDao wxMsgTemplateDao) {
		super(wxMsgTemplateDao);
	}
	
	/**
	 * 更新同步数据
	 */
	public Boolean updateSynDate(){
		try {
			String jsonStr = wxPmRequests.wxAllTemplate();
			JSONObject jSONObject = JSONObject.parseObject(jsonStr);
			JSONArray JSONArray= jSONObject.getJSONArray("template_list");
			List<WxMsgTemplate> wxMsgTemplateList = JSONArray.toJavaList(WxMsgTemplate.class);
			for (WxMsgTemplate wxMsgTemplate : wxMsgTemplateList) {
				WxMsgTemplate source= wxMsgTemplateDao.get(wxMsgTemplate.getTemplate_id());
				if(source != null){
					continue;
				}
				List<String> list = RegexUtil.paraseData(wxMsgTemplate.getContent());
				wxMsgTemplate.setArraycnt(JSON.toJSONString(list));
				this.save(wxMsgTemplate);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	

	public Map<String,String> velocityReplace(String templateId, VelocityContext velocity){
		Map<String,String>  map= new HashMap<String,String>();
		List<WxMsgTemplateItem> list = wxMsgTemplateItemService.findTemplateItem(templateId);
		for (WxMsgTemplateItem wxMsgTemplateItem : list) {
			String value = VelocityHtmlUtils.vm(wxMsgTemplateItem.getValue(), velocity);
			map.put(wxMsgTemplateItem.getName(), value);
		}
		return map ;// 解析html
	}
	
	/**
	 * 
	 * 
	 * @param templateId 模板ID 
	 * @param touser //发给谁? 用户openId
	 * @param data // Map<"first",value>
	 * @return
	 */
	public Boolean sendMsg(String templateId,String touser,Map<String,String> data){
		boolean state = true;
		try {
			List<WxMsgTemplateItem> list = wxMsgTemplateItemService.findTemplateItem(templateId);
			JSONObject jsonObject = new JSONObject();
			for (WxMsgTemplateItem wxMsgTemplateItem : list) {
				JSONObject jo =new JSONObject();
				jo.put("color", wxMsgTemplateItem.getColor());
				String str= data.get(wxMsgTemplateItem.getName());
				jo.put("value", str);
				jsonObject.put(wxMsgTemplateItem.getName(),jo);
			}
			WxSendTemplateMsg wstmg = new WxSendTemplateMsg();
			wstmg.setTouser(touser);
			wstmg.setTemplate_id(templateId);
			wstmg.setData(jsonObject);
			state = sendMsg(wstmg);
		} catch (Exception e) {
			state = false;
		}
		return state;
	}
	public Boolean sendMsg(WxSendTemplateMsg wstmg ){
		try {
			wxPmRequests.wxTemplateSendMsg(JSON.toJSONString(wstmg));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
