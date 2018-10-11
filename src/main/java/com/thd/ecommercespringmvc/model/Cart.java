package com.thd.ecommercespringmvc.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cart implements Serializable {
    private Map<Integer, Integer> productQuantity;
    private Map<Integer, Integer> productMaxQuantity;
    public Cart() {
        productQuantity = new HashMap<>();
    }
    public void addToCart(int productId) {
        productQuantity.put(productId, productQuantity.containsKey(productId) ? productQuantity.get(productId) +1: 1);
    }
    public int countProduct() {
        return productQuantity.values().stream().mapToInt(Number::intValue).sum();
    }

    public int getProductOrderNumber(int id) {
        return productQuantity.get(id);
    }

    public Set<Integer> getAllProductId() {
        return productQuantity.keySet();
    }

    public void updateCart(int id,int num) {
        productQuantity.put(id, num);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "productQuantity=" + productQuantity +
                '}';
    }
}
