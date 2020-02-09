package cn.bohoon.util;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import cn.bohoon.framework.service.BaseService;

public class IDUtil {
	private static String[] enPos = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"} ;
	private static IDUtil instance = new IDUtil();  
	private IDUtil (){}

	static Logger logger = LoggerFactory.getLogger(IDUtil.class) ;

	public static IDUtil getInstance() {  
		return instance;  
	}  

	public static int rand(long num) {
		return (int) (Math.random() * num);
	}
	
	public synchronized String getId(String prefix) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy{0}dd{1}mmssSSS");
		Calendar calendar = Calendar.getInstance() ;
		String month = enPos[calendar.get(Calendar.MONTH)] ;
		String hour = enPos[calendar.get(Calendar.HOUR_OF_DAY)] ;
		String result = prefix.concat(sdf.format(calendar.getTime())) ;
		result = MessageFormat.format(result, month,hour) ;
		return result ;
	}
	
	/**
	 * 
	 * @param baseService
	 * @param clazz
	 * @param prefix
	 * @param col
	 * @return
	 */
	public synchronized String getIdByDb(BaseService<?,?> baseService,Class<?> clazz,String prefix,String col)  {
		String id = "" ;
		try {
			Date date = new Date() ;
			String prefixDate =IDUtil.getPrefixDate(prefix, date);
			if(StringUtils.isEmpty(col)) {
				col = "id" ;
			}
			String hql = "select max(substring("+col+",9,12)) from " + clazz.getName() + " where substring("+col+", 1, 8)=?";
			String count = (String)baseService.select(hql ,String.class, prefixDate);
			if (count==null) {
				id = IDUtil.getId(prefixDate,1);
			} else {
				id = IDUtil.getId(prefixDate,new Long(count)+1) ;
			}
			
		} catch (Exception e) {
			logger.error("increate id error "+e);
		}
		return id ;
	}
	
	
	/**
	 * 
	 * @param baseService
	 * @param clazz
	 * @param prefix
	 * @param col
	 * @return
	 */
	public synchronized String getOrderIdByDb(BaseService<?,?> baseService,Class<?> clazz)  {
		String id = "" ;
		try {
			Date date = new Date() ;
			String prefixDate =IDUtil.getPrefixDate("", date);
			
			String hql = "select max(substring(id,7,10)) from " + clazz.getName() + " where substring(id, 1, 6)=?";
			String count = (String)baseService.select(hql ,String.class, prefixDate);
			if (count==null) {
				id = IDUtil.getId(prefixDate,1);
			} else {
				id = IDUtil.getId(prefixDate,new Long(count)+1) ;
			}
			
		} catch (Exception e) {
			logger.error("increate id error "+e);
		}
		return id ;
	}
	

	public static String getPrefixDate(String prefix, Date date) {
		// 按楼主的要求格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return prefix + sdf.format(date);
	}
	
	public static String getId(String prefixDate,Number number){
		return prefixDate + getNumString(number,"0000");
	}

	public static String getNumString(Number number,String style) {
		// 按楼主的要求格式化数字
		DecimalFormat df = new DecimalFormat();
		df.applyPattern(style);
		return df.format(number);
	}
	
	public synchronized String getCommonId(BaseService<?,?> baseService,Class<?> clazz){
		String id = getMemberId() ;
		String hql = "select count(id) from " + clazz.getName() + " where id=?";
		Long count = (Long)baseService.select(hql ,Long.class, id);
		if(count == null || count == 0 ) {
			return id ;
		}
		return getCommonId(baseService,clazz) ;
	}
	
 
	
	public static String getMemberId(){
		String first= randString("12356789",1);
		String end= randString("0123456789",3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
		return first + LocalDate.now().format(formatter) + end;
	}
	
	public static String randString(int length) {
		String ssource = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		return randString(ssource,length);
	}

	public static String randString(String str,int length) {
		char[] src = str.toCharArray();
		char[] buf = new char[length];
		Random ran = new Random();

		for (int i = 0; i < length; i++) {
			int rnd = ran.nextInt(src.length);
			buf[i] = src[rnd];
		}
		return new String(buf);
	}
	
	

}
