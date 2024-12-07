package com.restaurantapp.ndnhuy.orderservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ra_order")
@Getter
@Setter
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long customerId;

  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
