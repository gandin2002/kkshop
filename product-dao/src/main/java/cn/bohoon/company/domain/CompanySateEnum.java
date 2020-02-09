package cn.bohoon.company.domain;

public enum CompanySateEnum {

	INIT {
		public String getName() {
			return "审核中";
		}
	},
	PASS {
		public String getName() {
			return "审核通过";
		}
	},
	UNPASS {
		public String getName() {
			return "审核未通过";
		}
	},
	LOGOUT {
		public String getName() {
			return "注销";
		}
	};
	public abstract String getName();
}
