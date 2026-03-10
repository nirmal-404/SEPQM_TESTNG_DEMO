package com.demo.oms.repository;

import com.demo.oms.model.Product;

import java.util.List;

public interface ProductRepository {

    Product save(Product product);

    Product findById(String productId);

    List<Product> findAll();

    Product update(Product product);

    boolean existsById(String productId);
}
