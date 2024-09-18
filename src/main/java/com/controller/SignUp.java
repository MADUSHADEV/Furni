package com.controller;

import com.dto.Response_DTO;
import com.dto.User_DTO;
import com.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.EmailUtil;
import com.model.HibernateUtil;
import com.model.Validation;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;

@WebServlet(name = "SignUp", value = "/signup")
public class SignUp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Response_DTO responseDto = new Response_DTO();
        new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_dto = new Gson().fromJson(request.getReader(), User_DTO.class);

        if (user_dto.getFirst_name().isEmpty()) {
            responseDto.setMessage("First name is required");
        } else if (user_dto.getLast_name().isEmpty()) {
            responseDto.setMessage("Last name is required");
        } else if (user_dto.getEmail().isEmpty()) {
            responseDto.setMessage("Email is required");
        } else if (user_dto.getPassword().isEmpty()) {
            responseDto.setMessage("Password is required");
        } else if (!Validation.validateEmail(user_dto.getEmail())) {
            responseDto.setMessage("Invalid email");
        } else if (!Validation.validatePassword(user_dto.getPassword())) {
            responseDto.setMessage("Invalid password");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", user_dto.getEmail()));
            if (!criteria.list().isEmpty()) {
                responseDto.setMessage("Email already exists");
            } else {
                int Code = (int) (Math.random() * 1000000);
                User user = new User();
                user.setFirst_name(user_dto.getFirst_name());
                user.setLast_name(user_dto.getLast_name());
                user.setEmail(user_dto.getEmail());
                user.setPassword(user_dto.getPassword());
                user.setVerification_code(String.valueOf(Code));

                Thread mailThread = new Thread(() -> {
                    try {
                        EmailUtil.sendEmail(user.getEmail(), "Verification Code", "Your verification code is: " + Code);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                });
                mailThread.start();

                request.getSession().setAttribute("email", user_dto.getEmail());
                session.save(user);
                session.beginTransaction().commit();
                responseDto.setSuccess(true);
                responseDto.setMessage("User registered successfully");
            }
            session.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(responseDto));
    }
}
