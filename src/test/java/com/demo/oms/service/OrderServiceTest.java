package com.demo.oms.service;

import com.demo.oms.exception.InvalidOrderException;
import com.demo.oms.exception.PaymentFailedException;
import com.demo.oms.external.EmailService;
import com.demo.oms.model.Order;
import com.demo.oms.model.OrderStatus;
import com.demo.oms.model.PaymentReceipt;
import com.demo.oms.model.Product;
import com.demo.oms.model.User;
import com.demo.oms.repository.InMemoryOrderRepository;
import com.demo.oms.repository.InMemoryProductRepository;
import com.demo.oms.repository.OrderRepository;
import com.demo.oms.repository.ProductRepository;
import com.demo.oms.util.OrderIdGenerator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OrderServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private OrderRepository orderRepository;

    private PaymentService paymentService;
    private EmailService emailService;

    private OrderService orderService;

    private User user;

    @BeforeMethod
    public void setUp() {
        productRepository = new InMemoryProductRepository();
        productService = new ProductService(productRepository);
        orderRepository = new InMemoryOrderRepository();

        paymentService = mock(PaymentService.class);
        emailService = mock(EmailService.class);

        orderService = new OrderService(productService, orderRepository, paymentService, emailService, new OrderIdGenerator());

        productService.addProduct(new Product("P-100", "Mouse", "Wireless", new BigDecimal("19.99"), 10));
        productService.addProduct(new Product("P-200", "Keyboard", "Mechanical", new BigDecimal("59.99"), 5));

        user = new User("USR-1", "Alice", "alice@example.com", "alice", "pass123");
    }

    @Test
    public void shouldRejectCheckoutForEmptyOrder() {
        Order order = orderService.createOrder(user);

        Assert.expectThrows(InvalidOrderException.class, () -> orderService.checkout(order));
    }

    @Test
    public void shouldCheckoutSuccessfullyWhenPaymentSucceeds() {
        Order order = orderService.createOrder(user);
        orderService.addItemToOrder(order, "P-100", 2);
        orderService.addItemToOrder(order, "P-200", 1);

        PaymentReceipt receipt = new PaymentReceipt("RCT-1", order.getOrderId(), new BigDecimal("99.97"), true, "OK", Instant.now());
        doReturn(receipt).when(paymentService).processPayment(any(Order.class));

        PaymentReceipt result = orderService.checkout(order);

        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(order.getStatus(), OrderStatus.CONFIRMED);
        Assert.assertEquals(productService.getProductById("P-100").getStockQuantity(), 8);
        Assert.assertEquals(productService.getProductById("P-200").getStockQuantity(), 4);
        verify(emailService, times(1)).sendOrderConfirmation(user, order);
    }

    @Test
    public void shouldNotReduceStockWhenPaymentFails() {
        Order order = orderService.createOrder(user);
        orderService.addItemToOrder(order, "P-100", 2);

        doThrow(new PaymentFailedException("Card declined")).when(paymentService).processPayment(any(Order.class));

        Assert.expectThrows(PaymentFailedException.class, () -> orderService.checkout(order));

        Assert.assertEquals(order.getStatus(), OrderStatus.FAILED);
        Assert.assertEquals(productService.getProductById("P-100").getStockQuantity(), 10);
        verify(emailService, never()).sendOrderConfirmation(any(User.class), any(Order.class));
    }

    @Test
    public void shouldSendConfirmationEmailOnlyAfterSuccessfulCheckout() {
        Order order = orderService.createOrder(user);
        orderService.addItemToOrder(order, "P-100", 1);

        PaymentReceipt receipt = new PaymentReceipt("RCT-2", order.getOrderId(), new BigDecimal("19.99"), true, "OK", Instant.now());
        doReturn(receipt).when(paymentService).processPayment(any(Order.class));

        orderService.checkout(order);

        verify(emailService, times(1)).sendOrderConfirmation(user, order);
    }

    @Test
    public void shouldMarkOrderAsFailedWhenPaymentFails() {
        Order order = orderService.createOrder(user);
        orderService.addItemToOrder(order, "P-200", 1);

        doThrow(new PaymentFailedException("Insufficient funds")).when(paymentService).processPayment(any(Order.class));

        Assert.expectThrows(PaymentFailedException.class, () -> orderService.checkout(order));

        Assert.assertEquals(order.getStatus(), OrderStatus.FAILED);
    }
}
