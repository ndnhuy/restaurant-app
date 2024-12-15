package com.restaurantapp.ndnhuy.orderservice;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(Long orderId) {
    super("order not found: " + orderId);
  }
}
