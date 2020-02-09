package cn.bohoon.message.domain;

public enum MessageContentType {
	
	NEWS{
		public String getName(){
			return "最新活动";
		}
	},LOGISTICS{
		public String getName(){
			return "物流助手";
		}
	},SHOPPINGINFO{
		public String getName(){
			return "购物消息";
		}
	},SYSINFO{
		public String getName(){
			return "系统消息";
		}
	},INFORM{
		public String getName(){
			return "通知消息";
		}
	};
	public abstract String getName();
	
}
