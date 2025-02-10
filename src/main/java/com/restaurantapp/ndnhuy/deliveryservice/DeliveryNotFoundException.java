package com.restaurantapp.ndnhuy.deliveryservice;

public class DeliveryNotFoundException extends RuntimeException {
  public DeliveryNotFoundException(Long orderId) {
    super("Delivery not found for order id: " + orderId);
  }
}
