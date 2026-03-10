package com.demo.oms.repository;

import com.demo.oms.model.Order;
import com.demo.oms.model.User;

import java.util.List;

public interface OrderRepository {

    Order save(Order order);

    Order findById(String orderId);

    List<Order> findByUser(User user);

    List<Order> findAll();
}
