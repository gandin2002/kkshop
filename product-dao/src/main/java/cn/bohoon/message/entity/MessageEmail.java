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

import cn.bohoon.message.domain.SendType;
/**
 * 邮件消息
 * @author hj
 * 2017年11月9日,上午10:25:43
 */
@Entity
@Table(name = "t_message_email")
public class MessageEmail {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	String beFrom;
	String sendTo;
	String title;
	@Column(columnDefinition = "text not null default ''")
	String content;
	@Column(columnDefinition="tinyint(2)")
	Boolean state;
	Date sendDate;
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
	
	
}
