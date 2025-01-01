package com.restaurantapp.ndnhuy.restaurantservice;

public class TicketNotFoundException extends RuntimeException {
  public TicketNotFoundException(Long orderId) {
    super("ticket not found for order " + orderId);
  }
}
