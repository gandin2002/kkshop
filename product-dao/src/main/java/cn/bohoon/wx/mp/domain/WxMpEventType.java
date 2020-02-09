package cn.bohoon.wx.mp.domain;
/**
 * 微信平台响应类型
 * @author HJ
 * 2018年3月8日,下午5:27:23
 */
public enum WxMpEventType {
	VIEW{
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "view";
		}
		
	},
	subscribe{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "关注事件";
		}
		
	},unsubscribe{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "取消关注事件";
		}
	},SCAN{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "已关注扫描二维码事件";  //未关注 == subscribe
		}
		
	},LOCATION{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "上报地理位置事件";
		}
		
	},CLICK{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "自定义菜单事件";
		}
	},TEMPLATESENDJOBFINISH {
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "模板消息送达情况提醒";
		}
	};
	
	public abstract String getName();
}
