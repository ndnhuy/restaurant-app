package com.restaurantapp.ndnhuy.deliveryservice;

import com.restaurantapp.ndnhuy.common.events.OrderWasCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component("deliveryOrderCreatedHandler")
public class OrderCreatedHandler {

  private final DeliveryService deliveryService;

  @EventListener
  void on(OrderWasCreated event) {
    log.info("handle event: {}", event);

    var delivery = deliveryService.createDelivery(CreateDeliveryRequest.builder()
        .orderId(event.getOrderId())
        .customerId(event.getCustomerId())
        .restaurantId(event.getRestaurantId())
        .build());


    log.info("delivery created: {}", delivery);
  }
}
