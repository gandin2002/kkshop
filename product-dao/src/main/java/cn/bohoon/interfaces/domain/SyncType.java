package cn.bohoon.interfaces.domain;

public enum SyncType {
	
	USER_SYNC {
		@Override
		public String getName() {
			return "手动同步";
		}
	},
	AUTO_SYNC {
		@Override
		public String getName() {
			return "定时任务同步";
		}
	};
	public abstract String getName();
}
