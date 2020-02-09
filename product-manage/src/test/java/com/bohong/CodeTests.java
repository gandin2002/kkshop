 package com.bohong;


import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import cn.bohoon.Application;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.syn.dao.SynUserInfoDao;
import cn.bohoon.syn.entity.SynUserInfo;
import cn.bohoon.userInfo.domain.UserSex;
import cn.bohoon.userInfo.entity.User;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.wx.mp.service.WxMsgTemplatService;
import cn.bohoon.wx.mp.service.WxMsgTemplateItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class CodeTests {

	@Autowired
	DataSource dataSource;
	
 
	
//	@Autowired
//	WxPmRequests wxPmRequests;
//
	
	@Autowired
	WxMsgTemplatService wxMsgTemplatService;
	
	@Autowired
	WxMsgTemplateItemService wxMsgTemplateItemService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SynUserInfoDao userInfoDao;
	
	@Autowired
	CompanyService companyService;
	
	
	Logger logger = LoggerFactory.getLogger(this.getClass()) ;
	
	@Test
	public void copyCode() throws Exception{
		
		List<UserInfo> list= userInfoService.list();
		for (UserInfo userInfo : list) {
			userInfoService.deleteAll(userInfo.getId());
		}
		
	}
//		List<SynUserInfo> list= userInfoDao.findAll();
//		for (SynUserInfo synUserInfo : list) {
//			String mobile=synUserInfo.getMobile();
//			if(!StringUtils.isEmpty(mobile)){
//				
//				String companyId = "";
//				
//				UserInfo userInfo = userInfoService.select(" from UserInfo where phone = ?  ", mobile);
//				if(userInfo == null){
//					userInfo = new UserInfo();
//					UserSex sex = synUserInfo.getSex().equals("0") ? UserSex.MAN:UserSex.WOMAN;
//					
//					String email=synUserInfo.geteMail();
//					companyId = synUserInfo.getBizPartnerId();
//					
//					userInfo.setSex(sex);
//					userInfo.setPhone(mobile);
//					userInfo.setRealname(synUserInfo.getLinkMan());
//					userInfo.setEmail(email);
//					userInfo.setCompanyId(companyId);
//					userInfo.setNickname(synUserInfo.getLinkMan());
//					
//					User user = new User();
//					user.setEmail(email);
//					user.setMobile(mobile);
//					
//					userInfoService.addAccount(userInfo, user);
//					
//				}else{
//					
//					UserSex sex = synUserInfo.getSex().equals("0") ? UserSex.MAN:UserSex.WOMAN;
//					
//					String email=synUserInfo.geteMail();
//					companyId = synUserInfo.getBizPartnerId();
//					
//					userInfo.setNickname(synUserInfo.getLinkMan());
//					userInfo.setSex(sex);
//					userInfo.setRealname(synUserInfo.getLinkMan());
//					userInfo.setEmail(email);
//					userInfo.setCompanyId(companyId);
//					userInfoService.save(userInfo);
//					
//				}  
//				
//				Boolean blea = new Boolean(synUserInfo.getIsMain());
//				if(blea){ //如果为主要公司主要联系人
//					 Company company = companyService.get(companyId);
//					 if(company == null){
//						 continue;
//					 }
//					 company.setUserId(userInfo.getId());
//					 companyService.save(company);
//				}
//			}
//			
//		}
//		
//	}
	

	}
//	@Test
//	public void testGetExcel() throws Exception {
//		
//		String path = getClass().getClassLoader().getResource("").getPath()+"roeld2.xlsx";
//		System.out.println(path);
//		List<Quotation> quotationList = quotationService.list();
//		List<ExcelQuotation> excelQuotationList = new ArrayList<>();
//		for (Quotation quotation: quotationList) {
//			List<QuotationItem> quotationItemList = quotationItemService.list(" from QuotationItem where  quotationId = ? ",quotation.getId());
//			for (QuotationItem quotationItem : quotationItemList) {
//	        	Sku sku = skuService.get(quotationItem.getSkuId());
//	        	Product product = productService.get(sku.getProductId());
//	        	ExcelQuotation excelQuotation = new ExcelQuotation();
//	        	excelQuotation.setProductName(product.getName()+"("+sku.getAttrAndAttrValues()+")");
//	        	excelQuotation.setQuantity(quotationItem.getQuantity().toString());
//	        	excelQuotation.setQuotationId(quotationItem.getQuotationId());
//	        	excelQuotation.setQuotationSkuPrice(quotationItem.getQuotationSkuPrice().toString());
//	        	excelQuotation.setQuotationPrice(quotationItem.getQuotationPrice().toString());
//	        	excelQuotation.setSkuId(sku.getCode());
//	        	excelQuotationList.add(excelQuotation);
//			}
//			
//		}
//		FileOutputStream fileOutputStream = new FileOutputStream(path);
// 		ExcelRead.writeExcel(fileOutputStream, excelQuotationList);
// 		fileOutputStream.close();
//	}
	
 

//}