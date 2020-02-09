package cn.bohoon.order.domain;

public enum OperateIdentity {
	CONSUMER {

		@Override
		public String getName() {
			return "消费者";
		}
	},
	PRODUCER {

		@Override
		public String getName() {
			return "生成者";
		}

	},
	MANAGER {
		@Override
		public String getName() {
			return "管理人员";
		}

	};

	public abstract String getName();
}
