package cn.bohoon.message.domain;

public enum MessageType {

	Email {
		public String getName() {
			return "邮件消息";
		}

	},
	Sms {
		public String getName() {
			return "短信消息";
		}

	},
	Site {
		public String getName() {
			return "站内消息";
		}

	},
	WeChat {
		public String getName() {
			return "微信消息";
		}

	};

	public abstract String getName();

}
