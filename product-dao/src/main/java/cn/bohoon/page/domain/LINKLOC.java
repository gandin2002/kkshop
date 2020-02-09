package cn.bohoon.page.domain;

public enum LINKLOC {

	bottom{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "底部链接";
		}
		
	},
	friend{

		@Override
		public String getName() {
			return "友情链接";
		}
		
	},
	footer{

		@Override
		public String getName() {
			return "footer链接";
		}
		
	},
	header{

		@Override
		public String getName() {
			return "头部导航";
		}
		
	},
	center{

		@Override
		public String getName() {
			return "个人中心";
		}
		
	};
	
	public abstract String getName() ;
}
