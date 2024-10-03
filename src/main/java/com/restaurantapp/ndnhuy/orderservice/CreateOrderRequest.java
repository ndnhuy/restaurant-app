package com.restaurantapp.ndnhuy.orderservice;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateOrderRequest {

  long customerId;
  private List<LineItem> lineItems;

  @Getter
  @Builder
  public static class LineItem {

    private Long menuItemId;
    private int quantity;
  }
}
