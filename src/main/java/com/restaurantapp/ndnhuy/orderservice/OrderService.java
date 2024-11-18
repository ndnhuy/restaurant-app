package com.restaurantapp.ndnhuy.orderservice;

import java.util.Optional;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderService {

    private OrderRepository orderRepository;

    private MeterRegistry meterRegistry;

    @Value("${test.sleep-in-ms}")
    private Long sleepInMs;

    @Value("${test.process-in-ms}")
    private Long processInMs;

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

    @SneakyThrows
    public void testCreateOrder(OrderStatus status, long waitTimeInMs, long processTimeInMs) {
        log.info("start - cores: {}",  Runtime.getRuntime().availableProcessors());
        Thread.sleep(waitTimeInMs);
        busyInMs(processTimeInMs);
        var counter = Counter.builder("api_order_create")
                .tag("status", status.toString())
                .description("order status")
                .register(meterRegistry);
        counter.increment();
    }

    private void busyInMs(long ms) {
        long endTime = System.currentTimeMillis() + ms;
        var cnt = 0.0;
        while (System.currentTimeMillis() < endTime) {
            cnt += Math.sqrt(Math.random());
        }
        // trick jvm to not  optimize away the busy loop
        blackhole(cnt);
    }

    private void blackhole(double d) {
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
