package cn.bohoon.userInfo.domain;

public enum ExpType {
	MANUALLY_ADD{
		public String  getName(){
			return "手动增加";
		}
	},
	MANUALLY_SUB{
		public String  getName(){
			return "手动减少";
		}
	},
	MANUALLY_NEW{
		public String  getName(){
			return "手动设置新经验值";
		}
	},
	ORDER{
		public String getName() {
			return "订单支付";
		}
	};
	public abstract String getName();
}
