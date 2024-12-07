package com.restaurantapp.ndnhuy.orderservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateOrderRequest {

  private Long customerId;

  private List<LineItem> lineItems;

  @Getter
  @Builder
  public static class LineItem {

    private Long menuItemId;
    private int quantity;
  }
}
