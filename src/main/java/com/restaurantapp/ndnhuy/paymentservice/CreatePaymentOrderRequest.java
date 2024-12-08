package com.restaurantapp.ndnhuy.paymentservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreatePaymentOrderRequest {

  private Long orderId;

}
