package com.demo.oms.service;

import com.demo.oms.exception.InvalidOrderException;
import com.demo.oms.exception.PaymentFailedException;
import com.demo.oms.external.EmailService;
import com.demo.oms.model.Order;
import com.demo.oms.model.OrderItem;
import com.demo.oms.model.OrderStatus;
import com.demo.oms.model.PaymentReceipt;
import com.demo.oms.model.Product;
import com.demo.oms.model.User;
import com.demo.oms.repository.OrderRepository;
import com.demo.oms.util.OrderIdGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final OrderIdGenerator orderIdGenerator;

    public OrderService(
            ProductService productService,
            OrderRepository orderRepository,
            PaymentService paymentService,
            EmailService emailService,
            OrderIdGenerator orderIdGenerator
    ) {
        this.productService = Objects.requireNonNull(productService, "productService must not be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "orderRepository must not be null");
        this.paymentService = Objects.requireNonNull(paymentService, "paymentService must not be null");
        this.emailService = Objects.requireNonNull(emailService, "emailService must not be null");
        this.orderIdGenerator = Objects.requireNonNull(orderIdGenerator, "orderIdGenerator must not be null");
    }

    public Order createOrder(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        Order order = new Order(orderIdGenerator.nextId(), user);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(BigDecimal.ZERO);
        return order;
    }

    public Order addItemToOrder(Order order, String productId, int quantity) {
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }

        Product product = productService.getProductById(productId);
        if (product.getStockQuantity() < quantity) {
            throw new com.demo.oms.exception.InsufficientStockException(
                    "Insufficient stock for product " + productId + ": requested " + quantity + ", available " + product.getStockQuantity()
            );
        }

        OrderItem item = new OrderItem(new Product(product), quantity);
        order.addItem(item);
        return order;
    }

    public BigDecimal calculateTotal(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
        return order.calculateTotal();
    }

    public PaymentReceipt checkout(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
        if (order.getItems().isEmpty()) {
            throw new InvalidOrderException("Order must contain at least one item");
        }

        calculateTotal(order);

        try {
            PaymentReceipt receipt = paymentService.processPayment(order);

            if (receipt == null || !receipt.isSuccess()) {
                order.setStatus(OrderStatus.FAILED);
                return receipt;
            }

            order.setStatus(OrderStatus.PAID);

            for (OrderItem item : order.getItems()) {
                productService.reduceStock(item.getProduct().getProductId(), item.getQuantity());
            }

            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
            emailService.sendOrderConfirmation(order.getUser(), order);

            return receipt;
        } catch (PaymentFailedException ex) {
            order.setStatus(OrderStatus.FAILED);
            throw ex;
        }
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
