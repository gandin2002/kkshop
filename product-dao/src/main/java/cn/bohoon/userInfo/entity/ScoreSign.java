package cn.bohoon.userInfo.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.userInfo.domain.SignStatus;

/**
 * 积分签到表
 * @author Administrator
 *
 */

@Entity
@Table(name="t_score_sign")
public class ScoreSign {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	Integer id;			// 主键id
	Integer u_id;		// 用户id
	Date last_time;		// 最后签到的时间
	BigDecimal sign_times = new BigDecimal(0);  // 签到的次数
	@Enumerated(EnumType.STRING)
	SignStatus states = SignStatus.RECEIVE_INTEGRAL;					// 默认是领取积分状态
	@Column(columnDefinition = "text")
	String month_days;					// 一个月中签到的所有记录，用json描述
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getU_id() {
		return u_id;
	}
	public void setU_id(Integer u_id) {
		this.u_id = u_id;
	}
	public Date getLast_time() {
		return last_time;
	}
	public void setLast_time(Date last_time) {
		this.last_time = last_time;
	}
	public BigDecimal getSign_times() {
		return sign_times;
	}
	public void setSign_times(BigDecimal sign_times) {
		this.sign_times = sign_times;
	}
	public SignStatus getStates() {
		return states;
	}
	public void setStates(SignStatus states) {
		this.states = states;
	}
	public String getMonth_days() {
		return month_days;
	}
	public void setMonth_days(String month_days) {
		this.month_days = month_days;
	}
	
	
	
	
	
	
}
