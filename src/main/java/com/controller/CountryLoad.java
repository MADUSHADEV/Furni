package com.controller;

import com.entity.Country;
import com.google.gson.Gson;
import com.util.HibernateSessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CountryLoad", value = "/country")
public class CountryLoad extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = HibernateSessionUtil.openSession();
        Criteria country = session.createCriteria(Country.class);
        country.addOrder(Order.asc("name"));
        List<Country> countries = country.list();

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(countries));
    }
}
