package cn.bohoon.product.domain;

import java.util.ArrayList;
import java.util.List;

import cn.bohoon.product.entity.ProductImage;
import cn.bohoon.product.entity.ProductInfo;
import cn.bohoon.product.entity.ProductParam;
import cn.bohoon.product.entity.ProductScore;
import cn.bohoon.product.entity.SkuAttr;
import cn.bohoon.product.entity.SkuScore;

public class ProdSkuScore {

	private ProductScore prod;
	private ProductInfo prodInfo;
	private ProductImage prodImage;
	private SkuScore skuScore;
	private List<SkuScore> skus = new ArrayList<SkuScore>();
	private List<ProductParam> ppms = new ArrayList<ProductParam>();
	private List<List<SkuAttr>> skuAttrs = new ArrayList<List<SkuAttr>>();
	public ProductScore getProd() {
		return prod;
	}
	public void setProd(ProductScore prod) {
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
	public List<SkuScore> getSkus() {
		return skus;
	}
	public void setSkus(List<SkuScore> skus) {
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
	public SkuScore getSkuScore() {
		return skuScore;
	}
	public void setSkuScore(SkuScore skuScore) {
		this.skuScore = skuScore;
	}	
}
