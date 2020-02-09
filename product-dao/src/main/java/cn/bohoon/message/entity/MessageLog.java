package cn.bohoon.message.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * 消息记录
 * 
 * @author HJ 2018年3月30日,下午3:56:15
 */
@Table(name = "t_message_log")
@Entity
public class MessageLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	@Column(columnDefinition="text")
	String content; //发送内容
	
	@DateTimeFormat(iso=ISO.DATE_TIME)
	Date sendTime; //发送时间
	Boolean sendStatus;//发生状态
	String userNumber; //发送用户
	String msgType; //发送类型 
	String templateId; //模板ID
	String tMsgType;//模板消息类型
	Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Boolean getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Boolean sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String gettMsgType() {
		return tMsgType;
	}

	public void settMsgType(String tMsgType) {
		this.tMsgType = tMsgType;
	}
	
}
