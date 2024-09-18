package com.controller;

import com.dto.Response_DTO;
import com.entity.Category;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.util.HibernateSessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryController", value = "/category")
public class CategoryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response_DTO responseDto = new Response_DTO();
        Gson gson = new Gson();
        Session session = HibernateSessionUtil.openSession();

        Criteria criteriaCategory = session.createCriteria(Category.class);
        criteriaCategory.addOrder(Order.asc("name"));
        List<Category> category = criteriaCategory.list();

        JsonObject featureList = new JsonObject();
        featureList.add("category", gson.toJsonTree(category));

        responseDto.setSuccess(true);
        responseDto.setMessage(featureList);

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDto));

        session.close();
    }
}
