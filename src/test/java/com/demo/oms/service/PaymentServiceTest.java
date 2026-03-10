package com.demo.oms.service;

import com.demo.oms.exception.PaymentFailedException;
import com.demo.oms.model.Order;
import com.demo.oms.model.OrderItem;
import com.demo.oms.model.PaymentReceipt;
import com.demo.oms.model.Product;
import com.demo.oms.model.User;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class PaymentServiceTest {

    @Test
    public void shouldProcessPaymentSuccessfullyForPositiveAmount() {
        PaymentService paymentService = new PaymentService();

        Order order = new Order("ORD-1", new User("USR-1", "Alice", "alice@example.com", "alice", "pass"));
        order.addItem(new OrderItem(new Product("P-1", "Item", "", new BigDecimal("10.00"), 1), 2));
        order.calculateTotal();

        PaymentReceipt receipt = paymentService.processPayment(order);

        Assert.assertTrue(receipt.isSuccess());
        Assert.assertEquals(receipt.getOrderId(), "ORD-1");
        Assert.assertEquals(receipt.getAmount(), new BigDecimal("20.00"));
    }

    @Test
    public void shouldFailPaymentWhenAmountIsZeroOrLess() {
        PaymentService paymentService = new PaymentService();

        Order order = new Order("ORD-2", new User("USR-2", "Bob", "bob@example.com", "bob", "pass"));
        order.setTotalAmount(BigDecimal.ZERO);

        Assert.expectThrows(PaymentFailedException.class, () -> paymentService.processPayment(order));
    }
}
