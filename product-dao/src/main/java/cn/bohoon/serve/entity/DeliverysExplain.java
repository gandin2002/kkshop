package cn.bohoon.serve.entity;
/*
 * 配送说明
 */

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "t_deliverys_explain")
public class DeliverysExplain {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;                              //主键
	String scopeType;                        //范围类型
	String explains; 						 //配送说明
	Integer state = 0;                       //启用状态       0 未启用   1启用
	Date createTime;						 //创建时间
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getScopeType() {
		return scopeType;
	}
	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}
	
	public String getExplains() {
		return explains;
	}
	public void setExplains(String explains) {
		this.explains = explains;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
