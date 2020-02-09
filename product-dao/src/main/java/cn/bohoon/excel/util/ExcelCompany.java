package cn.bohoon.excel.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import cn.bohoon.company.helper.CompanyExcelModel;

public class ExcelCompany {
	
	
 public static List<CompanyExcelModel> getExcel(InputStream file) throws Exception {
		
		
		List<List<String>> list = ExcelRead.readExcel(file);
		List<CompanyExcelModel> companyList = new ArrayList<>();
		
		for(List<String> li : list){
			
			String companyName = li.get(0).trim();
			String phone = li.get(1).trim();
			
			if (StringUtils.isEmpty(companyName)){
				continue;
			}
			
			
			if (StringUtils.isEmpty( phone.trim() )){
				continue;
			}
			
			
			if (phone.length() != 11){
				
				continue;
			}
			
			String regex = "^1[0-9]\\d{9}$";
			
			boolean flag = phone.matches(regex);
			
			if (flag){
				CompanyExcelModel companyExcelModel = new CompanyExcelModel();
				
				companyExcelModel.setName(companyName);
				companyExcelModel.setPhone(phone);
				companyExcelModel.setRealName(li.get(2));
				
				companyList.add(companyExcelModel);
			}
			
		}
		
		
		
		return companyList;
		
 }
		

}
