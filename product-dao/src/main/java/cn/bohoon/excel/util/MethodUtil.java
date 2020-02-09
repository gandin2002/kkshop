package cn.bohoon.excel.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodUtil {

	/**
	 * 通过反射获得参数名
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static List<String> getObjectName(Object object) throws Exception {
		if (object != null) {
			
			Class<?> clz = object.getClass();
			Field[] fields = clz.getDeclaredFields();
			
			List<String> list = new ArrayList<>();
			for (Field field : fields) {
				list.add(field.getName());
			}
			return list;
		}
		return null;
	}
	
	/**
	 * 注解方式
	 * @param object
	 * @return
	 */
	public static List<String> getAnnotationName(Object object){
		Field[] fields = object.getClass().getDeclaredFields();
        List<String> listName = new ArrayList<>();
        for (Field field : fields)  //字段
        {
        	if(field.isAnnotationPresent(ExportConfig.class)){
        		ExportConfig exportConfig = field.getAnnotation(ExportConfig.class);
        		listName.add(exportConfig.name()); //获取注解name
        	}
        
        }
        Method[]  methods  =  object.getClass().getMethods(); //方法
        for (Method method : methods) {
			if(method.isAnnotationPresent(ExportConfig.class)){
				ExportConfig exportConfig = method.getAnnotation(ExportConfig.class);
				listName.add(exportConfig.name());//获取注解name
			}
        	
		}
        
        return listName;
	}
	
	
	/**
	 * 通过反射调用get方法
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static List<Object> getObjectInvoke(Object object) throws Exception {
			List<String> mlist = getObjectName(object);
			List<Object> list = new ArrayList<>();
			for (String string : mlist) {
				String MethodName="get"+getMethodName(string);
				Method method=object.getClass().getMethod(MethodName);
				Object  val =method.invoke(object);
				list.add(val);
			}
			return list;
	}
	/**
	 * 注解方式
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static List<Object> getAnnotationInvoke(Object object) throws Exception{
		Field[] fields = object.getClass().getDeclaredFields();
		List<Object> list = new ArrayList<>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ExportConfig.class)) {

				String MethodName = "get" + MethodUtil.getMethodName(field.getName());
				Method method = object.getClass().getMethod(MethodName);
				list.add(method.invoke(object));

			}
		}
		
		Method[]  methods  =  object.getClass().getMethods(); //方法
		for (Method method : methods) {
			if(method.isAnnotationPresent(ExportConfig.class)){
				list.add(method.invoke(object));
			}
		}
		
		return list;
	}
	
	public static String getMethodName(String fildeName) throws Exception {
		 fildeName =  fildeName.substring(0, 1).toUpperCase() +  fildeName.substring(1);
		return fildeName;
	}
}
