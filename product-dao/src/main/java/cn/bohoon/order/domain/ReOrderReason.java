package cn.bohoon.order.domain;

public enum ReOrderReason {

	SEND_ERROR {
		public String getName() {
			return "货品错发漏发";
		}
	},
	NEED_REPAIR {
		public String getName() {
			return "货品需要维修";
		}
	},
	DONOTWANT {
		public String getName() {
			return "我不想要了";
		}
	},
	QUALITY_BAD {
		public String getName() {
			return "质量有问题";
		}
	},
	OTHER_RESON {
		public String getName() {
			return "其他";
		}
	};
	public abstract String getName();

}
