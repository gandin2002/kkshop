package cn.bohoon.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {

	public static boolean isMobile(String mobile) {
		String regExp = "^1[0-9]{10}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobile);
		return m.find();// boolean
	}
}
