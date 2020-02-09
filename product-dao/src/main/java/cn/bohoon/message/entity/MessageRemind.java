package cn.bohoon.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 消息提醒
 * @author hj
 * 2017年11月9日,上午10:25:55
 */
//消息提醒
@Entity
@Table(name = "t_message_remind")
public class MessageRemind {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id; //ID
	@Column(columnDefinition="tinyint(2)")
	private Boolean state; //状态
	private String label; //标注
	private String title; //项目标题
	private String code;//标识
	private String msg;//需要发送的消息

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
