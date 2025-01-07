package com.restaurantapp.ndnhuy.restaurantservice;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class TicketDto {

  private Long id;

  private Long customerId;

  private Long orderId;

  private Long restaurantId;

  private TicketStatus status;

  private List<TicketLineItem> lineItems;
}
