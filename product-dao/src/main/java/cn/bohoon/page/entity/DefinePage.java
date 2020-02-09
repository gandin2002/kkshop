package cn.bohoon.page.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t_define_page")
public class DefinePage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	String name; // 页面名称
	String title; // 页面标题
	String synopsis; // 页面简介
	Integer types = 1 ; // 页面类型 1： PC端，2：移动端
	String templateId; // 所属模板ID
	String templateName; // 所属模板名称

	@Column(columnDefinition = "text")
	String content;// 页面内容
	@Column(columnDefinition = "int default 0")
	Integer quotes = 0 ;// 引用次数
	@Column(columnDefinition = "int default 0")
	Integer views = 0 ; // 浏览次数
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createTime; // 创建时间

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public Integer getTypes() {
		return types;
	}

	public void setTypes(Integer types) {
		this.types = types;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getQuotes() {
		return quotes;
	}

	public void setQuotes(Integer quotes) {
		this.quotes = quotes;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
