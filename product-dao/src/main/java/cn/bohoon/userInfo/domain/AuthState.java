package cn.bohoon.userInfo.domain;

public enum AuthState {

	NO_AUTHED {
		public String getName() {
			return "未认证";
		}
	},
	AUTHING {
		public String getName() {
			return "认证中";
		}
	},
	REAUTHING {
		public String getName() {
			return "重新认证中";
		}
	},
	AUTH_PASS {
		public String getName() {
			return "认证通过";
		}
	},
	AUTH_REFUSE {
		public String getName() {
			return "认证被拒绝";
		}
		
	};
	public abstract String getName();

}
