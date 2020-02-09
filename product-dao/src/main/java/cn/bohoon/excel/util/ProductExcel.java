package cn.bohoon.excel.util;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.product.domain.ProdExcelModel;
import cn.bohoon.product.domain.ProdExcelSkuModel;
import cn.bohoon.product.domain.SkuInventoryInputExcelMode;
import cn.bohoon.product.entity.AttrValue;
import cn.bohoon.product.entity.Brand;
import cn.bohoon.product.entity.Category;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuAttr;
import cn.bohoon.product.entity.SkuWare;
import cn.bohoon.product.entity.TaxCode;
import cn.bohoon.product.entity.Unit;
import cn.bohoon.product.service.AttrValueService;
import cn.bohoon.product.service.BrandService;
import cn.bohoon.product.service.CategoryService;
import cn.bohoon.product.service.ProductService;
import cn.bohoon.product.service.SkuAttrService;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.SkuWareService;
import cn.bohoon.product.service.TaxCodeService;
import cn.bohoon.product.service.UnitService;
import cn.bohoon.stock.entity.WareHouse;
import cn.bohoon.stock.service.WareHouseService;
import cn.bohoon.util.IDUtil;

public class ProductExcel {

	Logger logger = LoggerFactory.getLogger(ProductExcel.class) ;
	public static Map<String,List<ProdExcelModel>> importData(List<ProdExcelModel> result) throws CheckException {
		
		Map<String,List<ProdExcelModel>> map = new HashMap<String,List<ProdExcelModel>>();
		
		map.put("ptSucceedArray", new ArrayList<ProdExcelModel>()); //成功 
		map.put("ptErrorArray", new ArrayList<ProdExcelModel>()); //失败
		map.put("ptRepetitionArray", new ArrayList<ProdExcelModel>()); //重复
		
		ProductService proService = SpringContextHolder.getBean(ProductService.class) ;
		CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class) ;
		BrandService brandService = SpringContextHolder.getBean(BrandService.class) ;
		UnitService unitService = SpringContextHolder.getBean(UnitService.class) ;
		AttrValueService attrValueService = SpringContextHolder.getBean(AttrValueService.class) ;
		SkuService skuService = SpringContextHolder.getBean(SkuService.class) ; 
		SkuAttrService skuAttrService = SpringContextHolder.getBean(SkuAttrService.class) ;
		TaxCodeService taxCodeService = SpringContextHolder.getBean(TaxCodeService.class);
		
		
		for(ProdExcelModel pm : result ) {
			if(StringUtils.isEmpty(pm.getCode())||StringUtils.isEmpty(pm.getName())||StringUtils.isEmpty(pm.getBrand())||StringUtils.isEmpty(pm.getUnitName())||StringUtils.isEmpty(pm.getCategory())||StringUtils.isEmpty(pm.getProAttr())){ //如果Code is null 视为错误
				pm.setMark("关键字段为空！");
				map.get("ptErrorArray").add(pm);
				continue;
			}
 
			Product pro = proService.select(" from Product where code=?", pm.getCode()) ;
			Unit unit = unitService.select(" from Unit where name=?", pm.getUnitName()) ;
			Category category = categoryService.select(" from Category where name=?",  pm.getCategory()) ;
			if(category == null){
				pm.setMark("无该分类！");
				map.get("ptErrorArray").add(pm);
				continue;
			}
			Brand brand = brandService.select(" from Brand where name=?", pm.getBrand()) ;
			if(null == brand ) {
				pm.setMark("无该品牌！");
				map.get("ptErrorArray").add(pm);
				continue ;
			}
			Integer productId = 0 ;
			if(null == unit) {
				unit = new Unit() ;
				unit.setName(pm.getUnitName());
				unit.setCode(IDUtil.randString(5));
				unitService.save(unit);
			}
			if(null == pro) { //存Prdouct
				Product product = new Product() ;
				product.setCode(pm.getCode());
				product.setName(pm.getName());
				product.setCategoryId(category.getId());
				product.setBrandId(null != brand ?brand.getId():null);
				product.setUnitId(null != unit ?unit.getId():null);
				product.setSalesPrice(new BigDecimal(pm.getSkuPrice()));
				product.setFlag(false);
				map.get("ptSucceedArray").add(pm);//成功
				proService.save(product);
				productId = product.getId() ;
			} else {
				map.get("ptRepetitionArray").add(pm);//重复
				productId = pro.getId() ;
				continue ;
			}
			Sku sku = new Sku(); //存SKu
			sku.setProductId(productId);
			sku.setSkuPrice(new BigDecimal(pm.getSkuPrice()));
			sku.setInventory(parseInteger(pm.getInventorys()));
			sku.setStatus(true);
			sku.setFlag(false);
			sku.setCode(IDUtil.getInstance().getId("SK"));
			sku.setName(pm.getName()+sku.getCode());
			sku.setErpCode(sku.getCode());
			pm.setSkuCode(sku.getCode());
			
			TaxCode taxCode = taxCodeService.findDefaultTax(); //默认税码
			if(taxCode != null){
	   			sku.setTax(taxCode.getTax());
    			sku.setTaxId(taxCode.getId());
			}
			
			
			
			String attrs = pm.getProAttr() ;
			List<String> atvs = Arrays.asList(attrs.split(",")) ;
			List<Integer> attrIds = new ArrayList<Integer>() ;
			List<Integer> attrValueIds = new ArrayList<Integer>() ;
			String attrValues = "" ;
			for(String atv : atvs) {
				AttrValue attrValue = attrValueService.select(" from AttrValue where name=?", atv) ;
				if(null != attrValue ) {
					attrIds.add(attrValue.getAttrId()) ;
					attrValueIds.add(attrValue.getId()) ;
					attrValues = attrValues+attrValue.getId()+"," ;
				}
			}
			if(attrValues.endsWith(",")) {
				attrValues = attrValues.substring(0, attrValues.length()-1) ;
			}
			sku.setAttrValues(attrValues);
			skuService.save(sku);
			
			for(int i=0;i<attrIds.size() ;i++ ) {
				SkuAttr ska = new SkuAttr() ;
				Integer attrId =  attrIds.get(i) ;
				Integer attrValueId = attrValueIds.get(i) ;
				ska.setAttrId(attrId);
				ska.setAttrValueId(attrValueId);
				ska.setSkuId(sku.getId());
				skuAttrService.save(ska);
			}
			
		}
		return map;
	}
	
	public static List<ProdExcelModel> getExcel(InputStream file) throws CheckException {
		List<List<String>> list= new ArrayList<List<String>>() ;
		List<ProdExcelModel> result = new ArrayList<ProdExcelModel>() ;
		try {
			list = ExcelRead.readExcel(file);
		} catch (Exception e) {
			
			throw new CheckException("解析EXCEL 文件异常！") ;
		}
		
		for(List<String> datas : list) {
			
			if(datas.size() == 11 ) {
				boolean state = true;
				for (int i = 1; i < 11; i++) {
					if(!StringUtils.isEmpty(datas.get(i))){
						state = false;
					}
				}
				if(state){
					 continue;
				}
				
				ProdExcelModel pm = new ProdExcelModel() ;
				pm.setCode(datas.get(1));
				pm.setName(datas.get(2));
				pm.setCategory(datas.get(3));
				pm.setBrand(datas.get(4));
				pm.setProAttr(datas.get(5));
				pm.setUnitName(datas.get(6));
				pm.setInventorys(datas.get(7));
				pm.setSkuPrice(datas.get(8));
				pm.setScore(datas.get(9));
				pm.setMark(datas.get(10));
				result.add(pm) ;
			}
		}
		return result ;
	}
	//全新货品导入
	public static Map<String,List<ProdExcelSkuModel>> NewimportData(List<ProdExcelSkuModel> result) throws CheckException {
		
		Map<String,List<ProdExcelSkuModel>> map = new HashMap<String,List<ProdExcelSkuModel>>();
		
		map.put("ptSucceedArray", new ArrayList<ProdExcelSkuModel>()); //成功 
		map.put("ptErrorArray", new ArrayList<ProdExcelSkuModel>()); //失败
		map.put("ptRepetitionArray", new ArrayList<ProdExcelSkuModel>()); //重复
		
		ProductService proService = SpringContextHolder.getBean(ProductService.class) ;
		CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class) ;
		BrandService brandService = SpringContextHolder.getBean(BrandService.class) ;
		UnitService unitService = SpringContextHolder.getBean(UnitService.class) ;
		AttrValueService attrValueService = SpringContextHolder.getBean(AttrValueService.class) ;
		SkuService skuService = SpringContextHolder.getBean(SkuService.class) ; 
		SkuAttrService skuAttrService = SpringContextHolder.getBean(SkuAttrService.class) ;
		TaxCodeService taxCodeService = SpringContextHolder.getBean(TaxCodeService.class);
		
		
		for(ProdExcelSkuModel pm : result ) {
			if(StringUtils.isEmpty(pm.getCode())||StringUtils.isEmpty(pm.getName())||StringUtils.isEmpty(pm.getBrandName())||StringUtils.isEmpty(pm.getUnitName())||StringUtils.isEmpty(pm.getCategoryName())||StringUtils.isEmpty(pm.getAttrValues())){ //如果Code is null 视为错误
				pm.setMark("关键字段为空！");
				map.get("ptErrorArray").add(pm);
				continue;
			}
 
			Product pro = proService.select(" from Product where code=?", pm.getCode()) ;
			Unit unit = unitService.select(" from Unit where name=?", pm.getUnitName()) ;
			Category category = categoryService.select(" from Category where name=?",  pm.getCategoryName()) ;
			if(category == null){
				pm.setMark("无该分类！");
				map.get("ptErrorArray").add(pm);
				continue;
			}
			Brand brand = brandService.select(" from Brand where name=?", pm.getBrandName()) ;
			if(null == brand ) {
				pm.setMark("无该品牌！");
				map.get("ptErrorArray").add(pm);
				continue ;
			}
			Integer productId = 0 ;
			if(null == unit) {
				unit = new Unit() ;
				unit.setName(pm.getUnitName());
				unit.setCode(IDUtil.randString(5));
				unitService.save(unit);
			}
			if(null == pro) { //存Prdouct
				Product product = new Product() ;
				product.setCode(pm.getCode());
				product.setName(pm.getName());
				product.setCategoryId(category.getId());
				product.setBrandId(null != brand ?brand.getId():null);
				product.setUnitId(null != unit ?unit.getId():null);
				product.setSalesPrice(new BigDecimal(pm.getSalesPrice()));
				BigDecimal bigDecimal = new BigDecimal(pm.getStartNum());
				product.setStartNum(bigDecimal.intValue());
				product.setInventoryHint(parseInteger(pm.getInventoryHint()));
				product.setDisplayPrice(new BigDecimal(pm.getDisplayPrice()));
				product.setVolume(new BigDecimal(pm.getVolume()));
				product.setWeight(new BigDecimal(pm.getWeight()));
				product.setForShort(pm.getForShort());
				product.setFlag(false);
				map.get("ptSucceedArray").add(pm);//成功
				proService.save(product);
				productId = product.getId() ;
			} else {
				map.get("ptRepetitionArray").add(pm);//重复
				productId = pro.getId() ;
				continue ;
			}
			Sku sku = new Sku(); //存SKu
			sku.setProductId(productId);//货品ID
			sku.setSkuPrice(new BigDecimal(pm.getSalesPrice()));//货品单价
			sku.setStatus(true);
			sku.setFlag(false);
			sku.setCode(IDUtil.getInstance().getId("SK"));
			sku.setName(pm.getName()+sku.getCode());
			sku.setErpCode(sku.getCode());
			
			
			pm.setSkuCode(sku.getCode());
			
			TaxCode taxCode = taxCodeService.findDefaultTax(); //默认税码
			if(taxCode != null){
	   			sku.setTax(taxCode.getTax());
    			sku.setTaxId(taxCode.getId());
			}
			
			
			
			String attrs = pm.getAttrValues() ;
			List<String> atvs = Arrays.asList(attrs.split(",")) ;
			List<Integer> attrIds = new ArrayList<Integer>() ;
			List<Integer> attrValueIds = new ArrayList<Integer>() ;
			String attrValues = "" ;
			for(String atv : atvs) {
				AttrValue attrValue = attrValueService.select(" from AttrValue where name=?", atv) ;
				if(null != attrValue ) {
					attrIds.add(attrValue.getAttrId()) ;
					attrValueIds.add(attrValue.getId()) ;
					attrValues = attrValues+attrValue.getId()+"," ;
				}
			}
			if(attrValues.endsWith(",")) {
				attrValues = attrValues.substring(0, attrValues.length()-1) ;
			}
			sku.setAttrValues(attrValues);
			sku.setVolume( new BigDecimal( pm.getVolume()));
			sku.setWeight( new BigDecimal( pm.getWeight()));
			sku.setScore(parseInteger(pm.getScore()));
			sku.setTranslateRate(parseInteger(pm.getTranslateRate()));
			sku.setBarCode(pm.getBarCode());
			Unit unit2=unitService.select("from Unit where name=? ", pm.getAuxUnitName());
			if (!StringUtils.isEmpty(unit2)) {
				sku.setAuxUnitName(unit2.getName());
				sku.setAuxUnitId(unit2.getId());
			}else{
				unit = new Unit() ;
				unit.setName(pm.getAuxUnitName());
				unit.setCode(IDUtil.randString(5));
				unitService.save(unit);
			}
		
			
			
			skuService.save(sku);
			
			for(int i=0;i<attrIds.size() ;i++ ) {
				SkuAttr ska = new SkuAttr() ;
				Integer attrId =  attrIds.get(i) ;
				Integer attrValueId = attrValueIds.get(i) ;
				ska.setAttrId(attrId);
				ska.setAttrValueId(attrValueId);
				ska.setSkuId(sku.getId());
				skuAttrService.save(ska);
			}
			
		}
		return map;
	}
	
	public static List<ProdExcelSkuModel> getNewExcel(InputStream file) throws CheckException {
		List<List<String>> list= new ArrayList<List<String>>() ;
		List<ProdExcelSkuModel> result = new ArrayList<ProdExcelSkuModel>() ;
		try {
			list = ExcelRead.readExcel(file);
		} catch (Exception e) {
			
			throw new CheckException("解析EXCEL 文件异常！") ;
		}
		
		for(List<String> datas : list) {
			
			if(datas.size() == 18 ) {
				ProdExcelSkuModel pm = new ProdExcelSkuModel() ;
				pm.setCode(datas.get(1));
				pm.setName(datas.get(2));
				pm.setCategoryName(datas.get(3));
				pm.setBrandName(datas.get(4));
				pm.setUnitName(datas.get(5));
				pm.setStartNum(datas.get(6));
				pm.setInventoryHint(datas.get(7));
				pm.setDisplayPrice(datas.get(8));
				pm.setSalesPrice(datas.get(9));
				pm.setVolume(datas.get(10));
				pm.setWeight(datas.get(11));
				pm.setAttrValues(datas.get(12));
				pm.setScore(datas.get(13));
				pm.setTranslateRate(datas.get(14));
				pm.setAuxUnitName(datas.get(15));
				pm.setBarCode(datas.get(16));
				pm.setForShort(datas.get(17));
				result.add(pm) ;
			}
		}
		return result ;
	}
	
	private static Integer parseInteger(String value) {
		Integer result = 0 ;
		try {
			if(value.contains(".")) {
				value = value.substring(0,value.indexOf(".")) ;
			}
			result = Integer.valueOf(value) ;
		} catch(Exception e) {
		}
		return result ;
	}
	
	
	//库存导入
	public static Map<String,List<SkuInventoryInputExcelMode>> NewimportDataWarehouse(List<SkuInventoryInputExcelMode> result) throws CheckException {
		
		Map<String,List<SkuInventoryInputExcelMode>> map = new HashMap<String,List<SkuInventoryInputExcelMode>>();
		
		map.put("ptSucceedArray", new ArrayList<SkuInventoryInputExcelMode>()); //成功 
		map.put("ptErrorArray", new ArrayList<SkuInventoryInputExcelMode>()); //失败
		
		SkuService skuService=SpringContextHolder.getBean(SkuService.class);
		WareHouseService wareHouseService=SpringContextHolder.getBean(WareHouseService.class);
		SkuWareService skuWareService=SpringContextHolder.getBean(SkuWareService.class);
		
		
		
		for(SkuInventoryInputExcelMode pm : result ) {
			
			if(StringUtils.isEmpty(pm.getSkuCode())||StringUtils.isEmpty(pm.getWarehouseId())){ //如果Code is null 视为错误
				pm.setMark("关键字段为空！");
				map.get("ptErrorArray").add(pm);
				continue;
			}
			Sku sku=skuService.select("from Sku where code=?", pm.getSkuCode());
			if (sku==null) {
				pm.setMark("sku编号有误！");
				map.get("ptErrorArray").add(pm);
				continue;
			}
			WareHouse WareHouse = wareHouseService.select("from WareHouse where id=?", parseInteger(pm.getWarehouseId()));
			if (WareHouse==null) {
				pm.setMark("仓库编号有误！");
				map.get("ptErrorArray").add(pm);
				continue;
			}
			SkuWare skuWare = skuWareService.select("from SkuWare where skuCode=? and wareHouseId=?", pm.getSkuCode(),parseInteger(pm.getWarehouseId()));
			if (skuWare==null) {
				skuWare=new SkuWare();
				skuWare.setSkuId(sku.getId());
				skuWare.setWareHouseId(WareHouse.getId());
				skuWare.setWareHouseName(WareHouse.getCorporateName());
				skuWare.setSkuCode(sku.getCode());
				skuWare.setWareHouseCode(WareHouse.getErp());
				skuWare.setQuantity(parseInteger(pm.getNumber()));
				Integer skuInventory= sku.getInventory()+parseInteger(pm.getNumber());
				sku.setInventory(skuInventory);
			}else {
				skuWare.setQuantity(parseInteger(pm.getNumber())+skuWare.getQuantity());
				Integer skuInventory= sku.getInventory()+parseInteger(pm.getNumber());
				sku.setInventory(skuInventory);
			}
			skuService.save(sku);
			skuWareService.save(skuWare);	
			map.get("ptSucceedArray").add(pm);//成功
			
		}
		return map;
	}
	
	public static List<SkuInventoryInputExcelMode> getWarehouseExcel(InputStream file) throws CheckException {
		List<List<String>> list= new ArrayList<List<String>>() ;
		List<SkuInventoryInputExcelMode> result = new ArrayList<SkuInventoryInputExcelMode>() ;
		try {
			list = ExcelRead.readExcel(file);
		} catch (Exception e) {
			
			throw new CheckException("解析EXCEL 文件异常！") ;
		}
		
		for(List<String> datas : list) {
			
			if(datas.size() == 4 ) {
				SkuInventoryInputExcelMode pm = new SkuInventoryInputExcelMode() ;
				pm.setSkuCode(datas.get(1));
				pm.setWarehouseId(datas.get(2));
				pm.setNumber(datas.get(3));
				result.add(pm) ;
			}
		}
		return result ;
	}
}
