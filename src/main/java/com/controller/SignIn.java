package com.controller;

import com.dto.Response_DTO;
import com.dto.User_DTO;
import com.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.spi.AbstractResourceBundleProvider;

@WebServlet(name = "SignIn", value = "/signIn")
public class SignIn extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response_DTO responseDto = new Response_DTO();
        User_DTO user_dto = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(req.getReader(), User_DTO.class);
        String redirectUrl = req.getParameter("redirect");
        System.out.println(redirectUrl);


        if (user_dto.getEmail().isEmpty()) {
            responseDto.setMessage("Email is required");
        } else if (user_dto.getPassword().isEmpty()) {
            responseDto.setMessage("Password is required");
        } else {
            // Check if the user exists in the database
            // If the user exists, set the responseDto success to true and set the message to "User signed in successfully"
            // If the user does not exist, set the responseDto message to "Invalid email or password"
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(User.class);

            criteria.add(Restrictions.eq("email", user_dto.getEmail()));
            criteria.add(Restrictions.eq("password", user_dto.getPassword()));

            if (!criteria.list().isEmpty()) {
                User user = (User) criteria.list().get(0);
                if (!user.getVerification_code().equals("verified")) {
                    req.getSession().setAttribute("email", user_dto.getEmail());
                    responseDto.setMessage("User not verified");
                } else {
                    user_dto.setFirst_name(user.getFirst_name());
                    user_dto.setLast_name(user.getLast_name());
                    user_dto.setPassword(null);
                    req.getSession().setAttribute("user", user_dto);
                    responseDto.setSuccess(true);
                    responseDto.setMessage("User signed in successfully");

                }

            } else {
                responseDto.setMessage("Invalid email or password");
            }
            session.close();
        }
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            System.out.println("Redirecting to: " + redirectUrl);
            resp.setContentType("text/html");
            resp.sendRedirect("http://localhost:8080/furni-1.0-SNAPSHOT/" + redirectUrl);
            return;
        } else {
            System.out.println("Returning JSON response: " + new Gson().toJson(responseDto));
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(responseDto));
        }
    }
}
