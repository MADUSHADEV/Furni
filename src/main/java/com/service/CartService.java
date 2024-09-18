package com.service;

import com.dto.Cart_DTO;
import com.dto.Response_DTO;
import com.dto.User_DTO;
import com.model.HibernateUtil;
import com.entity.Cart;
import com.entity.Product;
import com.entity.User;
import com.model.Validation;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    public Response_DTO addToCart(int prodId, int productQty, HttpSession httpSession) {
        Response_DTO responseDTO = new Response_DTO();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (productQty <= 0) {
                responseDTO.setMessage("Quantity must be greater than 0");
                return responseDTO;
            }

            Product product = session.get(Product.class, prodId);
            if (product == null) {
                responseDTO.setMessage("Product not found");
                return responseDTO;
            }

            if (httpSession.getAttribute("user") != null) {
                // Handle logged-in user's cart
                responseDTO = handleDatabaseCart(httpSession, product, productQty, session, transaction);
            } else {
                // Handle session-based cart
                responseDTO = handleSessionCart(httpSession, product, productQty);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setMessage("An error occurred. Please try again.");
        } finally {
            session.close();
        }

        return responseDTO;
    }

    private Response_DTO handleDatabaseCart(HttpSession httpSession, Product product, int productQty, Session session, Transaction transaction) {
        Response_DTO responseDTO = new Response_DTO();
        User_DTO userDTO = (User_DTO) httpSession.getAttribute("user");

        Criteria criteriaUser = session.createCriteria(User.class);
        criteriaUser.add(Restrictions.eq("email", userDTO.getEmail()));
        User user = (User) criteriaUser.uniqueResult();

        Criteria criteriaCart = session.createCriteria(Cart.class);
        criteriaCart.add(Restrictions.eq("user", user));
        criteriaCart.add(Restrictions.eq("product", product));

        if (criteriaCart.list().isEmpty()) {
            if (productQty <= product.getQty()) {
                Cart cart = new Cart();
                cart.setProduct(product);
                cart.setQty(productQty);
                cart.setUser(user);
                session.save(cart);
                transaction.commit();

                responseDTO.setSuccess(true);
                responseDTO.setMessage("Product added to cart");
            } else {
                responseDTO.setMessage("Quantity not available");
            }
        } else {
            Cart existingCart = (Cart) criteriaCart.uniqueResult();
            if ((existingCart.getQty() + productQty) <= product.getQty()) {
                existingCart.setQty(existingCart.getQty() + productQty);
                session.update(existingCart);
                transaction.commit();

                responseDTO.setSuccess(true);
                responseDTO.setMessage("Cart updated");
            } else {
                responseDTO.setMessage("Quantity not available");
            }
        }

        return responseDTO;
    }

    private Response_DTO handleSessionCart(HttpSession httpSession, Product product, int productQty) {
        Response_DTO responseDTO = new Response_DTO();

        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");
        if (sessionCart == null) {
            sessionCart = new ArrayList<>();
        }

        Cart_DTO existingCart = null;
        for (Cart_DTO cartDTO : sessionCart) {
            if (cartDTO.getProduct().getId() == product.getId()) {
                existingCart = cartDTO;
                break;
            }
        }

        if (existingCart != null) {
            if ((existingCart.getQty() + productQty) <= product.getQty()) {
                existingCart.setQty(existingCart.getQty() + productQty);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Cart updated");
            } else {
                responseDTO.setMessage("Quantity not available");
            }
        } else {
            if (productQty <= product.getQty()) {
                Cart_DTO cartDTO = new Cart_DTO();
                cartDTO.setProduct(product);
                cartDTO.setQty(productQty);
                sessionCart.add(cartDTO);

                httpSession.setAttribute("sessionCart", sessionCart);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Product added to cart");
            } else {
                responseDTO.setMessage("Quantity not available");
            }
        }

        return responseDTO;
    }

    public List<Cart_DTO> loadCartItems(HttpSession httpSession) {
        List<Cart_DTO> cartDTOList = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            if (httpSession.getAttribute("user") != null) {
                // Load cart from database
                User_DTO userDTO = (User_DTO) httpSession.getAttribute("user");
                Criteria criteriaUser = session.createCriteria(User.class);
                criteriaUser.add(Restrictions.eq("email", userDTO.getEmail()));
                User user = (User) criteriaUser.uniqueResult();

                Criteria criteriaCart = session.createCriteria(Cart.class);
                criteriaCart.add(Restrictions.eq("user", user));
                List<Cart> cartList = criteriaCart.list();

                // Merge session cart items with database cart items
                List<Cart_DTO> sessionCart = (List<Cart_DTO>) httpSession.getAttribute("sessionCart");
                if (sessionCart != null) {
                    for (Cart_DTO sessionCartItem : sessionCart) {
                        boolean found = false;
                        for (Cart cart : cartList) {
                            if (cart.getProduct().getId() == sessionCartItem.getProduct().getId()) {
                                cart.setQty(cart.getQty() + sessionCartItem.getQty());
                                session.update(cart);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            Cart newCart = new Cart();
                            newCart.setProduct(sessionCartItem.getProduct());
                            newCart.setQty(sessionCartItem.getQty());
                            newCart.setUser(user);
                            session.save(newCart);
                        }
                    }
                    sessionCart.clear();
                    httpSession.setAttribute("sessionCart", sessionCart);
                }

                for (Cart cart : cartList) {
                    Cart_DTO cartDTO = new Cart_DTO();
                    cart.getProduct().getUser().setPassword(null);
                    cart.getProduct().getUser().setVerification_code(null);
                    cartDTO.setProduct(cart.getProduct());
                    cartDTO.setQty(cart.getQty());
                    cartDTOList.add(cartDTO);
                }
            } else {
                // Load cart from session
                if (httpSession.getAttribute("sessionCart") != null) {
                    cartDTOList = (List<Cart_DTO>) httpSession.getAttribute("sessionCart");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return cartDTOList;
    }
}
