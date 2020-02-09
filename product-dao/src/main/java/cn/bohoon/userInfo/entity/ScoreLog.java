package cn.bohoon.userInfo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.bohoon.userInfo.domain.ScoreType;

/**
 * 积分日志
 * @author hj
 *2017年11月9日10:18:44
 */
@Entity
@Table(name="t_score_log")
public class ScoreLog {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	Integer id;
	String memberId;
	String orderId;
	Integer score;
	String note;
	@Enumerated(EnumType.STRING)
	ScoreType scoreType; //积分类型
	@Column(columnDefinition="tinyint(2)")
	Boolean flag = false ; //收入？支出  如果是true 则表示收入 如果是false 表示支出
	Date createDate = new Date();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
 
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public ScoreType getScoreType() {
		return scoreType;
	}
	public void setScoreType(ScoreType scoreType) {
		this.scoreType = scoreType;
	}
	
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
