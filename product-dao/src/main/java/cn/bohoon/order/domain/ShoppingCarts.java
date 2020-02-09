package cn.bohoon.order.domain;

import java.util.List;

import cn.bohoon.order.entity.ShoppingCart;

public class ShoppingCarts {

	List<ShoppingCart> carts;

	public List<ShoppingCart> getCarts() {
		return carts;
	}

	public void setCarts(List<ShoppingCart> carts) {
		this.carts = carts;
	}

}
