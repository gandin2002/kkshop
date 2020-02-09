package cn.bohoon.wx.mp.domain;

public enum     WxMsgType {
 
	text{
		@Override
		public String getName() {
			return "文本类型";
		}
	},image{

		@Override
		public String getName() {
			return "图片类型";
		}
	},voice{

		@Override
		public String getName() {
			return "语言消息";
		}
	},video{

		@Override
		public String getName() {
			return "视频消息";
		}
	},shortvideo{

		@Override
		public String getName() {
			return "小视频信息";
		}
	},location{

		@Override
		public String getName() {
			return "地理位置";
		}
	},link{

		@Override
		public String getName() {
			return "连接消息";
		}
		
		
	};
	
	public abstract String getName();
}
