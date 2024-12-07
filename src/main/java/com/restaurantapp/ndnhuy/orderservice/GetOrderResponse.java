package com.restaurantapp.ndnhuy.orderservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class GetOrderResponse {
  private Long orderId;
  private Long customerId;
  private OrderStatus status;
  private BigDecimal amount;
}
