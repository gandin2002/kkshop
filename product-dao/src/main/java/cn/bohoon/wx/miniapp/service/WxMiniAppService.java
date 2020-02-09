package cn.bohoon.wx.miniapp.service;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.bohoon.payment.domain.WechatPayVo;
import cn.bohoon.payment.entity.PaymentType;
import cn.bohoon.payment.service.PaymentTypeService;
import cn.bohoon.wx.miniapp.domain.WxMaJscode2SessionResult;

@Service
public class WxMiniAppService {


	@Autowired
	PaymentTypeService paymentTypeService ;
	
	Logger logger = LoggerFactory.getLogger(WxMiniAppService.class);

	String JSCODE_TO_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";

	@Autowired
	RestTemplate restTemplate;

	public WxMaJscode2SessionResult getSessionInfo(String jsCode) {
		PaymentType pay = paymentTypeService.selectByCode("wxminiapp") ;
		WechatPayVo wePayVo = pay.getWxConfigMap() ;
		
		logger.info("jsCode ======================"+jsCode);
		String requestUrl = JSCODE_TO_SESSION_URL ;
		requestUrl = MessageFormat.format(requestUrl, wePayVo.getAppId(), wePayVo.getAppsecret(), jsCode);
		logger.info("request url========="+requestUrl);
		String result = restTemplate.getForObject(requestUrl, String.class);
		logger.info("request result========="+result);
		WxMaJscode2SessionResult sessionResult = WxMaJscode2SessionResult.fromJson(result) ;
		return sessionResult;
	}
}
