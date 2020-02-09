package cn.bohoon.order.entity;

import java.util.Date;

import org.springframework.util.StringUtils;

import cn.bohoon.excel.util.ExportConfig;

public class MymaintenceReponrExecelModel {
	@ExportConfig(name = "维保订单")
	 String id; // 维保订单ID
	@ExportConfig(name= "状态")
	String state;
	@ExportConfig(name= "维保项目")
	String   maintennanceItem;  //维保项目
	
	@ExportConfig(name= "提交时间")
	Date  submitTime;
	
	@ExportConfig(name= "完成时间")
   String 	finishTime;
  
	@ExportConfig(name= "技术员")
   String 	handlePeople;
	

	
	public void setReportParams(Mymaintenance mymaintenance){
		this.id=mymaintenance.getId();
		if(!StringUtils.isEmpty(mymaintenance.getState())){
			if(mymaintenance.getState()==0){
				this.state="未处理";
			}else{
				this.state="已处理";
			}
		}
		this.maintennanceItem=mymaintenance.getMaintennanceItem();
		this.handlePeople=mymaintenance.getHandlePeople();
		
		this.submitTime=mymaintenance.getSubmitTime();
		this.finishTime=mymaintenance.getFinishTime();
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMaintennanceItem() {
		return maintennanceItem;
	}

	public void setMaintennanceItem(String maintennanceItem) {
		this.maintennanceItem = maintennanceItem;
	}


	public String getHandlePeople() {
		return handlePeople;
	}

	public void setHandlePeople(String handlePeople) {
		this.handlePeople = handlePeople;
	}


	

	
	 
	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}


	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

}
