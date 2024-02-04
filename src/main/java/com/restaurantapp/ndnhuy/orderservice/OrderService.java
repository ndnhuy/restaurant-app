package com.restaurantapp.ndnhuy.orderservice;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@AllArgsConstructor
@Service
public class OrderService {

  private OrderRepository orderRepository;

  @Transactional
  public Order createOrder(long customerId) {
    if (customerId == 0) {
      throw new IllegalArgumentException("invalid customer id");
    }
    var order = new Order();
    order.setStatus(OrderStatus.INIT);
    order.setCustomerId(customerId);
    orderRepository.save(order);
    return order;
  }

  public Optional<Order> findOrder(long orderId) {
    return orderRepository.findById(orderId);
  }
}
