package com.controller;

import com.dto.Response_DTO;
import com.entity.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.model.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;

@WebServlet(name = "UserVerify", value = "/userVerify")
public class UserVerify extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject verifyCodeDto = new Gson().fromJson(req.getReader(), JsonObject.class);
        String code = verifyCodeDto.get("verificationCode").getAsString();
        Response_DTO responseDto = new Response_DTO();

        if (req.getSession().getAttribute("email") != null) {
            String email = req.getSession().getAttribute("email").toString();
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));
            criteria.add(Restrictions.eq("verification_code", code));

            if (!criteria.list().isEmpty()) {
                User user = (User) criteria.list().get(0);
                user.setVerification_code("verified");
                session.update(user);
                session.beginTransaction().commit();

                req.getSession().removeAttribute("email");

                responseDto.setSuccess(true);
                responseDto.setMessage("User verified successfully");
            } else {
                responseDto.setMessage("Invalid verification code");
            }

        } else {
            responseDto.setMessage("Invalid request");
        }
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(responseDto));
    }
}
