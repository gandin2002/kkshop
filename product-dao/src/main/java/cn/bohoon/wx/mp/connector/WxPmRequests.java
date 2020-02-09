package cn.bohoon.wx.mp.connector;

import org.apache.el.util.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.wxpay.util.WXUtil;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;

/**
 * 微信发送消息
 * @author HJ
 * 2018年3月23日,下午1:58:32
 */
@Component
public class WxPmRequests {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SysParamService sysParamService;

	String tokenPath = new String("");
	String menuCreatePath = new String("");
	String menyGetPath = new String("");
	String secret = new String("");
	String appid = new String("");
	String userInfoPath = new String("");
	String templateAllPath = new String("");
	String templateIdPath = new String("");
	String templateDeletePath = new String("");
	String templateSendPath = new String("");
	
	public void initData() {
		SysParam tokenPathSys = sysParamService.findParam(SysParamHelper.WX_MP_TOKEN_GET, SysParamType.WX_MP_CONFIG);//获取token 
		SysParam menuCreatePathSys = sysParamService.findParam(SysParamHelper.WX_MP_MENU_CREATE,SysParamType.WX_MP_CONFIG); //创建自定义菜单
		SysParam menyGetPathSys = sysParamService.findParam(SysParamHelper.WX_MP_MENY_GET, SysParamType.WX_MP_CONFIG); //获取自定义菜单
		SysParam UserInfoPathSys = sysParamService.findParam(SysParamHelper.WX_MP_MENU_USER_INFO, SysParamType.WX_MP_CONFIG); //用户信息
		SysParam templateAllPathSys = sysParamService.findParam(SysParamHelper.WX_MP_TEMPLATE_ALL, SysParamType.WX_MP_CONFIG); //模板全部列表
		SysParam templateIdPathSys = sysParamService.findParam(SysParamHelper.WX_MP_TEMPLATE_ID, SysParamType.WX_MP_CONFIG); //模板单个列表
		SysParam templateDeletePathSys = sysParamService.findParam(SysParamHelper.WX_MP_TEMPLATE_DELETE, SysParamType.WX_MP_CONFIG); //模板模板
		SysParam templateSendPathSys = sysParamService.findParam(SysParamHelper.WX_MP_TEMPLATE_SEND, SysParamType.WX_MP_CONFIG); //发送模板消息
		
		SysParam secretPathSys = sysParamService.findParam(SysParamHelper.WX_MP_SECRET, SysParamType.WX_MP_CONFIG);
		SysParam appidPathSys = sysParamService.findParam(SysParamHelper.WX_MP_APPID, SysParamType.WX_MP_CONFIG);

		
		templateSendPath = templateSendPathSys.getValue();
		templateDeletePath = templateDeletePathSys.getValue();
		templateIdPath = templateIdPathSys.getValue();
		tokenPath = tokenPathSys.getValue();
		menuCreatePath = menuCreatePathSys.getValue();
		menyGetPath = menyGetPathSys.getValue();
		userInfoPath = UserInfoPathSys.getValue();
		templateAllPath = templateAllPathSys.getValue();
		
		secret = secretPathSys.getValue();
		appid = appidPathSys.getValue();

	}

	/**
	 * 获取AccessToken
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 */
	public String getAccessToken() {

		initData();

		String tokenUrl = MessageFactory.get(tokenPath, appid, secret);
		String resultTokenJson = WXUtil.httpRequests(tokenUrl, "GET", null);

		logger.info("----------------获取微信公众号 Token  appid：{} secret:{} Token:{}----------------", appid, secret,
				resultTokenJson);
		return resultTokenJson;
	}

	/**
	 * 获取菜单
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 */
	public String getMenu() {

		initData();

		String accessTokenJson = getAccessToken();
		String accessTokenStr = JSON.parseObject(accessTokenJson).getString("access_token");

		String menyGetUrl = MessageFactory.get(menyGetPath, accessTokenStr);

		String resultMenuJson = WXUtil.httpRequests(menyGetUrl, "GET", null);

		logger.info("----------------获取微信公众号菜单    appid：{} secret:{} Token:{}----------------", appid, secret,accessTokenStr);

		return resultMenuJson;
	}

	/**
	 * 创建菜单
	 * 
	 * @param appid
	 * @param secret
	 * @param updateJson
	 * @return
	 */
	public String updateMenu(String updateJson) {

		initData();

		String accessTokenJson = getAccessToken();
		String accessTokenStr = JSON.parseObject(accessTokenJson).getString("access_token");

		String menuCreateUrl = MessageFactory.get(menuCreatePath, accessTokenStr);
		String resultJson = WXUtil.httpRequests(menuCreateUrl, "POST", updateJson);

		logger.info("----------------创建微信公众号菜单    appid：{} secret:{} Token:{}----------------", appid, secret,
				accessTokenStr);
		return resultJson;

	}

	/**
	 * 获取用户信息 
	 * @param openId
	 * @return
	 */
	public String wxUserInfo(String openId) {
		initData();
		String accessTokenJson = getAccessToken();
		String accessTokenStr = JSON.parseObject(accessTokenJson).getString("access_token");
		String userInfoUrl = MessageFactory.get(userInfoPath, accessTokenStr,openId);
		return  WXUtil.httpRequests(userInfoUrl,"GET", null);
	}
	
	
	/**
	 * 获得微信已添加的全部模板列表
	 * @return
	 */
	public String wxAllTemplate(){
		initData();
		String accessTokenJson = getAccessToken();
		String accessTokenStr = JSON.parseObject(accessTokenJson).getString("access_token");
		String templateAllUrl = MessageFactory.get(templateAllPath, accessTokenStr);
		return WXUtil.httpRequests(templateAllUrl,"GET", null);
	} 
	
	/**
	 * 获得模板库中的模板 
	 * @param template_id_short JSON 数据     { "template_id_short":"TM00015" }
	 * @return
	 */
	public String wxTemplateLibrary(String template_id_short){
		initData();
		String accessTokenJson = getAccessToken();
		String accessTokenStr = JSON.parseObject(accessTokenJson).getString("access_token");
		String templateIdUrl = MessageFactory.get(templateIdPath, accessTokenStr);
		return WXUtil.httpRequests(templateIdUrl,"POST", template_id_short);
	}
	
	
	/**
	 * 删除模板消息
	 * @param template_id_short JSON 数据     { "template_id_short":"TM00015" }
	 * @return
	 */
	public String wxTemplateDelete(String template_id){
		initData();
		String accessTokenJson = getAccessToken();
		String accessTokenStr = JSON.parseObject(accessTokenJson).getString("access_token");
		String templateDeleteUrl = MessageFactory.get(templateDeletePath, accessTokenStr);
		return WXUtil.httpRequests(templateDeleteUrl, "POST", template_id);
	}
	
	
	/**
	 * 发送模板消息
	 * @return
	 */
	public String wxTemplateSendMsg(String dataJson){
		initData();
		String accessTokenJson = getAccessToken();
		String accessTokenStr = JSON.parseObject(accessTokenJson).getString("access_token");
		String templateSendPathUrl = MessageFactory.get(templateSendPath, accessTokenStr);
		return WXUtil.httpRequests(templateSendPathUrl, "POST", dataJson);
	}
	

}
