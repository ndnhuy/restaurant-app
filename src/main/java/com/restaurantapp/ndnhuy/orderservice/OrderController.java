package com.restaurantapp.ndnhuy.orderservice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private OrderService orderService;

    @PostMapping
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest req) {
        var order = orderService.createOrder(req.getCustomerId());
        return CreateOrderResponse.builder().orderId(order.getId()).build();
    }

    @PostMapping("/create/{status}")
    public CreateOrderResponse testCreateOrder(@PathVariable OrderStatus status) {
        var order = orderService.testCreateOrder(status);
        return CreateOrderResponse.builder().orderId(order.getId()).build();
    }

    @PostMapping("/confirm/{orderId}/{status}")
    public ResponseEntity<Order> confirmOrder(@PathVariable long orderId, OrderStatus status) {
        var orderMaybe = orderService.confirmOrder(orderId, status);
        return orderMaybe
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable long orderId) {
        return orderService
                .findOrder(orderId)
                .map(order -> new ResponseEntity<>(newGetOrderResponse(order), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private GetOrderResponse newGetOrderResponse(Order order) {
        return GetOrderResponse
                .builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .build();
    }
}
