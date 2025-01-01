package com.restaurantapp.ndnhuy.orderservice;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class GetOrderResponse {
  private Long id;
  private Long customerId;
  private Long restaurantId;
  private OrderStatus status;
  private BigDecimal amount;
}
