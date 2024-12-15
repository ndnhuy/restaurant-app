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

  private Long restaurantId;

  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public Order paid() {
    this.status = OrderStatus.PAID;
    return this;
  }

  public Order accepted() {
    this.status = OrderStatus.ACCEPTED;
    return this;
  }

}
