package cn.bohoon.wx.mp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import cn.bohoon.message.domain.MessageNodeType;

/**
 * 微信消息模板
 * 
 * @author HJ 2018年3月23日,下午2:35:36
 */
@Table(name="t_wx_msg_template")
@Entity
public class WxMsgTemplate {

	@Id
	String template_id;
	String title = "";
	String primary_industry ="";
	String deputy_industry = "";
	String content = "";
	String example = "";
	@Column(columnDefinition="text")
	String arraycnt =""; 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	String setupDate ; //设置时间
	Long succeedNum = 0L; //成功次数
	Long failureNum = 0L; //失败次数
	Boolean state = true;
	@Enumerated(EnumType.STRING)
	MessageNodeType messageNodeType = MessageNodeType.NOT_BINDING;
	
	
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrimary_industry() {
		return primary_industry;
	}
	public void setPrimary_industry(String primary_industry) {
		this.primary_industry = primary_industry;
	}
	public String getDeputy_industry() {
		return deputy_industry;
	}
	public void setDeputy_industry(String deputy_industry) {
		this.deputy_industry = deputy_industry;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public String getArraycnt() {
		return arraycnt;
	}
	public void setArraycnt(String arraycnt) {
		this.arraycnt = arraycnt;
	}
	public String getSetupDate() {
		return setupDate;
	}
	public void setSetupDate(String setupDate) {
		this.setupDate = setupDate;
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
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public MessageNodeType getMessageNodeType() {
		return messageNodeType;
	}
	public void setMessageNodeType(MessageNodeType messageNodeType) {
		this.messageNodeType = messageNodeType;
	}
}
