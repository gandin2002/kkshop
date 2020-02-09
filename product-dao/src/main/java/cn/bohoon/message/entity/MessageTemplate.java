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
import javax.persistence.Transient;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.service.UniversalService;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.domain.SendType;
/**
 * 消息模板
 * @author hj
 * 2017年11月9日,上午10:26:24
 */
@Entity
@Table(name = "t_message_template")
public class MessageTemplate {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	@Enumerated(EnumType.STRING)
	MessageType type; //消息类型编码
	String title;
	@Column(columnDefinition = "text not null default ''")
	String content;
	Date setupTime;
	@Column(columnDefinition="tinyint(2)")
	Boolean state; //true 发送成功  false 发送失败
	@Enumerated(EnumType.STRING)
	SendType sendType;
	
	Long succeedNum = 0L; //成功次数
	Long failureNum = 0L; //失败次数
	
	@Transient
	public Long countSuccess(){
		String hql = "select count(1) from Message"+ type.name()  +" where sendType = ? and state = ?";
		UniversalService universalService = (UniversalService)SpringContextHolder.getBean("universalService");
	    return universalService.select(hql, Long.class, this.sendType,true);
	}
	
	@Transient
	public Long countFail(){
		String hql = "select count(1) from Message"+ type.name()  +" where sendType = ? and state = ?";
		UniversalService universalService = SpringContextHolder.getBean(UniversalService.class);
	    return universalService.select(hql, Long.class, this.sendType,false);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
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
	public Date getSetupTime() {
		return setupTime;
	}
	public void setSetupTime(Date setupTime) {
		this.setupTime = setupTime;
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

	public Long getSucceedNum() {
		return succeedNum;
	}

	public void setSucceedNum(Long succeedNum) {
		this.succeedNum = succeedNum;
	}

	public Long getFailureNum() {
		return failureNum;
	}

	public void setFailureNum(Long failureNum) {
		this.failureNum = failureNum;
	}
}
