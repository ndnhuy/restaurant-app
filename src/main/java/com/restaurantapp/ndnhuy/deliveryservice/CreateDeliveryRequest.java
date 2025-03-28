package com.restaurantapp.ndnhuy.deliveryservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateDeliveryRequest {

  private Long orderId;

  private Long customerId;

  private Long restaurantId;

}
