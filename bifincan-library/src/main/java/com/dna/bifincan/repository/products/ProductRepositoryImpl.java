package com.dna.bifincan.repository.products;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dna.bifincan.model.products.Product;
import java.math.BigDecimal;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Product> findLatestActiveProducts(int maxResult) {
        return em.createQuery(
                "select p from Product p where  p.active=true order by p.entryDate desc").setMaxResults(maxResult).getResultList();
    }

    @Override
    public BigDecimal sumMoneyValueOfAllProducts() {
        return (BigDecimal) em.createQuery("select sum(p.stockQuantity * p.priceMoney) from Product p ").getSingleResult();
    }
}
