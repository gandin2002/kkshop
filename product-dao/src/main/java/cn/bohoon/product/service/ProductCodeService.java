package cn.bohoon.product.service;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alipay.sign.Base64;
import com.google.zxing.WriterException;
import com.qrcode.GenerateZxing;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.helper.SysParamHelper;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.product.dao.ProductCodeDao;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductCode;

@Service
@Transactional
public class ProductCodeService extends BaseService<ProductCode,Integer>{

	@Autowired
	ProductCodeDao productCodeDao;
	
	@Autowired
	SysParamService sysParamService;

	@Autowired
     ProductCodeService(ProductCodeDao productCodeDao) {
		super(productCodeDao);
	}
	public String getWebUrl(){
		SysParam sp = sysParamService.findParam(SysParamHelper.MOBILE_SITE, SysParamType.PLATFORM_PARAM);
		if(!StringUtils.isEmpty(sp.getValue())){
			return sp.getValue();
		}
		return "https://wx.bohoon.cn/";
	}
	
	public void createQrCode(Integer productId) throws WriterException, IOException{
		  ProductCode skuCode = this.select(" from ProductCode where productId = ? ", productId);
		  if(skuCode == null){
			  skuCode = new ProductCode();
			  skuCode.setProductId(productId);
			  
			  String qrcode = GenerateZxing.qrEnCodeBase64(getWebUrl()+"product/"+productId);
			  skuCode.setQrCode(qrcode);
			  this.save(skuCode);
		  } 
	 }
	 
	public void writeZip(ZipOutputStream zos, Product product) throws Exception{
		  this.createQrCode(product.getId());
		  ProductCode  productCode = this.select(" from ProductCode where productId = ?  ", product.getId());
		  String qrB64 = productCode.getQrCode();
		  qrB64 = qrB64.substring(23,qrB64.length());
		  byte[] date = Base64.decode(qrB64);
		  zos.putNextEntry(new ZipEntry(product.getName()+File.separator+product.getCode()+".jpg"));
		  zos.write(date);
	}
}
