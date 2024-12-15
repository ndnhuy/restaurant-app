package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AcceptOrderRequest {

  @NotNull(message = "orderId must not be empty")
  private Long orderId;
}
