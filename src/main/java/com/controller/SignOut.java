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

@WebServlet(name = "SignOut", value = "/admin-dashboard/sign-out")
public class SignOut extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Response_DTO responseDto = new Response_DTO();
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        responseDto.setSuccess(true);
        responseDto.setMessage("User logged out");
        response.getWriter().write(new Gson().toJson(responseDto));
    }
}
