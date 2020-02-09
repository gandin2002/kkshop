package cn.bohoon.product.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bohoon.basicSetup.domain.SysParamType;
import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.framework.service.UniversalService;
import cn.bohoon.product.dao.SkuDao;
import cn.bohoon.product.entity.Sku;

/**
 * 商品sku 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class SkuService extends BaseService<Sku,Integer>{

	@Autowired
	SkuDao skuDao;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UniversalService universalService;
	
	@Autowired
	SysParamService sysParamService;
	
	@Autowired
	SkuWareService skuWareService;

    @Autowired
    SkuService(SkuDao skuDao){
        super(skuDao);
    }
    
    Logger logger = LoggerFactory.getLogger(getClass());
    
    public List<Sku> findSkusByIds(Integer[] skuIds) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("ids", Arrays.asList(skuIds));
    	List<Sku> skus= skuDao.findAll(" from Sku where id IN :ids ",params);
    	
    	return skus ;
    }
    /**
     * 下架SKu 
     * 如果货品的SKu全部为下架状态  货品改为下架状态
     * @param sKuId
     */
	public void solout(Integer sKuId) {
		logger.info("-------开始下架Sku:{}-------",sKuId);
		
		execute(" update Sku  set flag = 0 where id = ? ", sKuId);
		
		Sku sku = this.select(" from Sku where id = "+sKuId);
		Integer num= this.list(" from Sku where flag = 1  and productId = "+sku.getProductId()).size();
		if(num == 0){
			productService.execute(" update Product  set flag = 0   where id = "+sku.getProductId());
		}
		logger.info("-------结束下架-------");
	}
    /**
     * 上架SKu 
     * 如果货品的SKu为上架状态  货品改为上架状态
     * @param sKuId
     */
	public void putaway(Integer sId) {
		
		logger.info("-------开始上架Sku:{}-------",sId);
		
		execute(" update Sku  set flag = 1 where id = ? ", sId);
		Sku sku = this.select(" from Sku where id = "+sId);
		productService.execute(" update Product  set flag = 1   where id = "+sku.getProductId());
		
		logger.info("-------结束上架-------");
	}
    
	/**
	 *  删除SKu
	 * @param skuId
	 */
	public void deleteSku(Integer skuId) {
		logger.info("-------开始删除Sku:{}-------",skuId);
		
		execute(" delete Sku where id = "+skuId);
		
		universalService.execute(" delete SkuAttr where skuId = "+skuId);
		
		String hql16 = " update ShoppingCart set skuId = -10  where skuId in  (select id from Sku where id = ? )";//购物车
		universalService.execute(hql16, skuId);
		
		String hql17 = " update CreditFlow set skuId = -10  where skuId in   (select id from Sku where id = ? )";//信用流水
		universalService.execute(hql17, skuId);
		
		logger.info("-------结束删除-------");
		
	}
	/**
	 *  通过PorductId 得到SKu
	 * @param PorductId
	 */
	public Sku getSkuByPorductId(Integer PorductId){
	String Hql="from Sku where productId=?";
		return skuDao.select(Hql,PorductId);
	}
	
	/**
	 * 通过后台设置来判断什么时候减库存
	 */
	public void subInventory(int senum,int skuId,String flag,int wareHouseId){
		//"下单立减库存":"0","付款立减库存":"1","发货立减库存":"2"
		
		SysParam sysParam=sysParamService.findParam("INVENTORY_COUNT", SysParamType.PRODUCT_PARAM);
		String value=sysParam.getValue();
		if(flag.equals(value)){
			//sku表减库存
			skuDao.execute("update Sku set inventory=inventory-? where id=?", senum,skuId);
			//仓库减库存
			if(wareHouseId!=0){
				skuWareService.execute("update SkuWare set quantity=quantity-? where wareHouseId=? and skuId=?", senum,wareHouseId,skuId);
			}
		}
	}
	
	
}