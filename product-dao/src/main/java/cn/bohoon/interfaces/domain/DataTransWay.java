package cn.bohoon.interfaces.domain;

public enum DataTransWay {

	rw {
		@Override
		public String getName() {
			return "读写";
		}

		@Override
		public String getCode() {
			return "read and write";
		}
	},
	r {
		@Override
		public String getName() {
			return "只读";
		}

		@Override
		public String getCode() {
			return "read only";
		}
	},
	w {
		@Override
		public String getName() {
			return "只写";
		}

		@Override
		public String getCode() {
			return "write only";
		}
	};
	public abstract String getName();
	public abstract String getCode(); 
}
