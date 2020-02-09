package cn.bohoon.interfaces.domain;

public enum SourceType {

	SQLSERVER{
		@Override
		public String getName() {
			return "SQL SERVER";
		}
		@Override
		public String getCode() {
			return "sqlserver";
		}
		@Override
		public String getDriver() {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		@Override
		public String urlFormat() {
			return "jdbc:sqlserver://{0}:{1};DatabaseName={2}";
		}
	},
	MySQL{
		@Override
		public String getName() {
			return "MYSQL";
		}
		@Override
		public String getCode() {
			return "mysql";
		}
		@Override
		public String getDriver() {
			return "com.mysql.jdbc.Driver";
		}
		@Override
		public String urlFormat() {
			return "jdbc:mysql://{0}:{1}/{2}?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		}
	},
	ORACLE{
		@Override
		public String getName() {
			return "ORACLE";
		}
		@Override
		public String getCode() {
			return "oracle";
		}
		@Override
		public String getDriver() {
			return "oracle.jdbc.driver.OracleDriver";
		}
		@Override
		public String urlFormat() {
			return "jdbc:oracle:thin:@{0}:{1}:{2}";
		}
	},
	WEBSERVICE{

		@Override
		public String getName() {
			return "ws接口";
		}

		@Override
		public String getCode() {
			return "webservice";
		}

		@Override
		public String getDriver() {
			return "{0}";
		}

		@Override
		public String urlFormat() {
			return "{0}";
		}
		
	}
	;
	public abstract String getName();
	public abstract String getCode();
	public abstract String getDriver() ;
	public abstract String urlFormat() ;
}
