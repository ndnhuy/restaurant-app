package com.restaurantapp.ndnhuy.deliveryservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateDeliveryResponse {

  private Long deliveryId;

  private Long customerId;

  private Long orderId;

  private Long restaurantId;

  private DeliveryStatus status;

}
