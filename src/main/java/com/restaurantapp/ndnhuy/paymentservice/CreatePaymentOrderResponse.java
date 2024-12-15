package com.restaurantapp.ndnhuy.paymentservice;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreatePaymentOrderResponse {

  private Long id;

  private Long orderId;

  private Long customerId;

  private BigDecimal amount;

  private PaymentStatus status;

}
