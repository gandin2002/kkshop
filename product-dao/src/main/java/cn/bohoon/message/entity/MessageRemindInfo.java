package cn.bohoon.message.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 消息提醒内容
 * @author HJ
 * 2017年12月27日,下午3:06:11
 */
@Entity
@Table(name="t_message_remind_info")
public class MessageRemindInfo {
	 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;

	String title;//标题
	
	String content;//正文
	
	Date createTime;//发送时间
	
	Integer messageRemindId;//消息类型
	
	

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
 
	public Integer getMessageRemindId() {
		return messageRemindId;
	}

	public void setMessageRemindId(Integer messageRemindId) {
		this.messageRemindId = messageRemindId;
	}
}
