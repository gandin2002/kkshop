package cn.bohoon.order.domain;

/**
 * 售后类型
 * 
 * @author admin
 *
 */
public enum ReOrderType {

	REFUNDANDGOODS {
		public String getName() {
			return "退货退款";
		}
	},
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
	IN_REPAIR {
		public String getName() {
			return "维修";
		}
	},
	CANCEL_ORDER {
		public String getName() {
			return "取消订单";
		}
	},
	RETURNSREFUNDS {
		public String getName() {
			return "退货退款";
			}
	},
	OTHER {
		public String getName() {
			return "其他";
		}
	};
	public abstract String getName();

}
