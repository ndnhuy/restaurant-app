package com.restaurantapp.ndnhuy.orderservice;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Entity
@Table(name = "order_events")
@Getter
@Setter
public class OrderEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long orderId;

  private EventCode eventCode;

}
