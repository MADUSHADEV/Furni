package com.controller;

import com.dto.Cart_DTO;
import com.dto.Response_DTO;
import com.google.gson.Gson;
import com.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartController", value = "/cart")
public class CartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CartService cartService = new CartService();
        Gson gson = new Gson();
        Cart_DTO cartDTO = gson.fromJson(req.getReader(), Cart_DTO.class);

        int productId = cartDTO.getProduct().getId();
        int qty = cartDTO.getQty();
        HttpSession httpSession = (HttpSession) req.getSession();
        Response_DTO responseDTO = cartService.addToCart(productId, qty, httpSession);

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CartService cartService = new CartService();

        HttpSession httpSession = (HttpSession) req.getSession();
        List<Cart_DTO> cartItems = cartService.loadCartItems(httpSession);

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(cartItems));
    }

}
