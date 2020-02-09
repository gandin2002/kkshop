package cn.bohoon.order.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_back_info")
public class BackInfomation {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	String userId;
	String protectId;
	String theme;
	String descs;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProtectId() {
		return protectId;
	}
	public void setProtectId(String protectId) {
		this.protectId = protectId;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
}
