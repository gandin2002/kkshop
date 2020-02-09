package cn.bohoon.serve.domain;
/*
 * 账单状态
 */
public enum SaleType {
	CHANGEGOODS {
		public String getName() {
			return "换货";
		}
	},
	REFUND {
		public String getName() {
			return "仅退款";
		}
	},
	RETURNSREFUNDS {
			public String getName() {
				return "退货退款";
			}
	},IN_REPAIR {
		public String getName() {
			return "维修";
		}
	},OTHER{
		public String getName(){
			return "其他";
		}
	};
	public abstract String getName();
}
