package cn.bohoon.wx.mp.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 微信公众通知解析
 * @author HJ
 * 2018年3月8日,下午6:05:07
 */
public class WxMpNotifyParseUtil {

	@SuppressWarnings("unchecked")
	public static Map<Object, String> parseXml()   {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		
		// 将解析结果存储在HashMap中
		Map<Object, String> map = new HashMap<Object, String>();
		try {
			// 从request中取得输入流
			InputStream inputStream = request.getInputStream();
			// 读取输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到xml根元素
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			
			List<Element> elementList = root.elements();

			// 遍历所有子节点
			for (Element e : elementList) {
				System.out.println("eeeeeeeeee==============="+e);
				map.put(e.getName(), e.getText());
			}

			// 释放资源
			inputStream.close();
			inputStream = null;
		} catch (Exception e ) {
			System.out.println("异常=======================e="+e.getMessage());
			e.printStackTrace();
		}

		return map;
	}
	
	/**
	 * 发送普通文本
	 * @param fromUserName
	 * @param toUserName
	 * @param createTime
	 * @param Content
	 * @return
	 */
	public static String setXML(String fromUserName, String toUserName, String createTime, String Content) {
		
		String MessageXml = "<xml>";
        MessageXml += "<ToUserName><![CDATA["+fromUserName+"]]></ToUserName>";
        MessageXml += "<FromUserName><![CDATA["+toUserName+"]]></FromUserName>";
        MessageXml += "<CreateTime>"+createTime+"</CreateTime>";
        MessageXml += "<MsgType><![CDATA[text]]></MsgType>";   
        MessageXml += "<Content><![CDATA["+Content+"]]></Content>";
        MessageXml += "</xml>";
	
        return MessageXml;
	}
}
