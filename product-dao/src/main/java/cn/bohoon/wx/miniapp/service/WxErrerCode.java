package cn.bohoon.wx.miniapp.service;

public enum WxErrerCode {
	
 
	USER_NOT_FOUND{

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "未找到用户信息";
		}

		@Override
		public Integer getState() {
			// TODO Auto-generated method stub
			return 10001;
		}
		
	},
	COMPANY_NOT_FOUND{

		@Override
		public Integer getState() {
			// TODO Auto-generated method stub
			return 10002;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "企业未找到！";
			
		}
	},
	DEPARTMENT_NOT_FOUND{

		@Override
		public Integer getState() {
			// TODO Auto-generated method stub
			return 10003;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "企业部门未找到！";
		}
		
	},
	PLEASE_INPUT_PHONE{

		@Override
		public Integer getState() {
			// TODO Auto-generated method stub
			return 10004;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "请输入你的手机号！";
		}
		
	},
	PHONE_EXIST{

		@Override
		public Integer getState() {
			// TODO Auto-generated method stub
			return 10005;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "手机号已经存在!";
		}
		
	};
	public abstract Integer getState();
	public abstract String getName();
}
