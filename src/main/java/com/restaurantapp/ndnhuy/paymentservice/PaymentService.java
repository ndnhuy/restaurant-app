package com.restaurantapp.ndnhuy.paymentservice;

import com.restaurantapp.ndnhuy.orderservice.Order;
import com.restaurantapp.ndnhuy.orderservice.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {

  private final PaymentRepository paymentRepository;

  private final OrderService orderService;

  private PaymentOrder createPaymentOrder(Order order) {
    var pmtOrder = PaymentOrder.builder()
        .orderId(order.getId())
        .customerId(order.getCustomerId())
        .amount(order.getAmount())
        .status(PaymentStatus.INIT)
        .build();

    return paymentRepository.save(pmtOrder);
  }

  public PaymentOrder createAndPay(Long orderId) {
    log.info("create and pay order {}", orderId);
    var order = orderService.findOrder(orderId).orElseThrow(() -> new PaymentExecption("order not found"));
    var pmtOrder = createPaymentOrder(order);

    pay(pmtOrder.getId());

    orderService.confirmOrderWasPaid(orderId);

    log.info("payment order created and paid: order {}, payment {}", order, pmtOrder);
    return pmtOrder;
  }

  private void pay(Long orderId) {
    paymentRepository.findById(orderId)
        .map(PaymentOrder::approve)
        .ifPresentOrElse(paymentRepository::save, () -> {
          throw new RuntimeException("Order not found");
        });
  }

}
