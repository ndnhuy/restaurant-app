package com.restaurantapp.ndnhuy.orderservice;

public enum OrderStatus {
  // order is placed
  INIT,
  // order is created
  CREATED,
  // order is paid
  PAID,
  // order is accepted by restaurant and assigned to a driver
  ACCEPTED,
  // order is rejected by restaurant or no drivers found
  REJECTED,
  // restaurant completes the order
  READY_TO_PICK,
  // driver picked up
  PICKED_UP,
  // driver delivered the order
  DELIVERED;
}
