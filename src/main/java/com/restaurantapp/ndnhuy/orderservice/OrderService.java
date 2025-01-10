package com.restaurantapp.ndnhuy.orderservice;

import com.restaurantapp.ndnhuy.common.OrderNotFoundException;
import com.restaurantapp.ndnhuy.common.RequestLineItem;
import com.restaurantapp.ndnhuy.common.events.OrderWasCreated;
import com.restaurantapp.ndnhuy.restaurantservice.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {

  private OrderRepository orderRepository;

  @Lazy
  private RestaurantService restaurantService;

  private MeterRegistry meterRegistry;

  private ApplicationEventPublisher eventPublisher;

  @Transactional
  public Order createOrder(@NonNull CreateOrderRequest request) {
    if (request.getCustomerId() == 0) {
      throw new IllegalArgumentException("invalid customer id");
    }

    var menuIds = request.getLineItems()
        .stream()
        .map(RequestLineItem::getMenuItemId)
        .toList();
    var menuItems = restaurantService.findMenuItems(menuIds);
    if (CollectionUtils.isEmpty(menuItems)) {
      throw new MenuItemsNotFoundException(menuIds);
    }

    var invalidMenuIds = menuItems.stream().filter(item -> !item.getRestaurantId().equals(request.getRestaurantId()))
        .map(item -> RestaurantDTO.MenuItemDTO.builder()
            .id(item.getId())
            .price(item.getPrice())
            .name(item.getName())
            .build())
        .toList();
    if (!CollectionUtils.isEmpty(invalidMenuIds)) {
      throw new InvalidMenuItemsException(invalidMenuIds, "menu items not belong to restaurant " + request.getRestaurantId());
    }

    var priceById = menuItems.stream().collect(Collectors.toMap(MenuItem::getId, MenuItem::getPrice));
    var amount = request.getLineItems().stream()
        .map(item -> priceById.get(item.getMenuItemId()).multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal::add)
        .orElseThrow(() -> new GeneralException("error calculate order amount"));

    var order = new Order();
    order.setStatus(OrderStatus.CREATED);
    order.setCustomerId(request.getCustomerId());
    order.setAmount(amount);
    order.setRestaurantId(request.getRestaurantId());

    orderRepository.save(order);

    log.info("order created: {}", order);

    eventPublisher.publishEvent(OrderWasCreated.builder()
        .orderId(order.getId())
        .customerId(order.getCustomerId())
        .restaurantId(order.getRestaurantId())
        .lineItems(request.getLineItems())
        .build());

    return order;
  }

  public void confirmOrderWasPaid(Long orderId) {
    var order = findOrder(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    orderRepository.save(order.paid());
  }

  // this is dummy method used for testing grafana only
  @SneakyThrows
  public void testCreateOrder(OrderStatus status, long waitTimeInMs, long processTimeInMs) {
    log.info("start - cores: {}", Runtime.getRuntime().availableProcessors());
    Thread.sleep(waitTimeInMs);
    busyInMs(processTimeInMs);
    var counter = Counter.builder("api_order_create")
        .tag("status", status.toString())
        .description("order status")
        .register(meterRegistry);
    counter.increment();
  }

  private void busyInMs(long busyTimeInMs) {
    var cnt = 0.0;
    // a magic number 60000 to make sure CPU takes as long as <busyTimInMs> to complete the task
    // for example: if busyTimeInMs=1000, busyTimeInMs*60000 will make sure it process to approximately 1000ms
    // this magic number may vary depending on the machine
    for (var i = 0; i < busyTimeInMs * 60000; i++) {
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

  public void save(Order order) {
    orderRepository.save(order);
  }
}
