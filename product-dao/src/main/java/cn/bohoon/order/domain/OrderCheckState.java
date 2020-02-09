package cn.bohoon.order.domain;

public enum OrderCheckState {
	
	CONFIRM_SUBMIT{
		public String getName() {
			return "提交";
		}
	},
	
	CONFIRM_RESUBMIT{
		public String getName() {
			return "重新提交";
		}
	},
	CONFIRM_UPDATE{
		
		@Override
		public String getName() {
			return "订单货品数量被修改";
		}
	},
	
	CONFIRM_CANCEL{
		public String getName() {
			return "取消";
		}
	},

	CONFIRM_PASS {
		public String getName() {
			return "审核通过";
		}
	},
	
	CONFIRM_NOT_PASS {
		public String getName() {
			return "审核不通过";
		}
	},
	
	CONFIRM_BUYER_PAY {
        public String getName() {
        	return "付款";
    	}
    },
	
	CONFIRM_DELIVERY {
        public String getName() {
        	return "发货";
    	}
    },
	
	CONFIRM_RECEIPT {
        public String getName() {
        	return "收货";
    	}
    },
	
	CONFIRM_FINISH {
        public String getName() {
        	return "完成";
    	}
    },
	CONFIRM_DEL{
		@Override
		public String getName() {
			return "订单商品被删除";
		}
	} ;  
	public abstract String getName();
}
