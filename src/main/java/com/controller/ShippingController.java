package com.controller;

import com.dto.Response_DTO;
import com.dto.User_DTO;
import com.entity.Address;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.service.ShippingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AddressController", value = "/address")
public class ShippingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = (HttpSession) req.getSession();
        try {
            ShippingService shippingService = new ShippingService();
            User_DTO userDto = (User_DTO) session.getAttribute("user");

            if (userDto == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("User not logged in");
                return;
            }

            List<Address> addresses = shippingService.getCurrentUserAddressDetails(session);
            JsonObject responseObject = new JsonObject();

            responseObject.addProperty("first_name", userDto.getFirst_name());
            responseObject.addProperty("last_name", userDto.getLast_name());
            responseObject.addProperty("email", userDto.getEmail());

            JsonArray addressArray = new JsonArray();

            if (!addresses.isEmpty()) {
                for (Address address : addresses) {
                    JsonObject addressJson = new JsonObject();
                    addressJson.addProperty("line1", address.getLine1());
                    addressJson.addProperty("line2", address.getLine2());
                    addressJson.addProperty("state", address.getState());
                    addressJson.addProperty("mobile", address.getMobile());
                    addressJson.addProperty("country", address.getCountry().getId());
                    addressJson.addProperty("address_id", address.getId());
                    addressArray.add(addressJson);
                }
            }
            responseObject.add("addresses", addressArray);
            resp.setContentType("application/json");
            resp.getWriter().write(responseObject.toString());

        } catch (Exception e) {
            Logger.getLogger(ShippingController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = (HttpSession) req.getSession();
        Response_DTO responseDto = new Response_DTO();
        Gson gson = new Gson();
        ShippingService shippingService = new ShippingService();
        try {
            Address address = gson.fromJson(req.getReader(), Address.class);
            boolean shippingData = shippingService.addNewUserAddress(session, address);
            if (shippingData) {
                responseDto.setSuccess(true);
                responseDto.setMessage("Address added successfully");
            } else {
                responseDto.setMessage("User already has an address");
            }

            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(responseDto));

        } catch (Exception e) {
            Logger.getLogger(ShippingController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
