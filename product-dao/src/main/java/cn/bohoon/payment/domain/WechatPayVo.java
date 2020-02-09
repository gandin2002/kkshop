package cn.bohoon.payment.domain;

public class WechatPayVo {

	String appId;// 微信应用id
	String mchId;// 微信商户号
	String apiKey; // 支付api密钥 app应用 key 手动在微信开发平台配置
	String appsecret;// appSecret
	String notifyUrl; // 异步回调URL
	String uniPayUrl ; //预支付下单URL
	String refundUrl ; //退款URL

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getUniPayUrl() {
		return uniPayUrl;
	}

	public void setUniPayUrl(String uniPayUrl) {
		this.uniPayUrl = uniPayUrl;
	}

	public String getRefundUrl() {
		return refundUrl;
	}

	public void setRefundUrl(String refundUrl) {
		this.refundUrl = refundUrl;
	}

	
}
