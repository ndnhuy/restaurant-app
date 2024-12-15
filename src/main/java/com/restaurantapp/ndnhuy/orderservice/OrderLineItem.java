package com.restaurantapp.ndnhuy.orderservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class OrderLineItem {

  private Long menuItemId;

  private String name;

  private BigDecimal price;

  private int quantity;

}
