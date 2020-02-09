package cn.bohoon.userInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 用户等级表
 * @author hj
 * 2017年11月9日,上午10:24:56
 */
@Entity
@Table(name="t_user_level")
public class UserLevel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id ; 
	private String name; //名称
	private String level;//等级
	private Integer exp; //所需经验
	@Column(columnDefinition="tinyint(2)")
	private Boolean state;//状态 TRUE启用 FALSE 禁用
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getExp() {
		return exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	
}
