package cn.bohoon.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.distribution.entity.ExpfeeTemplate;
import cn.bohoon.distribution.service.ExpfeeTemplateService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderCodeDao;
import cn.bohoon.order.dao.PresetOrdersDao;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderCode;
import cn.bohoon.order.entity.OrderItem;
import cn.bohoon.order.entity.PresetOrderItem;
import cn.bohoon.order.entity.PresetOrders;
import cn.bohoon.order.entity.ShoppingCart;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.TaxCode;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.product.service.TaxCodeService;
import cn.bohoon.userInfo.domain.LoginUser;
import cn.bohoon.userInfo.entity.ShippingInfo;
import cn.bohoon.util.ExpFeeUtil;
import cn.bohoon.util.UserSession;

@Service
@Transactional
public class PresetOrdersService extends BaseService<PresetOrders,String>{

	@Autowired
	PresetOrdersDao presetOrdersDao;

    @Autowired
    PresetOrdersService(PresetOrdersDao presetOrdersDao){
        super(presetOrdersDao);
    }

    @Autowired
    OrderItemService orderItemService;
    
    @Autowired
    SkuService skuService;
    
    @Autowired
    TaxCodeService taxCodeService;
    
    @Autowired
    ShoppingCartService shoppingCartService;
    
    @Autowired
    PresetOrderItemService presetOrderItemService;
	@Autowired
	ExpfeeTemplateService expfeeTemplateService;
    
    public List<PresetOrderItem> createOrder(PresetOrders order, List<ShoppingCart> carts) {
  		List<PresetOrderItem> list = new ArrayList<PresetOrderItem>();
  		BigDecimal totalPrice = new BigDecimal(0) ; //订单总金额
  		BigDecimal deliverFee = new BigDecimal(15) ; //运费
  		BigDecimal couponReduction = new BigDecimal(0) ;
  		BigDecimal totalWeight = new BigDecimal(0); // 总重量
  		BigDecimal totalVolume = new BigDecimal(0); // 总体积
  		
  		int totalNum = 0 ;
  		Integer rowNo = 1;
  		
  		for (ShoppingCart cart : carts) {
  			Sku sku = skuService.get(cart.getSkuId()) ;
  			BigDecimal price = sku.getVipPrice(order.getUserId()) ;
  			BigDecimal marketPrice = sku.getSkuPrice().subtract(price) ;
  			Integer quantity = cart.getQuantity() ;
  			BigDecimal _item_price = price.multiply(new BigDecimal(quantity)) ;
  			//实际金额
  			PresetOrderItem orderItem = makeItem(quantity,sku,order);
  			orderItem.setRowNo(rowNo++);
  			orderItem.setPrice(price);
  			orderItem.setSkuErpCode(sku.getErpCode());
  			if(!StringUtils.isEmpty(sku.getTaxId())){
  				TaxCode tax = taxCodeService.select(" from TaxCode where id = ?",sku.getTaxId());
  				orderItem.setTax(tax.getTax());
  				orderItem.setTaxId(tax.getId());
  				if(!StringUtils.isEmpty(sku.getTaxCode())){
  					orderItem.setErpTaxId(tax.getErpCode());
  				}
  				BigDecimal oAmountWithTax = orderItem.getPrice().multiply(new BigDecimal(quantity));//总金额 含税金额
  				
  				BigDecimal otax = oAmountWithTax.multiply(new BigDecimal(orderItem.getTax()*0.01));//不含税金额
  				BigDecimal 	oAmount = oAmountWithTax.subtract(otax);//税金
  				
  				orderItem.setoAmountWithTax(oAmountWithTax);
  				orderItem.setoAmount(oAmount);
  				orderItem.setOtax(otax);
  				
  			}
  			
  			Integer saleQuantity= quantity/sku.getTranslateRate();
  			
  			orderItem.setSaleQuantity(saleQuantity);
  			orderItem.setSaleUnitName(sku.getAuxUnitName());
//  			orderItem.setSkuCode(sku.getCode());
  			orderItem.setUnitCode(sku.getErpUnitCode());
  			presetOrderItemService.save(orderItem);
  			totalPrice = totalPrice.add(_item_price) ;
  			totalWeight = totalWeight.add(sku.getWeight().multiply(new BigDecimal(quantity))) ;
  			totalVolume = totalVolume.add(sku.getVolume().multiply(new BigDecimal(quantity))) ;
  			couponReduction = couponReduction.add(marketPrice.multiply(new BigDecimal(quantity))) ;
  			list.add(orderItem);
  			totalNum += quantity ;
  			if(!StringUtils.isEmpty(cart.getId())) {
  				Integer cartId = cart.getId() ;
  				shoppingCartService.execute(" delete from ShoppingCart where id=?", cartId) ;
  			}
  		}
  	//运费计算
  			String userId = order.getUserId();
  			ExpfeeTemplate template = expfeeTemplateService.getSet();
  			
  			ShippingInfo sif = new ShippingInfo();
  			sif.setProvince(order.getProvince());
  			if (null != template) {
  				if (template.getCalcTye() == 0) {
  					deliverFee = ExpFeeUtil.calcByPrice(totalPrice,userId, template);
  				}else if (template.getCalcTye() == 1) {
  				
  					if (template.getCoefficient()!=null&&template.getCoefficient().doubleValue()!=0) {
  						//先换算体积得到体重
  						Double dou = totalVolume.doubleValue()/template.getCoefficient().doubleValue();
  						//判断 谁重则取谁为体重计算
  						Double wg=dou>=totalWeight.doubleValue()?dou:totalWeight.doubleValue();
  						BigDecimal weight1=BigDecimal.valueOf(wg);
  						//使用重量运费计算方法
  						deliverFee=ExpFeeUtil.calcByTemplate(sif,weight1,userId, template);
  					}else {
  						//使用重量计算
  						deliverFee=ExpFeeUtil.calcByTemplate(sif,totalWeight,userId, template);
  					}
  				}
  			} else {
  				deliverFee = new BigDecimal(15);
  				
  			}
  		if (!list.isEmpty()) {
  			order.setProductFee(totalPrice);
  			order.setDeliverFee(deliverFee);
  			order.setTotalWeight(totalWeight);
  			order.setTotalVolume(totalVolume);
  			order.setTotalNum(totalNum);
  			order.setCouponReduction(couponReduction);
  			order.setTotal(totalPrice.add(deliverFee));
  			this.save(order);
  		}
  		return list ;
  	}
    
	
	/**
	 * 构造清单
	 * 
	 * @param quantity
	 * @param sku
	 * @param order
	 * @return
	 */
	private PresetOrderItem makeItem(Integer quantity, Sku sku, PresetOrders order) {
		PresetOrderItem oit = new PresetOrderItem() ;
		oit.setCreateDate(new Date());
		oit.setMemberId(order.getMemberId());
		oit.setOrderId(order.getId().toString());
		oit.setSkuId(sku.getId()+"");
		oit.setQuantity(quantity);
		oit.setProductId(sku.getProductId());
		oit.setProductName(sku.getProduct().getName());
		oit.setAttrAndAttrValues(sku.getAttrAndAttrValues());
		oit.setSkuCode(sku.getCode());
		oit.setUserId(order.getUserId());
		oit.setUsername(order.getUsername());
		oit.setStorageOut(false);
		oit.setSkuImage(sku.getImageUrl());
		oit.setPrice(sku.getVipPrice(order.getUserId()));
		oit.setVolume(sku.getVolume());
		oit.setWeight(sku.getWeight());
		oit.setScore(sku.getScore());
		oit.setOrdinaryPrice(sku.getProduct().getDisplayPrice());
		oit.setUnitName(sku.getProduct().getUnit().getName());
		oit.setTax(sku.getTax());
		return oit;
	}
}
