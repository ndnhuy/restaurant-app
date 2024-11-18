package com.restaurantapp.ndnhuy.orderservice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/orders")
@Slf4j
public class OrderController {

    private OrderService orderService;

    @PostMapping
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest req) {
        var order = orderService.createOrder(req.getCustomerId());
        return CreateOrderResponse.builder().orderId(order.getId()).build();
    }

    @PostMapping("/create/{status}")
    @SneakyThrows
    public CreateOrderResponse testCreateOrder(@PathVariable OrderStatus status, @RequestParam Long waitTimeInMs, @RequestParam Long processTimeInMs) {
        long start = System.currentTimeMillis();
        orderService.testCreateOrder(status, Optional.ofNullable(waitTimeInMs).orElse(0L),
                Optional.ofNullable(processTimeInMs).orElse(0L));
        long end = System.currentTimeMillis();
        log.info("testCreateOrder took {} ms", end - start);
        return CreateOrderResponse.builder().build();
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
