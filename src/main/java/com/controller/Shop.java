package com.controller;

import com.entity.Product;
import com.google.gson.Gson;
import com.model.ShopKeeper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "Shop", value = "/shop")
public class Shop extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Product> shopKeeper = new ShopKeeper().getAllProductDetails();
            for (Product product : shopKeeper) {
                product.setUser(null);
            }
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(shopKeeper));
        } catch (Exception e) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}


