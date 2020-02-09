package cn.bohoon.message.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_message_remind_role")
public class MessageRemindRole {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	Integer mrId;//消息提醒类型
	
	Integer roleId;//角色ID

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMrId() {
		return mrId;
	}

	public void setMrId(Integer mrId) {
		this.mrId = mrId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}