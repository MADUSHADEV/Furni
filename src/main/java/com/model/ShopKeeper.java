package com.model;

import com.entity.Product;
import com.util.HibernateSessionUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ShopKeeper {

    public List<Product> getAllProductDetails() {
        try (Session session = HibernateSessionUtil.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public List<Product> getProductsByCategoryOrPriceRange(Integer categoryId, Double minPrice, Double maxPrice) {
        try (Session session = HibernateSessionUtil.openSession()) {
            Criteria criteria = session.createCriteria(Product.class);
            if (categoryId != null) {
                criteria.createAlias("category", "c");
                criteria.add(Restrictions.eq("c.id", categoryId));
            }
            if (minPrice != null && maxPrice != null) {
                criteria.add(Restrictions.between("price", minPrice, maxPrice));
            }
            return criteria.list();
        }
    }

    public List<Product> searchProductsByKeyword(String keyword) {
        try (Session session = HibernateSessionUtil.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);

            String keywordPattern = "%" + keyword + "%";
            criteriaQuery.where(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("title"), keywordPattern),
                    criteriaBuilder.like(root.get("description"), keywordPattern),
                    criteriaBuilder.like(root.get("category").get("name"), keywordPattern)
            ));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}