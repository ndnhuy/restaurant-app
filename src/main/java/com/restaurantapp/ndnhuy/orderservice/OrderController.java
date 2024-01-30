package com.restaurantapp.ndnhuy.orderservice;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Service
@RequestMapping(path = "/orders")
public class OrderController {

  private OrderRepository orderRepository;

  @PostMapping
  public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest req) {
    var order = new Order();
    order.setStatus("init");
    order.setCustomerId(req.getCustomerId());
    orderRepository.save(order);
    return CreateOrderResponse.builder().orderId(order.getId()).build();
  }
}
