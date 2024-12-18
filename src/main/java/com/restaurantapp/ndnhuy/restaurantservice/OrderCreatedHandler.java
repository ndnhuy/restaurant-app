package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.common.events.OrderCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedHandler {

  private final RestaurantService restaurantService;

//  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  void on(OrderCreated event) {
    log.info("handle event: {}", event);
    var ticketId = restaurantService.createTicket(
        CreateTicketRequest.builder()
            .orderId(event.getOrderId())
            .customerId(event.getCustomerId())
            .restaurantId(event.getRestaurantId())
            .lineItems(event.getLineItems())
            .build()
    );

    log.info("ticket created: {}", ticketId);
  }
}
