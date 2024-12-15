package com.restaurantapp.ndnhuy.orderservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateOrderResponse {

  private Long id;

  private Long customerId;

  private Long restaurantId;

  private BigDecimal amount;

  private OrderStatus status;

  private List<OrderLineItem> orderLineItems;
}
