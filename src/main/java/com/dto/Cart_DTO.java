package com.dto;

import com.entity.Product;

import java.io.Serializable;

public class Cart_DTO implements Serializable {
    private int qty;
    private Product product;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart_DTO() {

    }
}
