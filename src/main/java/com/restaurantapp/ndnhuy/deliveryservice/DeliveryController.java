package com.restaurantapp.ndnhuy.deliveryservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/delivery")
@Slf4j
public class DeliveryController {

  private DeliveryService deliveryService;

  @PostMapping
  public ResponseEntity<CreateDeliveryResponse> createDelivery(@RequestBody CreateDeliveryRequest delivery) {
    Delivery createdDelivery = deliveryService.createDelivery(delivery);
    return new ResponseEntity<>(CreateDeliveryResponse.builder()
        .deliveryId(createdDelivery.getId())
        .customerId(createdDelivery.getCustomerId())
        .orderId(createdDelivery.getOrderId())
        .restaurantId(createdDelivery.getRestaurantId())
        .status(createdDelivery.getStatus())
        .build(), HttpStatus.CREATED);
  }

  @GetMapping(path = "/{deliveryId}")
  public ResponseEntity<GetDeliveryResponse> getDelivery(@PathVariable Long deliveryId) {
    return deliveryService.findDelivery(deliveryId)
        .map(delivery -> new ResponseEntity<>(GetDeliveryResponse.builder()
            .deliveryId(deliveryId)
            .orderId(delivery.getOrderId())
            .customerId(delivery.getCustomerId())
            .restaurantId(delivery.getRestaurantId())
            .status(delivery.getStatus())
            .build(), HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}
