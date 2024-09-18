package com.controller;

import com.dto.Response_DTO;
import com.dto.User_DTO;
import com.entity.Category;
import com.entity.Product;
import com.entity.Status;
import com.entity.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.model.HibernateUtil;
import com.model.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.naming.Referenceable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@MultipartConfig
@WebServlet(name = "AdminProduct", value = "/admin-dashboard/admin-product")
public class AdminProduct extends HttpServlet {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        com.dto.Response_DTO responseDto = new Response_DTO();
        Gson gson = new Gson();
        Session session = sessionFactory.openSession();

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        com.dto.Response_DTO responseDto = new Response_DTO();
        Session session = sessionFactory.openSession();

        String categoryId = req.getParameter("categoryId");
        String productTitle = req.getParameter("productTitle");
        String productDescription = req.getParameter("productDescription");
        String price = req.getParameter("price");
        String quantity = req.getParameter("quantity");

        Part image1 = req.getPart("image1");


        if (productTitle.isEmpty()) {
            responseDto.setMessage("Product title is required");
        } else if (!Validation.validateIntegerValue(categoryId)) {
            responseDto.setMessage("Invalid category");
        } else if (price.isEmpty()) {
            responseDto.setMessage("Price is required");
        } else if (productDescription.isEmpty()) {
            responseDto.setMessage("Product description is required");
        } else if (!Validation.validateDoubleValue(price)) {
            responseDto.setMessage("Invalid price");
        } else if (quantity.isEmpty()) {
            responseDto.setMessage("Quantity is required");
        } else if (categoryId.isEmpty() || categoryId.equals("0")) {
            responseDto.setMessage("Category is required");
        } else if (!Validation.validateDoubleValue(quantity)) {
            responseDto.setMessage("Invalid quantity");
        } else if (image1.getSubmittedFileName() == null) {
            responseDto.setMessage("Image 1 is required");
        } else {
            // Get the relative status from the database
            Status productStatus = session.get(Status.class, 1);

            // Get the user from the session
            User_DTO user_dto = (User_DTO) req.getSession().getAttribute("user");
            Criteria userCriteria = session.createCriteria(User.class);
            userCriteria.add(Restrictions.eq("email", user_dto.getEmail()));
            User user = (User) userCriteria.uniqueResult();

            Product product = new Product();
            product.setStatus(productStatus);
            product.setTitle(productTitle);
            product.setDescription(productDescription);
            product.setPrice(Double.parseDouble(price));
            product.setQty(Integer.parseInt(quantity));
            product.setUser(user);
            product.setCategory(session.get(Category.class, Integer.parseInt(categoryId)));

            session.beginTransaction();
            int productId = (int) session.save(product);
            session.getTransaction().commit();
            
            String uploadPath = "D:\\My Programming\\furni project\\project\\furni\\src\\main\\webapp\\images\\product-image";
            System.out.println("application path" + uploadPath);

            File folder = new File(uploadPath);

            // Create the folder if it does not exist
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file1 = new File(folder, productId + ".jpg");
            InputStream image1InputStream = image1.getInputStream();
            Files.copy(image1InputStream, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

            responseDto.setSuccess(true);
            responseDto.setMessage("Product listed successfully");
        }

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(responseDto));
        session.close();
    }
}
