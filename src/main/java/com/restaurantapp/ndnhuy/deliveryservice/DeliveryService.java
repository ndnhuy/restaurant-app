package com.restaurantapp.ndnhuy.deliveryservice;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class DeliveryService {

  private DeliveryRepository deliveryRepository;

  public Delivery createDelivery(CreateDeliveryRequest request) {
    return deliveryRepository.save(toDeliveryEntity(request));
  }

  private Delivery toDeliveryEntity(CreateDeliveryRequest request) {
    return Delivery.builder()
        .orderId(request.getOrderId())
        .customerId(request.getCustomerId())
        .restaurantId(request.getRestaurantId())
        .status(DeliveryStatus.PENDING)
        .build();
  }

  public void scheduleDelivery(Delivery delivery, LocalDateTime readyBy, Long assignedCourierId) {
    delivery.schedule(readyBy, assignedCourierId);
    deliveryRepository.save(delivery);
  }

  public Optional<Delivery> findDelivery(@NonNull Long deliveryId) {
    log.info("find delivery by {}", deliveryId);
    Assert.isTrue(deliveryId > 0, "must > 0");
    return deliveryRepository.findById(deliveryId);
  }

  public Delivery findDeliveryByOrderId(Long orderId) {
    log.info("find delivery by order id: {}", orderId);
    return deliveryRepository.findByOrderId(orderId)
        .orElseThrow(() -> new DeliveryNotFoundException(orderId));
  }
}
