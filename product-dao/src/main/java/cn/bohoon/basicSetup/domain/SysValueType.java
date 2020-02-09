package cn.bohoon.basicSetup.domain;

public enum SysValueType {

	INPUT {

		@Override
		public String getName() {
			return "文本";
		}
	},
	FILE {

		@Override
		public String getName() {
			return "附件";
		}

	},
	RADIO {
		@Override
		public String getName() {
			return "单选框";
		}
	},
	SELECT {
		@Override
		public String getName() {
			return "下拉框";
		}
	},
	IMAGE {
		@Override
		public String getName() {
			return "图片";
		}
	},
	TABLE{
		@Override
		public String getName() {
			return "表格";
		}
	},
	TEXT{
		@Override
		public String getName() {
			return "固定文本";
		}
	};
	public abstract String getName();
}
