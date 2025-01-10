package com.restaurantapp.ndnhuy.common;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(Long orderId) {
    super("order not found: " + orderId);
  }
}
