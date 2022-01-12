package com.cb008264.easy_pill_android.model;

import java.io.Serializable;

public class Cart implements Serializable {
    private String cartId;
    private String medicineId;
    private String userId;
    private String productName;
    private String subTotal;
    private Integer quantity;

    public Cart() {
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId='" + cartId + '\'' +
                ", medicineId='" + medicineId + '\'' +
                ", userId='" + userId + '\'' +
                ", productName='" + productName + '\'' +
                ", subTotal='" + subTotal + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
