package cn.bohoon.interfaces.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.interfaces.domain.SourceType;

/**
 * ERP数据源
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_erp_link")
public class ErpLink {

	@Id
	String id; //
	String sourceName;// 数据源名称
	@Enumerated(EnumType.STRING)
	SourceType sourceType;// 数据库类型
	String hostName;// 主机
	String port;// 端口号
	String username; // 数据库用户名
	String password; // 数据库密码
	String dbName;// 数据库名称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}
