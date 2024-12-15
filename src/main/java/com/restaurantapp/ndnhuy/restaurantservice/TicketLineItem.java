package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.persistence.Access;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketLineItem {

  private Long menuItemId;

  private int quantity;
}
