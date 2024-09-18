package com.util;

import com.model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateSessionUtil extends HibernateUtil {
    public static Session openSession() {
        SessionFactory sessionFactory = getSessionFactory();
        return sessionFactory.openSession();
    }
}