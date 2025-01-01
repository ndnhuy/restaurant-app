package com.restaurantapp.ndnhuy.orderservice;

import com.restaurantapp.ndnhuy.common.events.OrderCreatedEvent;
import com.restaurantapp.ndnhuy.common.events.TicketAcceptedEvent;
import com.restaurantapp.ndnhuy.restaurantservice.CreateTicketRequest;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketAcceptedEventHandler {

  private final OrderService orderService;

  //  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  void on(TicketAcceptedEvent event) {
    log.info("handle event: {}", event);

    var order = orderService.findOrder(event.getOrderId())
        .map(Order::accepted)
        .orElseThrow(() -> new OrderNotFoundException(event.getOrderId()));

    orderService.save(order);

    log.info("order accepted: {}", order);
  }
}
