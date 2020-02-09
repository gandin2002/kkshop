package cn.bohoon.message.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.message.domain.MessageContentType;
import cn.bohoon.message.domain.SendType;
/**
 * 站内消息
 * @author hj
 * 2017年11月9日,上午10:26:04
 */
@Entity
@Table(name = "t_message_site")
public class MessageSite {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	
	@Column(columnDefinition="tinyint(2)")
	Boolean userType = true ; //TRUE 为个人 FALSE 为企业
	String sendTo; //接受人Id 
	String beFrom; //发送人ID
	String title;
	@Column(columnDefinition = "text ", nullable = true)
	String content;
	Date sendDate;
	@Enumerated(EnumType.STRING)
	SendType sendType;
	@Enumerated(EnumType.STRING)
	MessageContentType messageContentType;
	
	@Column(columnDefinition="tinyint(2)")
	Boolean readState = false ; 
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getUserType() {
		return userType;
	}
	public void setUserType(Boolean userType) {
		this.userType = userType;
	}
	public String getSendTo() {
		return sendTo;
	}
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}
	public String getBeFrom() {
		return beFrom;
	}
	public void setBeFrom(String beFrom) {
		this.beFrom = beFrom;
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
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public SendType getSendType() {
		return sendType;
	}
	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}
	public MessageContentType getMessageContentType() {
		return messageContentType;
	}
	public void setMessageContentType(MessageContentType messageContentType) {
		this.messageContentType = messageContentType;
	}
	public Boolean getReadState() {
		return readState;
	}
	public void setReadState(Boolean readState) {
		this.readState = readState;
	}
	
	
}
