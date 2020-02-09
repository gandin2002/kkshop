package cn.bohoon.util;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityHtmlUtils {
	
	/**
	 * Velocity 读取字符串 模板生成代码
	 * 2017年11月8日15:02:13
	 * @param str  
	 * @param context
	 * @return 
	 */
	public static  String vm(String str,VelocityContext context){
		Properties p = new Properties();
		p.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
		p.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.RESOURCE_LOADER, "class"); 
		p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");
		VelocityEngine ve = new VelocityEngine();
		ve.init(p);
		
	    StringWriter stringWriter = new StringWriter();
	    ve.evaluate(context, stringWriter, "mystring", str);
	    return stringWriter.toString();
	}
	
 
 

}
