package com.restaurantapp.ndnhuy.orderservice;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private MeterRegistry meterRegistry;

    public OrderService(OrderRepository orderRepository, MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.meterRegistry = meterRegistry;
//        Gauge.builder("order_count", orderRepository::count)
//                .description("total number of orders")
//                .register(meterRegistry);
    }

    @Transactional
    public Order createOrder(long customerId) {
        if (customerId == 0) {
            throw new IllegalArgumentException("invalid customer id");
        }
        var order = new Order();
        order.setStatus(OrderStatus.INIT);
        order.setCustomerId(customerId);
        orderRepository.save(order);

        var counter = Counter.builder("api_order_create")
                .tag("status", order.getStatus().toString())
                .description("order status")
                .register(meterRegistry);
        counter.increment();

        return order;
    }

    @Transactional
    public Order testCreateOrder(OrderStatus status) {
        var order = new Order();
        order.setStatus(status);
        order.setCustomerId(ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE));
        orderRepository.save(order);

        var counter = Counter.builder("api_order_create")
                .tag("status", order.getStatus().toString())
                .description("order status")
                .register(meterRegistry);
        counter.increment();

        return order;
    }

    public Optional<Order> confirmOrder(long orderId, OrderStatus status) {
        var orderMaybe = findOrder(orderId);
        if (orderMaybe.isPresent()) {
            var o = orderMaybe.get();
            o.setStatus(status);
            orderRepository.save(o);

            var counter = Counter.builder("api_order_create")
                    .tag("status", o.getStatus().toString())
                    .description("order status")
                    .register(meterRegistry);
            counter.increment();

            return Optional.of(o);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Order> findOrder(long orderId) {
        return orderRepository.findById(orderId);
    }
}
