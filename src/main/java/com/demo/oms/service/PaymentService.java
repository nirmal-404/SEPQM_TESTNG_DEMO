package com.demo.oms.service;

import com.demo.oms.exception.PaymentFailedException;
import com.demo.oms.model.Order;
import com.demo.oms.model.OrderItem;
import com.demo.oms.model.PaymentReceipt;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class PaymentService {

    public PaymentReceipt processPayment(Order order) {
        if (order == null) {
            throw new PaymentFailedException("Order must not be null");
        }

        BigDecimal amount = order.getTotalAmount();
        if (amount == null) {
            amount = calculateFromItems(order);
            order.setTotalAmount(amount);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentFailedException("Payment rejected: amount must be > 0");
        }

        return new PaymentReceipt(
                "RCT-" + UUID.randomUUID(),
                order.getOrderId(),
                amount,
                true,
                "Payment approved",
                Instant.now()
        );
    }

    private static BigDecimal calculateFromItems(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        if (order.getItems() == null) {
            return total;
        }

        for (OrderItem item : order.getItems()) {
            if (item == null) {
                continue;
            }
            BigDecimal subtotal = item.getSubtotal();
            if (subtotal != null) {
                total = total.add(subtotal);
            }
        }
        return total;
    }
}
