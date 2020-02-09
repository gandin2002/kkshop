package cn.bohoon.order.domain;

/**
 * 订单的状态
 */
public enum OrderState {

	INIT_STATE {
		@Override
		public String getName() {
			return "未确认";
		}

		@Override
		public String getState() {
			return "订单未确认";
		}

		@Override
		public String getDescribe() {
			return "订单未确认...";
		}

	},
	WAIT_BUYER_PAY {
		public String getName() {
			return "待付款";
		}

		public String getState() {
			return "等待客户付款";
		}

		public String getDescribe() {
			return "您的订单已审核通过，等待客户付款中...";
		}
	},
	WAIT_DELIVERY {
		public String getName() {
			return "待发货";
		}

		public String getState() {
			return "等待商家发货";
		}

		public String getDescribe() {
			return "您的订单已生效，等待商家发货中...";
		}
	},
	WAIT_REAPIRING {
		public String getName() {
			return "维修中";
		}

		public String getState() {
			return "维修中";
		}

		public String getDescribe() {
			return "您的货品正在维修中...";
		}
	},
	WAIT_REVICE {
		public String getName() {
			return "待收货";
		}

		public String getState() {
			return "商家已发货";
		}

		public String getDescribe() {
			return "商家已经发货，请耐心等待...";
		}
	},
	TRADE_FINISHED {
		public String getName() {
			return "完成";
		}

		public String getState() {
			return "订单完成";
		}

		public String getDescribe() {
			return "商家已经发货，卖家已收货...";
		}
	},

	CANCEL {
		public String getName() {
			return "已取消";
		}

		public String getState() {
			return "订单已取消";
		}

		public String getDescribe() {
			return "您的订单已手动取消";
		}
	},

	CONFIRM_DENY {
		public String getName() {
			return "审核不通过";
		}

		public String getState() {
			return "商家审核不通过";
		}

		public String getDescribe() {
			return "您的订单商家已审核，审核未通过";
		}
	},
	WAIT_CONFIRM_SALES {
		public String getName() {
			return "待审核";
		}

		public String getState() {
			return "等待商家审核";
		}

		public String getDescribe() {
			return "您的订单已提交，等待商家审核中...";
		}
	},
	WAIT_CONFIRM_MANAGER {
		public String getName() {
			return "待复审";
		}

		public String getState() {
			return "等待商家审核";
		}

		public String getDescribe() {
			return "您的订单已提交，等待商家审核中...";
		}
	},
	DELAY_DELIVERY {
		public String getName() {
			return "收货延长";
		}

		public String getState() {
			return "订单时间已经延长";
		}

		public String getDescribe() {
			return "您的订单时间已手动延长";
		}
	},

	/******************** 下面是售后订单状态 *************************/
	WAIT_AUDIT {
		@Override
		public String getName() {
			return "待审核";
		}

		@Override
		public String getState() {
			return "待审核";
		}

		@Override
		public String getDescribe() {
			return "售后已申请，等待审核";
		}
	},
	REFUSE_AUDIT {
		@Override
		public String getName() {
			return "拒绝";
		}

		@Override
		public String getState() {
			return "审核拒绝";
		}

		@Override
		public String getDescribe() {
			return "审核被决绝";
		}
	},
	TREASURER_AUDIT {
		@Override
		public String getName() {
			return "财务审核";
		}

		@Override
		public String getState() {
			return "财务审核";
		}

		@Override
		public String getDescribe() {
			return "初审通过，等待财务审核";
		}
	},
	BEING_REFUND {
		@Override
		public String getName() {
			return "退款中";
		}

		@Override
		public String getState() {
			return "退款中";
		}

		@Override
		public String getDescribe() {
			return "退款中......";
		}
	},
	REFUND_FINISH {
		@Override
		public String getName() {
			return "售后完成";
		}

		@Override
		public String getState() {
			return "售后完成";
		}

		@Override
		public String getDescribe() {
			return "售后完成......";
		}
	},
	REFUND_AND_FINISH {
		@Override
		public String getName() {
			return "退款完成";
		}

		@Override
		public String getState() {
			return "退款完成";
		}

		@Override
		public String getDescribe() {
			return "退款完成......";
		}
	},
	WAIT_RETURN_GOODS {
		@Override
		public String getName() {
			return "等待退货";
		}

		@Override
		public String getState() {
			return "等待退货";
		}

		@Override
		public String getDescribe() {
			return "等待退货......";
		}
	},
	RETURNING_GOODS {
		@Override
		public String getName() {
			return "退货中";
		}

		@Override
		public String getState() {
			return "退货中";
		}

		@Override
		public String getDescribe() {
			return "退货中......";
		}
	},
	RETURNED_GOODS {
		@Override
		public String getName() {
			return "退货完成";
		}

		@Override
		public String getState() {
			return "退货完成";
		}

		@Override
		public String getDescribe() {
			return "退货完成......";
		}
	},
	EXCHANGE_GOODS {
		@Override
		public String getName() {
			return "换货发货";
		}

		@Override
		public String getState() {
			return "换货发货";
		}

		@Override
		public String getDescribe() {
			return "换货发货......";
		}
	},
	EXCHANGE_SENDED_GOODS {
		@Override
		public String getName() {
			return "换货已发货";
		}

		@Override
		public String getState() {
			return "换货已发货";
		}

		@Override
		public String getDescribe() {
			return "换货已发货......";
		}
	},
	REPAIRE_SENDED_GOODS {
		@Override
		public String getName() {
			return "维修已发货";
		}

		@Override
		public String getState() {
			return "维修已发货";
		}

		@Override
		public String getDescribe() {
			return "维修已发货......";
		}
	};

	public abstract String getName();

	public abstract String getState();

	public abstract String getDescribe();
}
