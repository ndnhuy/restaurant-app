package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class AcceptOrderRequest {

  @NotNull(message = "orderId must not be empty")
  private Long orderId;
}
