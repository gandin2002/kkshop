package cn.bohoon.page.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.framework.util.DateUtil;

/** 公告管理 */
@Entity
@Table(name = "t_notice")
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 主键id
	private String title; // 公告标题
	@Lob
	@Column(columnDefinition = "text")
	private String content; // 内容
	
	@Column(columnDefinition = "datetime(0)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime; // 创建时间
	@Column(columnDefinition = "datetime(0)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modifyTime; // 修改时间
	@Column(columnDefinition = "datetime(0)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishTime; // 发布时间
	
	private Boolean status; // 状态 0-未发布 1-已发布
	
	

	public Notice() {
		super();
	}

	public Notice(Integer id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	
	

	public Notice(Integer id, String title, String content, Date createTime, Date modifyTime, Date publishTime,
			Boolean status) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.publishTime = publishTime;
		this.status = status;
	}

	@Transient
	public String getCreateTimeString() {
		return DateUtil.formatDate(this.createTime);
	}
	
	@Transient
	public String getModifyTimeString() {
		return DateUtil.formatDate(this.modifyTime);
	}
	
	@Transient
	public String getPublishTimeString() {
		return DateUtil.formatDate(this.publishTime);
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}


}