package cn.bohoon.interfaces.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.interfaces.domain.SyncType;

@Entity
@Table(name = "t_sync_log")
public class SyncDataLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; // 同步日志ID 自动生成
	String groupName;// 同步组名称
	@Column(columnDefinition = "longtext")
	String errorSql; // 执行失败的sql
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date syncTime = new Date(); // 同步创建时间
	@Enumerated(EnumType.STRING)
	SyncType syncType = SyncType.USER_SYNC ; // 同步类型
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date syncFinishTime; // 同步完成时间
	@Column(columnDefinition="tinyint(2)")
	Boolean state = true; // 同步结果状态

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getErrorSql() {
		return errorSql;
	}

	public void setErrorSql(String errorSql) {
		this.errorSql = errorSql;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public SyncType getSyncType() {
		return syncType;
	}

	public void setSyncType(SyncType syncType) {
		this.syncType = syncType;
	}

	public Date getSyncFinishTime() {
		return syncFinishTime;
	}

	public void setSyncFinishTime(Date syncFinishTime) {
		this.syncFinishTime = syncFinishTime;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

}
