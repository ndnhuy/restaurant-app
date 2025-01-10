package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class TicketAcceptRequest {

  @NotNull(message = "orderId must not be empty")
  private Long orderId;
}
