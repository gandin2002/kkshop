package cn.bohoon.userInfo.domain;

public enum UserSex {

	WOMAN {
		public String getName() {
			return "女";
		}
	},
	MAN {
		public String getName() {
			return "男";
		}
	},
	UNKNOWN {
		public String getName() {
			return "未知";
		}
	};
	public abstract String getName();
}
