package com.demo.oms.service;

import com.demo.oms.exception.InsufficientStockException;
import com.demo.oms.exception.ProductNotFoundException;
import com.demo.oms.model.Product;
import com.demo.oms.repository.ProductRepository;

import java.util.List;
import java.util.Objects;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository, "productRepository must not be null");
    }

    public Product addProduct(Product product) {
        if (product == null || isBlank(product.getProductId())) {
            throw new IllegalArgumentException("product and productId are required");
        }
        if (productRepository.existsById(product.getProductId())) {
            throw new IllegalArgumentException("Product already exists: " + product.getProductId());
        }
        return productRepository.save(product);
    }

    public Product getProductById(String productId) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }
        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateStock(String productId, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("newQuantity must be >= 0");
        }
        Product product = getProductById(productId);
        product.setStockQuantity(newQuantity);
        return productRepository.update(product);
    }

    public Product reduceStock(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        Product product = getProductById(productId);
        if (product.getStockQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product " + productId + ": requested " + quantity + ", available " + product.getStockQuantity());
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        return productRepository.update(product);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
