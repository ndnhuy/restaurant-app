package com.restaurantapp.ndnhuy.deliveryservice;

import lombok.*;

import java.time.LocalDateTime;

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
  private LocalDateTime readyBy;
  private Long assignedCourierId;
}
