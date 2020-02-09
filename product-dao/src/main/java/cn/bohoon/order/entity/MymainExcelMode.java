package cn.bohoon.order.entity;
import java.util.Date;
import org.springframework.util.StringUtils;
import cn.bohoon.excel.util.ExportConfig;
public class MymainExcelMode {
	@ExportConfig(name = "维保订单")
	 String id; // 维保订单ID
	@ExportConfig(name= "状态")
	String state;
	@ExportConfig(name= "维保项目")
	String   maintennanceItem;  //维保项目
	
	@ExportConfig(name= "会员名")
	String  userName;  //会员名
	
	@ExportConfig(name= "维保联系人")
	String  realName; //维保联系人姓名
	
	@ExportConfig(name= "维保人电话")
	String ipohe;  //维保人电话
	

	@ExportConfig(name= "预约时间")
	String   startTime; //预约时间
	
	@ExportConfig(name= "提交时间")
	Date  submitTime;
	
	@ExportConfig(name= "公司名称")
	String company=""; // 所属企业
	
	@ExportConfig(name= "完成时间")
    String 	finishTime;

	
	public void setParams(Mymaintenance mymaintenance){
		this.id=mymaintenance.getId();
		if(!StringUtils.isEmpty(mymaintenance.getState())){
			if(mymaintenance.getState()==0){
				this.state="未处理";
			}else{
				this.state="已处理";
			}
		}
		this.maintennanceItem=mymaintenance.getMaintennanceItem();
		this.userName=mymaintenance.getUserName();
		this.realName=mymaintenance.getRealName();
		this.ipohe=mymaintenance.getIpohe();
		this.startTime=mymaintenance.getStartTime();
		this.submitTime=mymaintenance.getSubmitTime();
		this.company=mymaintenance.getCompany();
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIpohe() {
		return ipohe;
	}

	public void setIpohe(String ipohe) {
		this.ipohe = ipohe;
	}

	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	 
	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	
}
