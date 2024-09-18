package com.controller;

import com.dto.Response_DTO;
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

@WebServlet(name = "SortController", value = "/sort")
public class SortController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response_DTO responseDTO = new Response_DTO();
        ShopKeeper shopKeeper = new ShopKeeper();
        try {
            // Get the search keyword from the request
            String categoryIdParam = req.getParameter("categoryId");
            String maxPriceParam = req.getParameter("maxPrice");
            String minPriceParam = req.getParameter("minPrice");

            System.out.println("categoryIdParam: " + categoryIdParam);
            System.out.println("maxPriceParam: " + maxPriceParam);
            System.out.println("minPriceParam: " + minPriceParam);

            int categoryId = categoryIdParam != null && !categoryIdParam.isEmpty() ? Integer.parseInt(categoryIdParam) : 0;
            double maxPrice = maxPriceParam != null && !maxPriceParam.isEmpty() ? Double.parseDouble(maxPriceParam) : Double.MAX_VALUE;
            double minPrice = minPriceParam != null && !minPriceParam.isEmpty() ? Double.parseDouble(minPriceParam) : 0.0;

            List<Product> productList = shopKeeper.getProductsByCategoryOrPriceRange(categoryId, minPrice, maxPrice);

            if (productList.isEmpty()) {
                responseDTO.setMessage("No products found");
            }
            for (Product product : productList) {
                product.setUser(null);
            }
            responseDTO.setSuccess(true);
            responseDTO.setMessage(productList);

            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(responseDTO));
        } catch (
                Exception e) {
            Logger.getLogger(ProductSearch.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
