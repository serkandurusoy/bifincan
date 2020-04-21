package com.dna.bifincan.model.order;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.dna.bifincan.model.user.User;

public class ShoppingCart implements Serializable {
	private static final long serialVersionUID = -8867386385949244597L;
	
	private User user; // this perhaps is not necessary
	private Set<String> products;
	private int totalPointsOfProducts = 0;
	
	public ShoppingCart() { 
		this.products = new HashSet<String>();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<String> getProducts() {
		return products;
	}

	public void setProducts(Set<String> products) {
		this.products = products;
	}
	
	public int getTotalPointsOfProducts() {
		return totalPointsOfProducts;
	}

	public void setTotalPointsOfProducts(int totalPointsOfProducts) {
		this.totalPointsOfProducts = totalPointsOfProducts;
	}

	public int getSize() {
		return (products != null)?products.size():0;
	}
	
	// --- other operations --- //
	public void addProductSlug(String productSlug) {
		products.add(productSlug);
	}
	
	public void removeProductSlug(String productSlug) {
		products.remove(productSlug);  
	}
	
	public boolean includesProductSlug(String productSlug) {
		return products.contains(productSlug); 
	}
}
