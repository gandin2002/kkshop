package cn.bohoon.page.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t_sms_log")
public class SmsLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id; // 主键id
	String ipAdress; // 请求发送IP
	String mobile; // 手机号码
	String content; // 内容
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date visitDate = new Date();

	public SmsLog() {
		super();
	}

	public SmsLog(String ipAdress,String mobile,String content) {
		super();
		this.ipAdress = ipAdress;
		this.mobile = mobile ;
		this.content = content ;
	}

	public SmsLog(Integer id, String ipAdress, String mobile, String content, Date visitDate) {
		super();
		this.id = id;
		this.ipAdress = ipAdress;
		this.mobile = mobile;
		this.content = content;
		this.visitDate = visitDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

}
