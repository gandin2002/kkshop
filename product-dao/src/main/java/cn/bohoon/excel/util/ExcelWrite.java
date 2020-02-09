package cn.bohoon.excel.util;

import java.io.OutputStream;
import java.util.List;

public class ExcelWrite {
	/**
	 * 通过注解 写出Excel
	 * @param path
	 * @param object 
	 * @param list
	 * @throws Exception
	 */
	public static <X> void writeExcel(OutputStream outputStream,List<X> list) throws Exception {
		writeExcel(outputStream, MethodUtil.getAnnotationName(list.get(0)), list);
	}
	
	/**
	 * 写出Excel
	 * @param path
	 * @param listName
	 * @param list
	 * @throws Exception
	 */
	public static <X> void writeExcel(OutputStream outputStream, List<String> listName, List<X> list) throws Exception {
		ExcelUtils.createExcelFile(outputStream, "Sheet1");
		
		for (int i = 0; i < listName.size(); i++) { // 设置Excel 标题
			ExcelUtils.setCellData(listName.get(i), 0, i);
		}
		int i = 1;
		for (Object Object : list) {
			List<Object> Vallist = MethodUtil.getAnnotationInvoke(Object);
			for (int j = 0; j < Vallist.size(); j++) {
				if (Vallist.get(j) != null)
					ExcelUtils.setCellData(Vallist.get(j).toString(), i, j);
				continue;
			}
			i++;
		}
		  ExcelUtils.closeExcel();
	}
}
