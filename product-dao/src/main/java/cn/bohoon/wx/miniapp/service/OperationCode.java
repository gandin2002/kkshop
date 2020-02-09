package cn.bohoon.wx.miniapp.service;

public interface OperationCode {
	
	String OPERATION = "operation" ;
	
	/**
	 * 参数错误错误码
	 */
	Integer PARAM_ERR_STATE = -1 ;
	
	/**
	 * 参数错误提示消息
	 */
	String PARAM_ERR_MSG = "参数错误！" ;
	
	/**
	 * 数据错误协议码
	 */
	Integer PROTOCL_DBERR_STATE = -100 ;
	
	/**
	 * 参数错误提示消息
	 */
	String PROTOCL_DBERR_MSG = "数据交易错误！" ;
	
	
	/**
	 * 货品列表
	 */
	String PRODUCT_INDEX = "page" ;
	
	/**
	 * 货品详情
	 */
	String PRODUCT_DETAIL = "detail" ;

	/**
	 * 货品搜索
	 */
	String PRODUCT_SEARCH = "search" ;
	
	/**
	 * 常用清单
	 */
	String PRODUCT_COMMONUSE = "commonUse" ;
	/**
	 * 用户登录
	 */
	String USER_USERLOGIN = "login" ;
	
	/**
	 * 用户请求登录平台
	 */
	String USER_REQUEST_LOGIN = "req_login" ; 
	
	/**
	 * 用户个人中心
	 */
	String USER_USERCENTER = "userCenter" ;
	
	/**
	 * 用户手机号码绑定
	 */
	String USER_BIND_PHONE = "bindPhone" ;
	
	/**
	 * 用户邮箱绑定
	 */
	String USER_BIND_EMAIL = "bindEmail" ;
	
	/**
	 * （我的）订单列表
	 */
	String ORDER_UNIFIEDORDER = "unifiedorder" ;
	/**
	 * （我的）订单列表
	 */
	String ORDER_PAGE = "page" ;
	
	/**
	 * 订单详情
	 */
	String ORDER_DETAIL = "detail" ;
	
	/**
	 * 订单支付
	 */
	String ORDER_PAY = "orderPay" ;
	
	/**
	 * 订单状态变化
	 */
	String ORDER_STATE_CHANGE = "stateChange" ;
	
	/**
	 * 售后列表
	 */
	String REFUND_PAGE = "page" ;
	
	/**
	 * 提交售后订单
	 */
	String REFUND_SUBMIT = "submit" ;
	
	/**
	 * 挑货目录导航首页
	 */
	String PAGE_NAVIGATION_INDEX  = "index" ;
	
	/**
	 * 购物车
	 */
	String SHOPPING_CART = "shoppingCart" ;
	
	/**
	 * 购物车
	 */
	String SHOPPING_ADD_CART = "addCart" ;

	/**
	 * .增量新增采购栏
	 */
	String SHOPPING_INCREMENT_CART = "increment" ;
	
	/**
	 * 购物车
	 */
	String SHOPPING_DEL_CART = "delCart" ;
	
	/**
	 * 采购单结算
	 */
	String SHOPPING_SETTLEMENT = "settleMent" ;
	
	/**
	 * 绑定手机
	 */
	String SMS_BIND_PHONE = "bindPhone" ;
	
	/**
	 * 绑定邮箱
	 */
	String SMS_BIND_EMAIL = "bindEmail" ;
	
	/**
	 * 收货地址页面
	 */
	String SHIPPING_INDEX_INFO = "shippingInfo" ; 
	
	/**
	 * 收货地址页面
	 */
	String SHIPPING_ADD_INFO = "addShippInfo" ; 
	
	/**
	 * 收货地址页面
	 */
	String SHIPPING_DEL_INFO = "delShippInfo" ; 
	
	/**
	 * 用户收藏
	 */
	String USER_FAVORITE ="userFavorite";
	
	/**
	 * 用户收藏删除
	 */
	String USER_FAVORITE_DELETE ="userFavoriteDelete";
	
	/**
	 * 公司银行信息
	 */
	String COMPANY_BANK_LIST="companyBankList";
	
	/**
	 * 公司银行新增
	 */
	String COMPANY_BANK_ADD="companyBankAdd";
	
	/**
	 * 公司银行新增删除
	 */
	String COMPANY_BANK_DELETE="companyBankDelete";
	
	/**
	 * 公司银行新增编辑
	 */
	String COMPANY_BANK_EDIT="companyBankEdit";
	
	/**
	 * 报价管理
	 */
	String QUOTATION_INDEX = "myquotation" ;
	
	/**
	 * 报价详情
	 */
	String QUOTATION_DETAIL = "detail" ;
	
	/**
	 * 报价单货品
	 */
	String QUOTATION_DETAIL_ITEM = "items" ;
	
	/**
	 * 个人组织机构查看
	 */
	String USER_INFO_ORG_INDEX = "index" ;
	
	/**
	 * 编辑部门
	 */
	String USER_INFO_ORG_EIDT = "editOrg" ;
	/**
	 * 编辑部门
	 */
	String USER_ORG_TOADD = "toAddOrg" ;
	
	/**
	 * 提交部门
	 */
	String USER_INFO_ORG_SAVE ="saveOrg" ;
	
	/**
	 * 个人组织机构查看
	 */
	String USER_INFO_ORG = "userOrg" ;
	
	/**
	 * 去新增组织用户
	 * 
	 */
	String USER_ORG_TO_ADD = "userOrgAdd" ;
	
	/**
	 * 去编辑组织用户
	 * 
	 */
	String USER_ORG_TO_EDIT = "userOrgEdit" ;
	
	/**
	 * 保存组织机构员工
	 * 
	 */
	String USER_ORG_SAVE = "userOrgSave" ;
	
	/**
	 * 我的发票
	 * 
	 */
	String INVOICE_INDEX = "index" ;
	
	/**
	 * 我的发票
	 * 
	 */
	String INVOICE_WAIT_FOR = "invoiceWait" ;
	
	/**
	 * 去申请发票
	 * 
	 */
	String INVOICE_TO_APPLY = "toApply" ;
	
	/**
	 * 申请
	 * 
	 */
	String INVOICE_APPLY = "invoiceApply" ;
	
	/**
	 * 手机号码已存在账户，确认后原来账户将解绑！
	 */
	Integer PARAM_ERR_BIND_STATE = 1 ;

	/**
	 * 企业认证
	 */
	String COMPANY_ATTEST_ADD = "companyAttestAdd"; 

	/**
	 * 企业认证信息
	 */
	String COMPANY_ATTEST_INFO = "companyAttestInfo"; 
	
	/**
	 * 用户认证信息
	 */
	String USER_ATTEST_INFO = "userAttestInfo";

	/**
	 * 用户认证
	 */
	String USER_ATTEST_ADD = "userAttestAdd";

	/**
	 * 修改用户信息
	 */
	String USER_INFO_EDIT = "userInfoEdit";

	/**
	 * 用户积分记录
	 */
	String USER_SCORE_LOG = "userScoreLog";

	/**
	 * 信用记录
	 */
	String USER_CREADIT_AMOUNT_LOG = "userCreaditAmountLog";

	/**
	 * 发票列表
	 */
	String USER_INVOICE_LIST = "userInvoiceList";

	/**
	 * 新增发票
	 */
	String USER_INVOICE_ADD = "userInvoiceAdd";

	/**
	 * 设置默认
	 */
	String USER_INVOICE_DEFAULT = "userInvoiceDefault";

	/**
	 * 删除发票
	 */
	Object USER_INVOICE_DELETE = "userInvoiceDelete";

	/**
	 * 售后详情
	 */
	String REFUND_INFO = "refundInfo";
	
	/**
	 * 已售后列表
	 * 
	 */
	String REFUND_ED = "refunded" ;

	/**
	 * 组织结构成员列表
	 */
	String USER_ORG_MEMBER_LIST = "userOrgMemberlist";

	/**
	 * 新增部门员工
	 */
	String USER_ORG_MEMBER_ADD = "userOrgMemberAdd";

	/**
	 * 新增新会员
	 */
	String USER_ORG_MEMBER_ADD_NEW = "userOrgMemberAddNew";

	/**
	 * 发票详情
	 */
	String INVOICE_DETAIL = "invoiceDetail";

	/**
	 * 用户点击收藏
	 */
	String USER_FAVORITE_CLICK = "userFavoriteClick";
	
	/**
	 * 用户批量加入收藏
	 */
	String USERFAVORITEBATCH = "userFavoriteBatch";

	/**
	 * 再次加入购物车
	 */
	String SHOPPING_AGAIN_ADD_CART = "shoppingAgainAddCart";

	/**
	 * 修改密码
	 */
	String USER_CHANGE_PASSWORD = "userChangePassword";

	/**
	 * 获取简单用户信息 
	 */
	String GET_USER_INFO = "getUserInfo";

	/**
	 * 修改交易密码
	 */
	String USER_CHANGE_TRANS_PASSWORD="userChangeTransPassword";
	
	/***
	 * 用户忘记密码
	 */
	String USER_FORGET_PASSWORD = "userForgetPassword";
	
	/**
	 * 签到积分
	 */
	String USER_SIGN_IN = "userSignIn";
	
	/**
	 * 新增一个报价单
	 */
	String ADD_QUOTATION = "add_quotation";
	
	
	/**
	 * 协议专区
	 */
	String PROTOCOL_AREA = "protocol_area";
	
	/**
	 * 信用支付
	 */
	String CREDIT_PAY = "credit_pay";
	
	/**
	 * 最新活动
	 */
	String NEW_ACTIVITY ="new_activity";
	
	/**
	 * 物流助手
	 */
	String LOGISTICS_INFORMATION="logistics_information";
	
	/**
	 * 交易消息
	 */
	String TRANSACTION_MESSAGE ="transaction_message";
	
	/**
	 * 系统消息
	 */
	String SYSTEM_MESSAGE = "system_message";
	
	/**
	 * 通知消息
	 */
	String NOTIFICATION_MESSAGE ="notification_message";
	
	
	
	
	
	
}
