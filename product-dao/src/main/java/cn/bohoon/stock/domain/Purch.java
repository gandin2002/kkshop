package cn.bohoon.stock.domain;

import java.util.ArrayList;
import java.util.List;

import cn.bohoon.stock.entity.PurchReturnItem;
import cn.bohoon.stock.entity.Purchase;
import cn.bohoon.stock.entity.PurchaseItem;
import cn.bohoon.stock.entity.PurchaseReturn;

public class Purch {
	
	Purchase purchase;
	PurchaseReturn purchaseReturn ; 
	
	List<PurchaseItem> item = new ArrayList<PurchaseItem>();
	List<PurchReturnItem> itemReturn = new ArrayList<PurchReturnItem>();

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public List<PurchaseItem> getItem() {
		return item;
	}

	public void setItem(List<PurchaseItem> item) {
		this.item = item;
	}

	public PurchaseReturn getPurchaseReturn() {
		return purchaseReturn;
	}

	public void setPurchaseReturn(PurchaseReturn purchaseReturn) {
		this.purchaseReturn = purchaseReturn;
	}

	public List<PurchReturnItem> getItemReturn() {
		return itemReturn;
	}

	public void setItemReturn(List<PurchReturnItem> itemReturn) {
		this.itemReturn = itemReturn;
	}

	
}
