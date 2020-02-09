package cn.bohoon.message.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 消息日志
 * @author HJ
 * 2017年12月27日,下午1:53:20
 */
@Entity
@Table(name="t_message_remind_log")
public class MessageRemindLog {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	Integer msgId;//消息ID
	
	String operatorId;// 管理员ID
	
	Date readDate;//阅读时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
}
