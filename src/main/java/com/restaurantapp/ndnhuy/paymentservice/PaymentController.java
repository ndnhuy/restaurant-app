package com.restaurantapp.ndnhuy.paymentservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/payments")
@Slf4j
public class PaymentController {

  private PaymentService paymentService;

  @PostMapping("/createAndPay")
  public CreatePaymentOrderResponse createAndPayPaymentOrder(@RequestBody CreatePaymentOrderRequest req) {
    var pmt = paymentService.createAndPay(req.getOrderId());
    return CreatePaymentOrderResponse.builder()
        .id(pmt.getId())
        .orderId(pmt.getOrderId())
        .status(pmt.getStatus())
        .customerId(pmt.getCustomerId())
        .amount(pmt.getAmount())
        .build();
  }
}
