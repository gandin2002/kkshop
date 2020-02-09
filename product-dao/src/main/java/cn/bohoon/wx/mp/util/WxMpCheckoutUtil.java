package cn.bohoon.wx.mp.util;

import com.wxpay.sign.Sha1Util;

public class WxMpCheckoutUtil {
	
	 public static boolean checkSignature(String signature, String timestamp, String nonce,String token) {
		 String[] arr = new String[] {token,timestamp,nonce};
		 sort(arr);
		 StringBuilder content = new StringBuilder();
		 for (int i = 0; i < arr.length; i++) {
			 content.append(arr[i]);
		}
		 String tmpStr= Sha1Util.SHA1(content.toString());
		 return tmpStr != null ? tmpStr.equalsIgnoreCase(signature) : false;
	 }
 
	 //字典排序得到 String[]
	public static void sort(String a[]) {
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = i + 1; j < a.length; j++) {
				if (a[j].compareTo(a[i]) < 0) {
					String temp = a[i];
					a[i] = a[j];
					a[j] = temp;
				}
			}
		}
	}

}
