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

@WebServlet(name = "ProductSearch", value = "/product-search")
public class ProductSearch extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response_DTO responseDTO = new Response_DTO();
        ShopKeeper shopKeeper = new ShopKeeper();
        try {
            // Get the search keyword from the request
            String keyword = req.getParameter("param");
            List<Product> productList = shopKeeper.searchProductsByKeyword(keyword);

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
