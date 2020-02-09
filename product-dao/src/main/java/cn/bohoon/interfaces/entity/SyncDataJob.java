package cn.bohoon.interfaces.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.interfaces.service.SyncGroupService;

@Entity
@Table(name = "t_sync_job")
public class SyncDataJob {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Integer groupId; // 同步组ID
	String jobName;
	String sceneUrl ; //场景触发URL 如果有则按照场景触发规则
	String cronExpression; // 定时触发规则
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date lastExecute ; //最近一次执行时间
	@Column(columnDefinition="tinyint(2)")
	Boolean state = true; // 最近一次执行状态
	Integer lastLogId ;//最近一次日志ID
	Integer moduleId;// 模块ID
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}
	
	@Transient
	@JSONField(serialize=false)
	public SyncGroup getGroup() {
		SyncGroupService service = SpringContextHolder.getBean(SyncGroupService.class) ;
		if(!StringUtils.isEmpty(groupId)) {
			return service.get(groupId) ;
		}
		return null ;
	}
	
	@Transient
	@JSONField(serialize=false)
	public List<SyncGroup> getGroupList(){
		if(!StringUtils.isEmpty(moduleId)){
			SyncGroupService service = SpringContextHolder.getBean(SyncGroupService.class) ;
			List<SyncGroup> list = service.list(" from SyncGroup where moduleId = ? and status = 1 order by sort asc ", moduleId);
			return list;
		}
		return null;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getSceneUrl() {
		return sceneUrl;
	}

	public void setSceneUrl(String sceneUrl) {
		this.sceneUrl = sceneUrl;
	}

	public Date getLastExecute() {
		return lastExecute;
	}

	public void setLastExecute(Date lastExecute) {
		this.lastExecute = lastExecute;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public Integer getLastLogId() {
		return lastLogId;
	}

	public void setLastLogId(Integer lastLogId) {
		this.lastLogId = lastLogId;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
}
