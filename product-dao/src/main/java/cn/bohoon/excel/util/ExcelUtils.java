package cn.bohoon.excel.util;


import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * excel 文件处理
 * @author HJ
 * 2017年12月21日,上午10:53:42
 */
public class ExcelUtils {

	private static XSSFSheet excelWSheet;
	private static XSSFWorkbook excelWBook;
	private static XSSFCell cell;
	private static XSSFRow row;
	private static OutputStream excelFileOutputStream; //输出

	// 创建一个Excel文件
	public static void createExcelFile(OutputStream path,String sheetName){
		excelFileOutputStream = path;
		try {
			excelWBook = new XSSFWorkbook();
			excelWSheet = excelWBook.createSheet(sheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// 写入数据
	public static void setCellData(String value, int rowNum, int colNum) {
		try {
			row = excelWSheet.getRow(rowNum);
			if (row == null) {
				row = excelWSheet.createRow(rowNum);
			}
			cell = row.getCell(colNum);

			if (cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(value);
			} else {
				cell.setCellValue(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeExcel() throws IOException{
		excelWBook.write(excelFileOutputStream);
		excelWBook.close();
	}

	public static int getUsedRowsCount() {
		try {
			int rowCount = excelWSheet.getPhysicalNumberOfRows();
			return rowCount;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
