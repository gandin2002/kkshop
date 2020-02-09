package cn.bohoon.page.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 页面配置 帮助中心
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_help_center")
public class HelpCenter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	String loc;// pc端 还是微信端
	Integer pid = 0;
	String name; // 页面配置-帮助中心-栏目名称
	Integer level; // 页面配置-帮助中心-栏目级别
	@Column(columnDefinition="tinyint(2)")
	Boolean status ; //页面配置-帮助中心-栏目启用状态
	@Column(columnDefinition = "text")
	String content ; //页面配置-帮助中心-栏目内容
	Integer sort ; //页面配置-帮助中心-栏目序号
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
