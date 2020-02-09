package cn.bohoon.message.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.message.domain.EmailConfig;
import cn.bohoon.message.domain.MessageType;
import cn.bohoon.message.domain.SmsConfig;

@Entity
@Table(name = "t_message_config" )
public class MessageConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	
	@Enumerated(EnumType.STRING)
	MessageType type; //消息类型编码
	String configJson;//配置参数
	
	@Transient
	public EmailConfig emailConfig(){
		return JsonUtil.parse(configJson, EmailConfig.class);
	}
	
	@Transient
	public SmsConfig smsConfig(){
		return JsonUtil.parse(configJson, SmsConfig.class);
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


	public String getConfigJson() {
		return configJson;
	}
	public void setConfigJson(String configJson) {
		this.configJson = configJson;
	}
	
	

}
