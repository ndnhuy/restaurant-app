package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TicketLineItem {

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "menu_item_id")
  private MenuItem menuItem;

  private int quantity;
}
