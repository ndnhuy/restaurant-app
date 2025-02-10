package com.restaurantapp.ndnhuy.deliveryservice;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class GetDeliveryResponse {
  private Long deliveryId;
  private Long customerId;
  private Long orderId;
  private Long restaurantId;
  private DeliveryStatus status;
}
