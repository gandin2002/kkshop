package cn.bohoon.excel.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class  ExcelRead  {
	
	/**
	 * 读Excel 
	 * @param InputStream 
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> readExcel(InputStream InputStream) throws Exception {

		
		Workbook workBook = WorkbookFactory.create(InputStream);

		int sheets = workBook.getNumberOfSheets(); // 总共多少页

		List<List<String>> listRows = new ArrayList<List<String>>();
		for (int i = 0; i < sheets; i++) { // 页
			Sheet sheet = workBook.getSheetAt(i);
			if (sheet == null)
				continue;
			for (int k = 1; k < sheet.getPhysicalNumberOfRows(); k++) { // 行
				Row row = sheet.getRow(k);
				if (row == null)
					continue;
				List<String> listCells = new ArrayList<String>();
				for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) { // 单元格
					listCells.add(formatCell(sheet.getRow(k).getCell(j)));
				}
				listRows.add(listCells);
			}
		}
		InputStream.close();
		workBook.close();
		return listRows;
	}
	
	
	// 格式化
		public static String formatCell(Cell hssfCell) {
			
			if (hssfCell == null) {
				return "";
			}else {
				if (hssfCell.getCellType() == Cell.CELL_TYPE_BLANK) {
					return String.valueOf(hssfCell.getBooleanCellValue());
				}else if(hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					
					double db = hssfCell.getNumericCellValue();
					
					
					return String.valueOf((long)db);
				}else {
					return String.valueOf(hssfCell.getStringCellValue());
				}
			}
			
		}
	
	/**
	 * 基于反射和注解的Excel解析
	 * @param <X>
	 * @param InputStream 输入流
	 * @param clazz 注解
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 */
	public static <X> List<X> fastReadExcel(InputStream InputStream,Class<X> clazz) throws Exception{
		Workbook workbook = WorkbookFactory.create(InputStream);
		int sheets = workbook.getNumberOfSheets(); // 总共多少页
		
		List<X> list = new ArrayList<>();
		if(sheets <= 0){
			return list;
		}
		for (int i = 0; i < sheets; i++){
			Sheet sheet = workbook.getSheetAt(i); //得到页
			if (sheet == null){
				continue;
			}
		  int firstRowNum = sheet.getFirstRowNum();	 
		  Row row = sheet.getRow(firstRowNum); //得到表格的第一行
		  if(row == null){
			  continue;
		  }
		  short lastCellNum =row.getLastCellNum(); //得到第一行n格
		  
		  Map<String, Integer> cellNames = getCellMapping(row,lastCellNum); //得到第一行的key 为名字，val 为位置
		  
		  Map<String, Field> annotations  = getFeildMapping(clazz); //得到key 为名字 , val 映射字段
		  
		  int lastRowNum = sheet.getLastRowNum(); //拿取总共行数
		  Set<String> keys = cellNames.keySet();//拿取key 集合
		  
          for (int rowIndex = (++firstRowNum); rowIndex <= lastRowNum; rowIndex++) {
              X inst = clazz.newInstance(); //创建一个以这个为代表的类的新实例
              Row r = sheet.getRow(rowIndex); 
              for (String key : keys) {
                  Field field = annotations.get(key);
                  if (field == null)
                      continue;
                  Integer col = cellNames.get(key);
                  Cell cel = r.getCell(col);
                  if (cel == null)
                      continue;
                  field.setAccessible(true);//设置在使用构造器的时候不执行权限检查  
                  String val = cel.getStringCellValue();
                  field.set(inst, val); //指定的对象参数 设置新的值
              }
              list.add(inst);
          }
		  
		}
		return list;
	}
	
	private static Map<String,Integer> getCellMapping(Row row, short lastCellNum){
		Map<String,Integer> cellNames = new HashMap<>();
		Cell cell;
		for (int i = 0; i < lastCellNum; i++) {
			cell = row.getCell(i); 
			String val = cell.getStringCellValue();
			cellNames.put(val, i); 
		}
		return cellNames;
	}
	
	private static <X> Map<String,Field> getFeildMapping(Class<X> clazz){
		Map<String,Field> annotations = new HashMap<>();
		Field[] fields = clazz.getDeclaredFields();
		if(fields == null || fields.length <1){
			return annotations;
		}
		for (Field field : fields) {
			ExportConfig exportConfig = field.getAnnotation(ExportConfig.class);
			if(exportConfig == null){
				annotations.put(field.getName(), field);
			}else {
                annotations.put(exportConfig.name(),field);
            }
		
		}
		return annotations;
	}
}
