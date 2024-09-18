package com.controller;

import com.dto.Cart_DTO;
import com.dto.Response_DTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.service.CartService;
import com.service.OrderManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderController", value = "/order")
public class OrderController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response_DTO responseDTO = new Response_DTO();
        HttpSession session = (HttpSession) req.getSession();
        //load cart items
        CartService cartService = new CartService();
        List<Cart_DTO> currentCartDetails = cartService.loadCartItems(session);
        //create order
        OrderManager newOrder = new OrderManager();
        JsonObject orderDetails = newOrder.createOrder(currentCartDetails);
        //return order details
        resp.setContentType("application/json");
        responseDTO.setSuccess(true);
        responseDTO.setMessage(orderDetails);
        resp.getWriter().write(new Gson().toJson(responseDTO));
    }
}
