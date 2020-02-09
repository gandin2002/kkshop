package cn.bohoon.basicSetup.domain;

public enum SysParamType {

	PLATFORM_PARAM {
		@Override
		public String getName() {
			return "平台参数";
		}

	},
	ORDERPRING_PARAM {
		@Override
		public String getName() {
			return "订单打印参数";
		}

	},
	ORDER_PARAM {
		@Override
		public String getName() {
			return "订单相关参数";
		}

	},
	PRODUCT_PARAM {
		@Override
		public String getName() {
			return "商品相关参数";
		}

	},
	SCORE_PARAM {
		@Override
		public String getName() {
			return "积分相关参数";
		}

	},
	SIGNIN_PARAM {
		@Override
		public String getName() {
			return "签到相关参数";
		}

	},
	BACKBANK_PARAM {
		@Override
		public String getName() {
			return "收款银行相关参数";
		}

	},
	OTHER_PARAM {
		@Override
		public String getName() {
			return "其他相关参数";
		}

	},
	CHI_SYNC_PARAM {
		@Override
		public String getName() {
			return "正航T8相关";
		}

	},
	PRODUCT_LABEL{
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "产品标签默认数据";
		}
	},
	WX_MP_CONFIG{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "微信公众号参数";
		}
	},WX_APP_CONFIG{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "微信小程序参数";
		}
		
	}
	;

	public abstract String getName();
}
