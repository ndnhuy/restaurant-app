package com.restaurantapp.ndnhuy.orderservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  private Long customerId;

  @NotNull
  private Long restaurantId;

  @NotNull
  private List<LineItem> lineItems;

  @Getter
  @Builder
  public static class LineItem {

    private Long menuItemId;
    private int quantity;
  }
}
