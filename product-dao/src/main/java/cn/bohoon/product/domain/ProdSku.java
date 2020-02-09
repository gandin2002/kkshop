package cn.bohoon.product.domain;

import java.util.ArrayList;
import java.util.List;

import cn.bohoon.product.entity.Product;
import cn.bohoon.product.entity.ProductImage;
import cn.bohoon.product.entity.ProductInfo;
import cn.bohoon.product.entity.ProductParam;
import cn.bohoon.product.entity.Sku;
import cn.bohoon.product.entity.SkuAttr;

/**
 * 商品SKU信息
 */
public class ProdSku {
	private Product prod;
	private ProductInfo prodInfo;
	private ProductImage prodImage;
	private List<Sku> skus = new ArrayList<Sku>();
	private List<ProductParam> ppms = new ArrayList<ProductParam>();
	private List<List<SkuAttr>> skuAttrs = new ArrayList<List<SkuAttr>>();
	
	
	public Product getProd() {
		return prod;
	}
	public void setProd(Product prod) {
		this.prod = prod;
	}
	public ProductInfo getProdInfo() {
		return prodInfo;
	}
	public void setProdInfo(ProductInfo prodInfo) {
		this.prodInfo = prodInfo;
	}
	public ProductImage getProdImage() {
		return prodImage;
	}
	public void setProdImage(ProductImage prodImage) {
		this.prodImage = prodImage;
	}
	public List<Sku> getSkus() {
		return skus;
	}
	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}
	
	public List<ProductParam> getPpms() {
		return ppms;
	}
	public void setPpms(List<ProductParam> ppms) {
		this.ppms = ppms;
	}
	public List<List<SkuAttr>> getSkuAttrs() {
		return skuAttrs;
	}
	public void setSkuAttrs(List<List<SkuAttr>> skuAttrs) {
		this.skuAttrs = skuAttrs;
	}
	
}
