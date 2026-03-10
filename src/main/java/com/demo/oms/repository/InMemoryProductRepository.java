package com.demo.oms.repository;

import com.demo.oms.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> productsById = new HashMap<>();

    @Override
    public Product save(Product product) {
        if (product == null || product.getProductId() == null) {
            throw new IllegalArgumentException("product and productId must not be null");
        }
        productsById.put(product.getProductId(), product);
        return product;
    }

    @Override
    public Product findById(String productId) {
        return productsById.get(productId);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productsById.values());
    }

    @Override
    public Product update(Product product) {
        if (product == null || product.getProductId() == null) {
            throw new IllegalArgumentException("product and productId must not be null");
        }
        productsById.put(product.getProductId(), product);
        return product;
    }

    @Override
    public boolean existsById(String productId) {
        return productsById.containsKey(productId);
    }
}
