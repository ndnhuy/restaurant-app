package com.restaurantapp.ndnhuy.restaurantservice;

public class TicketNotFound extends RuntimeException {
  public TicketNotFound(Long orderId) {
    super("ticket not found for order " + orderId);
  }
}
