package com.demo.oms.service;

import com.demo.oms.exception.InsufficientStockException;
import com.demo.oms.exception.ProductNotFoundException;
import com.demo.oms.model.Product;
import com.demo.oms.repository.InMemoryProductRepository;
import com.demo.oms.repository.ProductRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class ProductServiceTest {

    private ProductService productService;

    @BeforeMethod
    public void setUp() {
        ProductRepository productRepository = new InMemoryProductRepository();
        productService = new ProductService(productRepository);

        productService.addProduct(new Product("P-100", "Mouse", "Wireless", new BigDecimal("19.99"), 10));
    }

    @Test
    public void shouldAddProductSuccessfully() {
        Product added = productService.addProduct(new Product("P-200", "Keyboard", "Mechanical", new BigDecimal("59.99"), 5));

        Assert.assertEquals(added.getProductId(), "P-200");
        Assert.assertEquals(productService.getProductById("P-200").getName(), "Keyboard");
    }

    @Test
    public void shouldFetchProductById() {
        Product fetched = productService.getProductById("P-100");

        Assert.assertEquals(fetched.getName(), "Mouse");
        Assert.assertEquals(fetched.getStockQuantity(), 10);
    }

    @Test
    public void shouldFailWhenProductDoesNotExist() {
        Assert.expectThrows(ProductNotFoundException.class, () -> productService.getProductById("NOPE"));
    }

    @Test
    public void shouldReduceStockSuccessfully() {
        productService.reduceStock("P-100", 3);

        Assert.assertEquals(productService.getProductById("P-100").getStockQuantity(), 7);
    }

    @Test
    public void shouldThrowWhenReducingStockBeyondAvailableQuantity() {
        Assert.expectThrows(InsufficientStockException.class, () -> productService.reduceStock("P-100", 999));
    }
}
