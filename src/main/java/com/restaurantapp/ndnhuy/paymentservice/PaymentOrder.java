package com.restaurantapp.ndnhuy.paymentservice;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Entity
@Table(name = "ra_payment_order")
@Getter
@Setter
public class PaymentOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long orderId;

  private Long customerId;

  private BigDecimal amount;


  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  public PaymentOrder approve() {
    this.status = PaymentStatus.SUCCESS;
    return this;
  }

  public PaymentOrder reject() {
    this.status = PaymentStatus.FAIL;
    return this;
  }


}
