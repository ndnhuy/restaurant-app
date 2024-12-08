package com.restaurantapp.ndnhuy.paymentservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;

  public PaymentOrder createOrder(Long customerId, Double amount) {
    var order = PaymentOrder.builder()
        .customerId(customerId)
        .amount(amount)
        .status(PaymentStatus.INIT)
        .build();

    return paymentRepository.save(order);
  }

  public PaymentOrder createAndPay(Long customerId, Double amount) {
    var order = createOrder(customerId, amount);
    pay(order.getId());
    return order;
  }

  public void pay(Long orderId) {
    paymentRepository.findById(orderId)
        .map(PaymentOrder::approve)
        .ifPresentOrElse(paymentRepository::save, () -> {
          throw new RuntimeException("Order not found");
        });
  }

}
