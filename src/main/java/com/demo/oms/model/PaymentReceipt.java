package com.demo.oms.model;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentReceipt {

    private String receiptId;
    private String orderId;
    private BigDecimal amount;
    private boolean success;
    private String message;
    private Instant timestamp;

    public PaymentReceipt() {
    }

    public PaymentReceipt(String receiptId, String orderId, BigDecimal amount, boolean success, String message, Instant timestamp) {
        this.receiptId = receiptId;
        this.orderId = orderId;
        this.amount = amount;
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PaymentReceipt{" +
                "receiptId='" + receiptId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
