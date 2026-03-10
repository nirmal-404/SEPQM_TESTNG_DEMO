package com.demo.oms.external;

/**
 * Mock Payment Gateway interface for demonstration
 */
public interface PaymentGateway {
    String processPayment(String cardNumber, java.math.BigDecimal amount);
    boolean refundPayment(String paymentId);
}
