package com.demo.oms.repository;

import com.demo.oms.model.Order;
import com.demo.oms.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> ordersById = new HashMap<>();

    @Override
    public Order save(Order order) {
        if (order == null || order.getOrderId() == null) {
            throw new IllegalArgumentException("order and orderId must not be null");
        }
        ordersById.put(order.getOrderId(), order);
        return order;
    }

    @Override
    public Order findById(String orderId) {
        return ordersById.get(orderId);
    }

    @Override
    public List<Order> findByUser(User user) {
        List<Order> result = new ArrayList<>();
        for (Order order : ordersById.values()) {
            if (order != null && user != null && user.equals(order.getUser())) {
                result.add(order);
            }
        }
        return result;
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(ordersById.values());
    }
}
