package cn.bohoon.express.entity;

/**
 * 从第三方获取物流信息进一步封装。
 * @author Administrator
 *
 */

public class LogisticsUtils {

	private String time;
	private String ftime;
	private String context;
	private String location;
	private String week;
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFtime() {
		return ftime;
	}
	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	/*@Override
	public String toString() {
		return "LogisticsUtils [time=" + time + ", ftime=" + ftime + ", context=" + context + ", location=" + location
				+ "]";
	}*/
	
	
	
}
