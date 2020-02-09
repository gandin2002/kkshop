package cn.bohoon.order.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONArray;

import cn.bohoon.framework.util.DateUtil;
import cn.bohoon.framework.util.JsonUtil;
import cn.bohoon.order.domain.SendGoodInfo;

/**
 * 发货单
 */
@Entity
@Table(name = "t_send_goods")
public class SendGoods {

	@Id
	String id; // 主键ID FH17J234893
	@Column(length=64)
	String orderId; // 订单ID
	
	String transCompany; // 物流名称
	String transCompanycode;// 物流编码
	String transNum;  //快递编号
	String revicer; // 收货人
	String rePhone; // 收货人联系方式
	String reArea; // 收货地区
	String reAdress; // 收货地址
	
	String sender; // 发货人
	String sendAdress; // 发货地址
	String senderCompany; // 厂库名称/发货人企业名称
	
	Integer sendId; // 仓库ID
	String sendItem; // 发货清单 json 格式
	Integer currSendNum; // 发货数量
	
	@Column(columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date sendTime; // 发货时间
	Date affirmTime;//确定时间
	Date createTime = new Date();//生成时间
	String note; // 备注

	@Transient
	public String getTimeString() {
		return DateUtil.formatDate(this.sendTime);
	}
	@Transient
	public String getAffirmTimeString() {
		return DateUtil.formatDate(this.affirmTime);
	}
	@Transient
	public String getCreateTimeString() {
		return DateUtil.formatDate(this.createTime);
	}
	
	@Transient
	public Map<Integer, Integer> getSendItemMap() {
		Map<Integer, Integer> alMap = new HashMap<Integer, Integer>();
		JSONArray array = JsonUtil.parse(this.sendItem, JSONArray.class);
		if (null != array && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				String jsonStr = array.getJSONObject(i).toJSONString();
				SendGoodInfo sdi = JsonUtil.parse(jsonStr, SendGoodInfo.class);
				Integer v = 0;
				if (alMap.containsKey(sdi.getId())) {
					v = alMap.get(sdi.getId());
				}
				alMap.put(sdi.getId(), v + sdi.getNum());
			}
		}
		return alMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransCompany() {
		return transCompany;
	}

	public void setTransCompany(String transCompany) {
		this.transCompany = transCompany;
	}

	public String getTransCompanycode() {
		return transCompanycode;
	}

	public void setTransCompanycode(String transCompanycode) {
		this.transCompanycode = transCompanycode;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getRevicer() {
		return revicer;
	}

	public void setRevicer(String revicer) {
		this.revicer = revicer;
	}

	public String getRePhone() {
		return rePhone;
	}

	public void setRePhone(String rePhone) {
		this.rePhone = rePhone;
	}

	public String getReArea() {
		return reArea;
	}

	public void setReArea(String reArea) {
		this.reArea = reArea;
	}

	public String getReAdress() {
		return reAdress;
	}

	public void setReAdress(String reAdress) {
		this.reAdress = reAdress;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSendAdress() {
		return sendAdress;
	}

	public void setSendAdress(String sendAdress) {
		this.sendAdress = sendAdress;
	}

	public String getSenderCompany() {
		return senderCompany;
	}

	public void setSenderCompany(String senderCompany) {
		this.senderCompany = senderCompany;
	}

	public Integer getSendId() {
		return sendId;
	}

	public void setSendId(Integer sendId) {
		this.sendId = sendId;
	}

	public String getSendItem() {
		return sendItem;
	}

	public void setSendItem(String sendItem) {
		this.sendItem = sendItem;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getCurrSendNum() {
		return currSendNum;
	}

	public void setCurrSendNum(Integer currSendNum) {
		this.currSendNum = currSendNum;
	}

	public Date getAffirmTime() {
		return affirmTime;
	}

	public void setAffirmTime(Date affirmTime) {
		this.affirmTime = affirmTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
