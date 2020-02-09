package cn.bohoon.product.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alipay.api.domain.Data;

import cn.bohoon.framework.exception.CheckException;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.service.UniversalService;
import cn.bohoon.product.dao.ProductDao;
import cn.bohoon.product.domain.ProdSku;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductImage;
import cn.bohoon.product.entity.ProductInfo;
import cn.bohoon.product.entity.ProductParam;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuAttr;
import cn.bohoon.product.entity.TaxCode;
import cn.bohoon.product.entity.Unit;
import cn.bohoon.util.ConvertUtils;
import cn.bohoon.util.IDUtil;

@Service
@Transactional
public class ProductService extends BaseService<Product,Integer>{

	@Autowired
    SkuService skuService;
	@Autowired
    SkuAttrService skuAttrService;
	@Autowired
    ProductImageService  productImageService;
	@Autowired
	ProductInfoService productInfoService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	BrandService brandService;
	
	@Autowired
	UniversalService universalService;
	
	@Autowired
	ProductParamService ppmService ;
	
	@Autowired
	TaxCodeService taxCodeService;
	
	@Autowired
	UnitService unitService;
	
	Logger logger =LoggerFactory.getLogger(getClass());

    @Autowired
    ProductService(ProductDao productDao){
        super(productDao);
    }
    
    public void saveProdSku(ProdSku prodSku) throws CheckException {
    	Product prod = prodSku.getProd();
    	prod.setCreateDate(new Date());
    	String prodCode = prod.getCode();
    	ProductInfo prodInfo = prodSku.getProdInfo();
    	ProductImage prodImage = prodSku.getProdImage();
    	TaxCode taxCode = taxCodeService.select(" from TaxCode where id = "+prod.getTaxId());
    	prod.setTax(taxCode.getTax());
    	
    	if(StringUtils.isEmpty(prod.getId())) {
	    	Product p = select("from Product where code = ?",prodCode);
	    	if(null != p) {
	    		throw new CheckException("商品编号重复");
	    	}
    	}
    	
    	List<Sku> lsSkus = prodSku.getSkus();
    	if(lsSkus.size() == 0 && prod.getFlag()){
    		throw new CheckException("商品Sku为空，请修改商品上下架状态");
    	}
    	List<Sku> skuList = new ArrayList<>();
    	for(Sku sku : lsSkus) {
    		if(sku.getFlag()){
    			skuList.add(sku);
    		}
    	}
    	if(skuList.isEmpty() && prod.getFlag()){
    		throw new CheckException("商品Sku全部下架，请修改商品上下架状态");
    	}
    	
    	if(StringUtils.isEmpty(prod.getId())) {
        	save(prod);
    	}else {
    		update(prod);
    		skuService.execute("update Sku set status = 0 where productId = ?", prod.getId());
    	}
    	
    	prodInfo.setId(prod.getId());
    	productInfoService.save(prodInfo);
    	
    	productImageService.execute("delete from ProductImage where productId = ?", prod.getId());
    	if(null != prodImage){
    		String[] imageUrls = prodImage.getImageUrl().split(",");
    		List<ProductImage> lsProdImage = new ArrayList<>();
    		for(String imageUrl : imageUrls){
    			ProductImage productImage = new ProductImage();
    			productImage.setProductId(prod.getId());
    			productImage.setImageUrl(imageUrl);
    			lsProdImage.add(productImage);
    		}
    		productImageService.saveBatch(lsProdImage, lsProdImage.size());
    	}
    	
    	List<List<SkuAttr>> lsSkuAttrs = prodSku.getSkuAttrs();
    	for(int i = 0; i < lsSkus.size(); i++) {
    		Sku sku = lsSkus.get(i);
    		sku.setProductId(prod.getId());
    		sku.setStatus(true);
    		
    		List<SkuAttr> skuAttrs = lsSkuAttrs.get(i);
    		if(skuAttrs.size()>0 && StringUtils.isEmpty(sku.getId())) {//保存
    			sku.setCode(IDUtil.getInstance().getId("SK"));
    			sku.setName(prod.getName()+sku.getCode());
    			if(StringUtils.isEmpty(sku.getErpCode())){ //如何ERPcode 客户未填写，那么ERPcode = code;
    				sku.setErpCode(sku.getCode());
    			}
    			if(StringUtils.isEmpty(sku.getTaxId())&&taxCode != null){
    	   			sku.setTax(taxCode.getTax());
        			sku.setTaxId(taxCode.getId());
    			}
    			
    			Unit unit = unitService.get(sku.getAuxUnitId());
    			if(unit != null){
    				 sku.setAuxUnitName(unit.getName());
    			}
    			
    			skuService.save(sku);
    			for(SkuAttr skuAttr : skuAttrs) {
    				skuAttr.setSkuId(sku.getId());
    				skuAttrService.save(skuAttr);
    			}
    		}else {//修改
    			if(StringUtils.isEmpty(sku.getErpCode())){ //如何ERPcode 客户未填写，那么ERPcode = code;
    				sku.setErpCode(sku.getCode());
    			}
    			
    			//if(StringUtils.isEmpty(sku.getTaxId())&&taxCode != null){
    	   			sku.setTax(taxCode.getTax());
        			sku.setTaxId(taxCode.getId());
    			//}
//    			sku.setTax(taxCode.getTax());
//    			sku.setTaxId(taxCode.getId());
    			
    			Unit unit = unitService.get(sku.getAuxUnitId());
    			if(unit != null){
    				 sku.setAuxUnitName(unit.getName());
    			}
    			
    			skuService.update(sku);
    			for (SkuAttr skuAttr : skuAttrs) {
    				SkuAttr target = skuAttrService.select("  from SkuAttr where skuId = "+sku.getId()+" and attrId = "+skuAttr.getAttrId()+" and attrValueId = "+skuAttr.getAttrValueId()+" ");
    				target.setAttrImage(skuAttr.getAttrImage());
    				skuAttrService.update(target);
				}
    		}
    	}
    	
    	List<ProductParam> ppms = prodSku.getPpms() ;
    	ppmService.execute("delete from ProductParam where productId = ?", prod.getId());
    	for(ProductParam pp : ppms) {
    		pp.setProductId(prod.getId());
    		pp.setCategoryId(prod.getCategoryId());
    		ppmService.save(pp);
    	}
    }
    
  //@Scheduled(cron = "0 0 18 * * ?")
	public void setProductSearchWords(){
    	System.out.println("-----start set product searchWords-----");
    	List<Product> productList = list();
    	List<Product> list = new ArrayList<>();
    	for(Product product : productList){
    		StringBuffer buffer = new StringBuffer();
    		buffer.append(product.getCode()+","+product.getName());
    		buffer.append("," + categoryService.get(product.getCategoryId()).getName());
    		if(!StringUtils.isEmpty(product.getLables())){
    			String[] lable = product.getLables().split(",");
    			for(String lable_ : lable)
    				buffer.append("," + lable_);
    		}
    		if(!StringUtils.isEmpty(product.getBrandId()))
    			buffer.append("," + brandService.get(product.getBrandId()).getName());
    		product.setSearchWords(buffer.toString());
    		list.add(product);
    	}
    	saveBatch(list, list.size());
    	System.out.println("-----end set product searchWords-----");
    }

	public String getProductNames(String productIds) {
		Map<String,Object> params = new HashMap<String,Object>();
    	Integer [] intIds =  ConvertUtils.parseIntArr(productIds) ;
    	params.put("ids", Arrays.asList(intIds));
    	List<String> result = list("select name from Product where id IN :ids",String.class,params);
    	return result.toString() ;
	}

	/**
	 * 下架  注： 商品下架 sku 全部下架
	 * @param pid 商品ID
	 */
	public void solout(Integer pid){
		logger.info("-------开始批量下架-------");
			 logger.info("-------商品：{} 下架-------",pid);
			 execute(" update Product set flag = 0 where id = ? ",pid);
			 skuService.execute(" update Sku  set flag = 0 where productId = ? ", pid);
		logger.info("-------结束批量下架-------");
	}

	/**
	 * 删除商品
	 * @param productId
	 */
	public void deleteAll(Integer productId) {
		logger.info("-------开始删除商品:{}-------",productId);
		String hql3 = " delete  ProductCollection where productId = ? ";//删除收藏
		universalService.execute(hql3, productId);
		String hql4 = " delete  ProductImage where productId = ? ";//删除图片
		universalService.execute(hql4, productId);
		String hql5 = " delete  ProductInfo  where id = ? ";//商品详情
		universalService.execute(hql5, productId);
		String hql6 = " delete ProductParam where productId = ? ";//商品参数
		universalService.execute(hql6, productId);
		String hql7 = " delete ProductRecommend where productId = ? ";//推荐商品
		universalService.execute(hql7, productId);
		String hql10 = " delete Browse where productId = ? ";//浏览记录
		universalService.execute(hql10, productId);
		String hql11 = " delete Favorite where productId = ? ";//商品收藏 
		universalService.execute(hql11, productId);
		String hql12 = " delete PlaceProduct where productId = ?  ";//商品推荐
		universalService.execute(hql12,productId);
		String hql13 = " update OrderItem set productId = -10 where productId = ?  "; //订单清单项
		universalService.execute(hql13, productId);
		String hql14 = " update AmOrderItem set productId = -10 where productId = ?  ";//售后清单
		universalService.execute(hql14, productId);
		String hql15 = " update QuotationItem set productId = -10 where productId = ? ";//报价单商品明细
		universalService.execute(hql15, productId);
		
		
		String hql9 = " delete SkuAttr where skuId in (select id from Sku where productId = ?) ";//SKu 规格
		universalService.execute(hql9, productId);
		
		
		String hql16 = " update ShoppingCart set skuId = -10  where skuId in  (select id from Sku where productId = ?)  ";//购物车
		universalService.execute(hql16, productId);
		
		String hql17 = " update CreditFlow set skuId = -10  where skuId in   (select id from Sku where productId = ?)  ";//信用流水
		universalService.execute(hql17, productId);
		
		String hql2 = " delete Sku where productId = ?";
		universalService.execute(hql2, productId);//删除SKu
		
		String hql1 =" delete Product where id = ? ";
		universalService.execute(hql1, productId); //删除SPU
		
		
		logger.info("-------删除商品结束-------");
		
	}

	/**
	 * 批量上架商品
	 * @param pid
	 */
	public  void putaway(Integer pid) {
		
		 logger.info("-------开始批量上架-------");
		 logger.info("-------商品：{} 上架-------",pid);
		 execute(" update Product set flag = 1 where id = ? ",pid);
		 skuService.execute(" update Sku  set flag = 1 where productId = ? ", pid);
		 logger.info("-------结束批量上架-------");
		
	}
}

