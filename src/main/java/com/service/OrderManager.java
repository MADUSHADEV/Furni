package com.service;

import com.dto.Cart_DTO;
import com.google.gson.JsonObject;
import com.model.PayhereHashGenerator;
import com.util.ShippingMethod;

import java.util.HashMap;
import java.util.List;

public class OrderManager {

    public JsonObject createOrder(List<Cart_DTO> cartItems) {
        HashMap<Integer, Double> productAmounts = new HashMap<>();
        cartItems.forEach(cartItem -> {
            // Product amount calculator
            HashMap<Integer, Double> productAmount = calculateProductAmount(cartItem.getProduct().getId(), cartItem.getQty(), cartItem.getProduct().getPrice());
            productAmounts.putAll(productAmount);
            System.out.println("Product Amount: " + productAmount);
        });
        // Calculate total amount
        double totalAmount = calculateTotalAmount(productAmounts);
        System.out.println("Total Amount: " + totalAmount);
        // order_id creator
        int orderId = createOrderId();
        String hashCode = PayhereHashGenerator.getData(orderId, totalAmount);

        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("order_id", orderId);
        returnObject.addProperty("total_amount", totalAmount);
        returnObject.addProperty("hash", hashCode);

        return returnObject;
    }

    //order_id creator
    private int createOrderId() {
        return (int) (Math.random() * 1000000);
    }

    //Product amount calculator
    private HashMap<Integer, Double> calculateProductAmount(int productId, int qty, double itemPrice) {
        double totalAmount = (qty * itemPrice) + ShippingMethod.STANDARD.getPrice();
        HashMap<Integer, Double> productAmount = new HashMap<>();
        productAmount.put(productId, totalAmount);
        return productAmount;
    }

    private double calculateTotalAmount(HashMap<Integer, Double> productAmount) {
        return productAmount.values().stream().mapToDouble(Double::doubleValue).sum();
    }

}
