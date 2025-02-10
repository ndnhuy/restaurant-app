package com.restaurantapp.ndnhuy.orderservice;

public enum OrderStatus {
  // order is placed
  INIT,
  // order is created
  CREATED,
  // order is paid
  PAID,
  // order is accepted by restaurant and scheduled for delivery
  ACCEPTED,
  // order is rejected by restaurant or no drivers found
  REJECTED,
  // restaurant completes the order
  READY_TO_PICK,
  // driver picked up
  PICKED_UP,
  // driver is delivering the order
  DELIVERING,
  // driver delivered the order
  DELIVERED;
}
