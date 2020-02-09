package cn.bohoon.express.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.bohoon.express.entity.Logistics;
import cn.bohoon.express.entity.LogisticsUtils;
import cn.bohoon.framework.util.JsonUtil;

/**
 * 用于封装物流信息的时间
 * @author Administrator
 *
 */

public class LogisticsHelper {

	// 星期的封装
	public static String[] week = {"","周日","周一","周二","周三","周四","周五","周六"};	

	
	/**
	 * 1.进行时间的排序
	 * @throws Exception 
	 */
	public  static List<LogisticsUtils> enTime(List<LogisticsUtils> list) throws Exception{
		
		List<LogisticsUtils> indexList = new ArrayList<LogisticsUtils>();
		// 认证的时间
		HashSet<String> isTime = new HashSet<String>();
		
		for (LogisticsUtils logistics : list){
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = sf.parse(logistics.getTime());
			
			SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
			String time = sf2.format(parse);
			logistics.setTime(time);
			
			SimpleDateFormat sf3 = new SimpleDateFormat("HH:mm:ss");
			String ftime = sf3.format(parse);
			logistics.setFtime(ftime);
			
			
			// 判读时间
			boolean flag = isTime.contains(time);
			
			if (flag){
				
				logistics.setTime(null);
			}else{
				
				Calendar calender = Calendar.getInstance();
				calender.setTime(parse);
				int i = calender.get(Calendar.DAY_OF_WEEK);
				logistics.setWeek( week[i] );
				
			}
			isTime.add(time);
			
			indexList.add(logistics);
		}
		
		
		return indexList;
	}
	
	/**
	 * 2.将字符串转换成list对象集合
	 */
	public static List<LogisticsUtils> toList(String resultUrl){
		
		List<LogisticsUtils> list = new ArrayList<LogisticsUtils>();
		
		JSONObject json  = JSON.parseObject(resultUrl) ;
		if(json.containsKey("data") ) {
			JSONArray jsonArray = json.getJSONArray("data") ;
			
			if (jsonArray.size() <= 0)
				return null;
			
			JSONObject object = (JSONObject) jsonArray.get(1);
			
			for (Object obj : jsonArray){
				
				JSONObject node = (JSONObject) obj;
				
				String time = node.getString("time");
				String context = node.getString("context");
				String ftime = node.getString("ftime");
				String location = node.getString("location");
				
				LogisticsUtils  logi = new LogisticsUtils();
				logi.setContext(context);
				logi.setFtime(ftime);
				logi.setLocation(location);
				logi.setTime(ftime);
				list.add(logi);
			}
			
		}
		
		// 反转
		Collections.reverse(list);
		
		return list;
		
	}
	
	/**
	 * 2.讲fastJson转换成List<LogisticsUtils>
	 */
}
