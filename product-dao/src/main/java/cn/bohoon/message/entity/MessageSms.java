package cn.bohoon.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.message.domain.SendType;
/**
 * 短信消息
 * @author hj
 * 2017年11月9日,上午10:26:15
 */
@Entity
@Table(name = "t_message_sms")
public class MessageSms {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	String beFrom;
	String sendTo;
	String title;
	String content;
	@Column(columnDefinition="tinyint(2)")
	Boolean state;
	@Enumerated(EnumType.STRING)
	SendType sendType;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBeFrom() {
		return beFrom;
	}
	public void setBeFrom(String beFrom) {
		this.beFrom = beFrom;
	}
	public String getSendTo() {
		return sendTo;
	}
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
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
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public SendType getSendType() {
		return sendType;
	}
	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}
}
