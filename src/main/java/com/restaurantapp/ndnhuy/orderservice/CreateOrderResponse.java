package com.restaurantapp.ndnhuy.orderservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateOrderResponse {

  private Long id;

  private Long customerId;

  private BigDecimal amount;

  private OrderStatus status;
}
