package cn.bohoon.product.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.helper.CompanyLevelCache;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.service.SkuService;
import cn.bohoon.quotation.entity.QuotationItem;
import cn.bohoon.quotation.service.QuotationItemService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;

/**
 * 
 * @author HJ
 * 2018年5月31日,下午4:49:53
 */
public class PriceCountUtil {
	/**
	 * 获取SKu
	 * @param product
	 * @return
	 */
	public static Sku getSKu(Product product) {

		SkuService skuService = SpringContextHolder.getBean(SkuService.class);

		List<Sku> skuList = skuService.list(" from Sku where productId = ? and flag=1 and status = 1 ", product.getId());
		if (skuList.isEmpty()) {
			return null;
		}
		for (Sku sku : skuList) {
			if(product.getUnitId().equals(sku.getAuxUnitId())){
				return sku;
			}
		}
		Sku sku = skuList.get(0);
		return sku;

	}
	 /**
	  * 获取用户信息
	  * @param userId
	  * @return
	  */
	public static UserInfo getUserInfo(String userId) {
		UserInfoService userInfoService = SpringContextHolder.getBean(UserInfoService.class);
		CompanyService companyService = SpringContextHolder.getBean(CompanyService.class);
		UserInfo userInfo = userInfoService.queryRespUser(userId);
		if (userInfo == null) {
			return null;
		}
		if( StringUtils.isEmpty(userInfo.getCompanyId())){
			return null;
		}
		Company company = companyService.get(userInfo.getCompanyId());
		if (company == null) {
			return null;
		}
		return userInfo;
	}

	/**
	 * 通过SPU  unit 获取 SKu价格
	 * @param userId
	 * @param product
	 * @return
	 */
	public static BigDecimal getPrice(String userId, Product product) {
		Sku sku = getSKu(product);
		return getPrice(userId, sku);
	}

	/**
	 * 通过   SKu 获取价格
	 * @param userId
	 * @param product
	 * @return
	 */
	public static BigDecimal getPrice(String userId, Sku sku) {
		if(StringUtils.isEmpty(userId)){
			return sku.getSkuPrice();
		}
		UserInfo userInfo = getUserInfo(userId);
		if (userInfo == null) {
			return sku.getSkuPrice();
		}
		BigDecimal price = countQuotationItemPrice(userInfo, sku);
		if (price == null) {
			price = countVipPrice(userId, sku.getSkuPrice());
		}

		return price;
	}

	public static BigDecimal getVipPrice(String userId, Product product) {
		Sku sku = getSKu(product);
		if(sku == null){
			return product.getSalesPrice();
		}
		return getVipPrice(userId, sku);
	}

	public static BigDecimal getVipPrice(String userId, Sku sku) {
		if (StringUtils.isEmpty(userId)) {
			return sku.getSkuPrice();
		}
		return countVipPrice(userId, sku.getSkuPrice());
	}
	
	public static BigDecimal getQuotationItemPrice(String userId, Product product) {
		Sku sku = getSKu(product);
		return getQuotationItemPrice(userId,sku);
	}
	
	public static BigDecimal getQuotationItemPrice(String userId,Sku sku){
		if (StringUtils.isEmpty(userId)) {
			return null;
		}
		UserInfo userInfo = getUserInfo(userId);
		if (userInfo == null) {
			return null;
		}
		BigDecimal price = countQuotationItemPrice(userInfo, sku);
		return price;
	}

	/**
	 * 报价单价格
	 * 
	 * @param userInfo
	 * @param sku
	 * @return
	 */
	public static BigDecimal countQuotationItemPrice(UserInfo userInfo, Sku sku) {
		if(StringUtils.isEmpty(userInfo.getCompanyId())){
			return null;
		}
		String hql = "select  qit from QuotationItem as qit,Quotation as qn  where qit.quotationId = qn.id and qn.quotationState =  'PASS_QUOTATION' and qn.companyId = ?  and qit.ValidDate  > NOW()  ";
		List<Object> params = new ArrayList<>();
		params.add(userInfo.getCompanyId());
		if (sku != null) {
			hql+=" and qit.skuId = ?";
			params.add(sku.getId());
		}
		hql +=" ORDER BY qit.ValidDate DESC ";

		QuotationItemService service = SpringContextHolder.getBean(QuotationItemService.class);
		List<QuotationItem> items = service.list(hql, params.toArray());
		if (!items.isEmpty()) {
			return items.get(0).getQuotationPrice();
		}

		return null;
	}

	/**
	 * 企业等级优惠
	 * @param userId
	 * @param skuPrice
	 * @return
	 */
	public static BigDecimal countVipPrice(String userId, BigDecimal skuPrice) {
		Float disCount = CompanyLevelCache.getCache().getDisCount(userId);
		skuPrice = skuPrice.multiply(new BigDecimal(disCount / 100));
		skuPrice = skuPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN);
		return skuPrice;
	}

}
