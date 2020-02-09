package cn.bohoon.basicSetup.helper;

/**
 * 系统参数 CODE 定义
 * @author Administrator
 *
 */
public interface SysParamHelper {
	
	final String PLATFORM_NAME           ="PLATFORM_NAME" ; //（PC平台使用全称）
	final String PLATFORM_DESC           ="PLATFORM_DESC" ; //（微信平台使用简称）
	final String MOBILE_SITE             ="MOBILE_SITE" ; //请以斜线结束!如：http://bohoon.com/
	final String PC_SITE                 ="PC_SITE" ; //请以斜线结束!如：http://bohoon.com/
	final String PLATFORM_LOGO           ="PLATFORM_LOGO" ; //平台展示logo
	final String SERVICE_MOBILE          ="SERVICE_MOBILE" ; //客服联系电话
	final String SERVICE_QQ              ="SERVICE_QQ" ; //客服QQ号码
	final String SERVICE_EMAIL           ="SERVICE_EMAIL" ; //客服邮箱
	final String PLATFORM_QRCODE         ="PLATFORM_QRCODE" ; //平台二维码
	final String COMPANY_NAME            ="COMPANY_NAME" ; //公司名称
	final String COMPANY_ADRESS          ="COMPANY_ADRESS" ; //公司地址
	final String NON_PAYMENT_ORDER       ="NON_PAYMENT_ORDER" ; //未付款订单取消时间  已天为单位
	final String NON_DELIVERY_ORDER_STATE="NON_DELIVERY_ORDER_STATE" ; //true 付款后 发货前 false 付款后一段时间
	final String NON_DELIVERY_ORDER_TIME ="NON_DELIVERY_ORDER_TIME" ; //付款后允许取消时间
	final String ORDER_AUTO_TIME         ="ORDER_AUTO_TIME" ; //自动确认收货时间
	final String ORDER_DELAY_TIME        ="ORDER_DELAY_TIME" ; //订单可延长收货的时间，以天为单位
	final String AFTER_SALE_DELAY_TIME   ="AFTER_SALE_DELAY_TIME" ; //售后延长时间
	final String SALES_RETURN_TIME       ="SALES_RETURN_TIME" ; //订单申请了售后，确认收货时间在原确认收货时间上自动延长，只延长一次，以天为单位
	final String AFTER_SALE_APPLY_TIME   ="AFTER_SALE_APPLY_TIME" ; //申请售后一段时间后自动成功
	final String AUTO_REFUND_TIME        ="AUTO_REFUND_TIME" ; //接受售后并申请财务盛和一段时间后默认财务通过，并退款
	final String AUTO_SALES_RETURN_TIME  ="AUTO_SALES_RETURN_TIME" ; //接受退货，换货申请后，等待退货一点时间买家为发货，默认售后取消
	
	final String BANK_DATA_LIST = "BANK_DATA_LIST";// 银行数据列表
	final String USER_DEFAULT_PASSWORD = "USER_DEFAULT_PASSWORD"; // 默认密码
	
	final String SYNC_CHI_URL             ="SYNC_CHI_URL" ;       //正航同步数据URL
	final String SYNC_CHI_GROUP_ID        ="SYNC_CHI_GROUP_ID" ;  //正航同步数据套账号
	final String SYNC_CHI_USER_ID         ="SYNC_CHI_USER_ID" ;   //正航同步数据登录账号
	final String SYNC_CHI_PWD             ="SYNC_CHI_PWD" ;       //正航同步数据T8密码
	final String SYNC_CHI_LANGUAGE        ="SYNC_CHI_LANGUAGE" ;  //正航同步数据语言
	
	final String PRODUCT_LABEL_DEFAULT_DATA = "PRODUCT_LABEL_DEFAULT_DATA"; //产品标签默认数据
	
	final String WX_MP_URL = "WX_MP_URL";// 服务器接入地址
	final String WX_MP_TOKEN = "WX_MP_TOKEN";// 服务器接入验证标识
	final String WX_MP_APPID = "WX_MP_APPID";// 应用ID
	final String WX_MP_SECRET = "WX_MP_SECRET";// 应用密钥
	final String WX_MP_BUTTON_TYPE = "WX_MP_BUTTON_TYPE";//微信公众号按钮类型
	final String WX_MP_MENU = "WX_MP_MENU";//微信公众号菜单
	final String WX_MP_OAUTH = "WX_MP_OAUTH"; //微信公众号页面授权地址
	final String WX_MP_TOKEN_GET = "WX_MP_TOKEN_GET" ;//获取TOKEN信息接口地址
	final String WX_MP_MENY_GET ="WX_MP_MENY_GET";//自定义菜单查询接口地址
	final String WX_MP_MENU_CREATE = "WX_MP_MENU_CREATE"; //自定义菜单创建接口地址
	final String WX_MP_MENU_ACCREDIT_URL = "WX_MP_MENU_ACCREDIT_URL"; //微信授权地址
	final String WX_MP_MENU_USER_INFO = "WX_MP_MENU_USER_INFO";//微信用户信息
	final String WX_MP_TEMPLATE_ALL = "WX_MP_TEMPLATE_ALL";//微信公众号模板列表
	final String WX_MP_TEMPLATE_DELETE = "WX_MP_TEMPLATE_DELETE";//删除模板
	final String WX_MP_TEMPLATE_SEND = "WX_MP_TEMPLATE_SEND";//发送模板消息
	final String WX_MP_TEMPLATE_ID = "WX_MP_TEMPLATE_ID";
}
