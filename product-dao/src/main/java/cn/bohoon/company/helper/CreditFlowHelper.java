package cn.bohoon.company.helper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreditFlowHelper {

	

	public static Date getPeriodCreditDate(int period) {
		int nowd = getDateNow();
		Date pdd = getPeriodDate(period);

//		System.out.println("今天是：" + nowd + "号，记账日是：" + period + "号");
//		System.out.print("记账第一个周期：");
		DateFormat formatter = DateFormat.getDateTimeInstance();
		if (period < nowd) {// 记账周期在本日之前
			System.out.println(formatter.format(pdd));
		} else { // 记账周期在本日之后
			pdd = getPeriodDate(period, false);
			System.out.println(formatter.format(pdd));
		}
		
		return pdd ;
	}
	
	
	public static int getDateNow() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	private static Date getPeriodDate(int period) {
		return getPeriodDate(period, true);
	}

	public static Date getPeriodDate(int peroid, boolean flag) {
		Date d = Calendar.getInstance().getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		
		if (!flag) {
			cal.add(Calendar.MONTH, -1);
		}
		cal.set(Calendar.DATE, peroid);
		return cal.getTime();
	}

	public static Date getPreMonDate() {
		Date d = Calendar.getInstance().getTime();
		return getPreMonDate(d);
	}

	public static Date getPreMonDate(Date datNow) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(datNow);
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}
	
	// 获取月份
	public static int getMonth(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	} 
	
	// 获取下个月份
	public static Date getPreNextMonDate(Date datNow) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(datNow);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}
	
	// 天数的增加和减少功能
	public static Date getAddOrReduce(Date datNow,Integer sum){
		Calendar cal = Calendar.getInstance();
		cal.setTime(datNow);
		cal.add(Calendar.DATE, sum);
		
		return cal.getTime();
	}

}
