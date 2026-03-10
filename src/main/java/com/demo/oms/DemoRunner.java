package com.demo.oms;

import com.demo.oms.external.EmailService;
import com.demo.oms.model.Order;
import com.demo.oms.model.PaymentReceipt;
import com.demo.oms.model.Product;
import com.demo.oms.model.User;
import com.demo.oms.repository.InMemoryOrderRepository;
import com.demo.oms.repository.InMemoryProductRepository;
import com.demo.oms.repository.InMemoryUserRepository;
import com.demo.oms.repository.OrderRepository;
import com.demo.oms.repository.ProductRepository;
import com.demo.oms.repository.UserRepository;
import com.demo.oms.service.AuthService;
import com.demo.oms.service.OrderService;
import com.demo.oms.service.PaymentService;
import com.demo.oms.service.ProductService;
import com.demo.oms.util.OrderIdGenerator;

import java.math.BigDecimal;

public class DemoRunner {

    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository();
        ProductRepository productRepository = new InMemoryProductRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();

        EmailService emailService = new EmailService();

        AuthService authService = new AuthService(userRepository, emailService);
        ProductService productService = new ProductService(productRepository);
        PaymentService paymentService = new PaymentService();
        OrderService orderService = new OrderService(productService, orderRepository, paymentService, emailService, new OrderIdGenerator());

        productService.addProduct(new Product("P-100", "Wireless Mouse", "Ergonomic mouse", new BigDecimal("19.99"), 10));
        productService.addProduct(new Product("P-200", "Mechanical Keyboard", "Blue switches", new BigDecimal("59.99"), 5));

        User user = new User(null, "Alice Perera", "alice@example.com", "alice", "pass123");
        authService.register(user);

        User loggedIn = authService.login("alice", "pass123");

        Order order = orderService.createOrder(loggedIn);
        orderService.addItemToOrder(order, "P-100", 2);
        orderService.addItemToOrder(order, "P-200", 1);

        PaymentReceipt receipt = orderService.checkout(order);

        System.out.println("\n=== ORDER SUMMARY ===");
        System.out.println(order);
        System.out.println("\n=== PAYMENT RECEIPT ===");
        System.out.println(receipt);
    }
}
