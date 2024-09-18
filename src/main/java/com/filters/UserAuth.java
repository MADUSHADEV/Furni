package com.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebFilter(urlPatterns = {"/my-account.html", "/checkout.html", "/admin-dashboard/index.html", "/admin-dashboard/add-product.html", "/hello.jsp"})
public class UserAuth implements Filter {
    private static final Logger logger = Logger.getLogger(UserAuth.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
        logger.info("UserAuth filter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("UserAuth filter invoked for URL: " + request.getRequestURI());

        if (request.getSession().getAttribute("user") != null) {
            logger.info("User is logged in, proceeding with request");
            filterChain.doFilter(request, response);
        } else {
            logger.warning("User is not logged in, redirecting to sign-in page");
            response.sendRedirect("/furni-1.0-SNAPSHOT/login.html?redirect=" + request.getRequestURI());
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
        logger.info("UserAuth filter destroyed");
    }
}
