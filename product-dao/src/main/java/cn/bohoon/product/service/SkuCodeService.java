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
import cn.bohoon.product.dao.SkuCodeDao;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuCode;

@Service
@Transactional
public class SkuCodeService extends BaseService<SkuCode,Integer>{
 
	@Autowired
	SkuCodeDao skuCodeDao;
	
	@Autowired
	SysParamService sysParamService;
	
	@Autowired
    SkuCodeService(SkuCodeDao skuCodeDao) {
		super(skuCodeDao);
	}
	
	public String getWebUrl(){
		SysParam sp = sysParamService.findParam(SysParamHelper.MOBILE_SITE, SysParamType.PLATFORM_PARAM);
		if(!StringUtils.isEmpty(sp.getValue())){
			return sp.getValue();
		}
		return "https://wx.bohoon.cn/";
	}
	
	public void createQrCode(Integer skuId) throws WriterException, IOException{
		  SkuCode skuCode = this.select(" from SkuCode where skuId = ? ", skuId);
		  if(skuCode == null){
			  skuCode = new SkuCode();
			  skuCode.setSkuId(skuId);
			  String qrcode = GenerateZxing.qrEnCodeBase64(getWebUrl()+"sku/"+skuId);
			  skuCode.setQrcode(qrcode);
			  this.save(skuCode);
		  } 
	 }
	
	public void writeZip(ZipOutputStream zos, Sku sku) throws Exception{
		  this.createQrCode(sku.getId());
		  SkuCode  skuCode = this.select(" from SkuCode where skuId = ?  ", sku.getId());
		  String qrB64 = skuCode.getQrcode();
		  qrB64 = qrB64.substring(23,qrB64.length());
		  byte[] date = Base64.decode(qrB64);
		  zos.putNextEntry(new ZipEntry(sku.getName()+File.separator+sku.getCode()+".jpg"));
		  zos.write(date);
	}
	
	
}
