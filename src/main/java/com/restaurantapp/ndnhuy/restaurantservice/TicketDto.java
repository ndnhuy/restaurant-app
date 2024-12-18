package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TicketDto {

  private Long id;

  private Long customerId;

  private Long orderId;

  private Long restaurantId;

  private TicketStatus status;

  private List<TicketLineItem> lineItems;
}
