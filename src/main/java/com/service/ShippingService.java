package com.service;

import com.entity.Address;
import com.dto.User_DTO;
import com.entity.Country;
import com.entity.User;
import com.util.HibernateSessionUtil;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ShippingService {

    public List<Address> getCurrentUserAddressDetails(HttpSession session) {
        User_DTO userDto = (User_DTO) session.getAttribute("user");
        if (userDto == null) {
            return List.of(); // Return an empty list if no user is logged in
        }

        Session hibernateSession = HibernateSessionUtil.openSession();
        Criteria userCriteria = hibernateSession.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("email", userDto.getEmail()));
        User user = (User) userCriteria.uniqueResult();

        if (user == null) {
            hibernateSession.close();
            return List.of(); // Return an empty list if no matching user is found
        }

        Criteria addressCriteria = hibernateSession.createCriteria(Address.class);
        addressCriteria.add(Restrictions.eq("user.id", user.getId()));
        Address address = (Address) addressCriteria.uniqueResult();

        if (address != null) {
            hibernateSession.close();
            return List.of(address);
        }
        hibernateSession.close();
        return List.of();
    }

    public boolean addNewUserAddress(HttpSession session, Address address) {
        User_DTO userDto = (User_DTO) session.getAttribute("user");
        if (userDto == null) {
            throw new IllegalStateException("No user is logged in");
        }

        Session hibernateSession = HibernateSessionUtil.openSession();
        Criteria userCriteria = hibernateSession.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("email", userDto.getEmail()));
        User user = (User) userCriteria.uniqueResult();

        if (user == null) {
            throw new IllegalStateException("No matching user found");
        }

        // Check if the user already has an address
        Criteria addressCriteria = hibernateSession.createCriteria(Address.class);
        addressCriteria.add(Restrictions.eq("user.id", user.getId()));
        List<Address> existingAddresses = addressCriteria.list();

        if (!existingAddresses.isEmpty()) {
            hibernateSession.close();
            return false; // User already has an address
        }


        //set the user to the address
        address.setUser(user);

        // Set the country to the address
        Criteria countryCriteria = hibernateSession.createCriteria(Country.class);
        countryCriteria.add(Restrictions.eq("id", address.getCountry().getId()));
        Country country = (Country) countryCriteria.uniqueResult();

        if (country == null) {
            throw new IllegalStateException("No matching country found");
        }
        address.setCountry(country);

        Transaction transaction = hibernateSession.beginTransaction();
        hibernateSession.save(address);
        transaction.commit();
        hibernateSession.close();

        return true;
    }
}