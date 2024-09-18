package com.controller;

import com.dto.Response_DTO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UserChecker", value = "/admin-dashboard/user-checker")
public class UserChecker extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = (HttpSession) req.getSession();
        Response_DTO responseDto = new Response_DTO();
        if (session.getAttribute("user") == null) {
            resp.setHeader("Content-Type", "application/json");
            responseDto.setMessage("User not logged in");
        } else {
            responseDto.setSuccess(true);
            responseDto.setMessage("User logged in");
        }
        resp.getWriter().write(new Gson().toJson(responseDto));
    }
}
