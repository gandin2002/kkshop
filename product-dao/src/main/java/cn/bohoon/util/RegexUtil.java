package cn.bohoon.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则 
 * @author HJ
 * 2018年3月27日,下午5:06:35
 */
public class RegexUtil {
	
	
	/**
	 * 匹配 数据 {{ data }} 拿取data 值
	 * @param str
	 * @return List<data> 
	 */
	public static List<String>   paraseData(String str){
		List<String> list = new ArrayList<String>();
		try {
			Pattern pattern = Pattern.compile("(?<=\\{\\{)(.+?)(?=\\}\\})");
			Matcher matcher = pattern.matcher(str);
			while(matcher.find()){
				String name = matcher.group();
				name =name.substring(0,name.indexOf(".DATA"));
				list.add(name);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	/**
	 *  匹配 数据 {data}  拿取data 值
	 * @param str
	 * @return
	 */
	public static List<String> paraseDataS(String str){
		List<String> list = new ArrayList<String>();
		try {
			Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
			Matcher matcher = pattern.matcher(str);
			while(matcher.find()){
				String name = matcher.group();
				name =name.substring(0,name.indexOf(".DATA"));
				list.add(name);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
		
	}
	
	/**
	 * @param target
	 *            匹配 数据 { data } 替换 数据
	 * @param data
	 * @return
	 */
	public static String regexData(String target, String data, String key) {

		Pattern pattern = Pattern.compile("(\\{" + key + "}\\");
		Matcher m = pattern.matcher(target);
		StringBuffer sbr = new StringBuffer();
		while (m.find()) { // 查找匹配
			m.appendReplacement(sbr, data);
		}
		m.appendTail(sbr);
		return sbr.toString();

	}

	public static String regexData(String target, Map<String, String> data) {
		Set<Map.Entry<String, String>> set = data.entrySet();
		for (Entry<String, String> entry : set) {
			String key = entry.getKey();
			String value = entry.getValue();
			target = regexData(target, value, key);
		}
		return target;
	}

}
