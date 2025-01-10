package com.restaurantapp.ndnhuy.orderservice;

import com.restaurantapp.ndnhuy.common.OrderNotFoundException;
import com.restaurantapp.ndnhuy.common.events.TicketWasAccepted;
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
public class TicketWasAcceptedEventHandler {

  private final OrderService orderService;

  private final OrderEventRepository orderEventRepository;

  //  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  void on(TicketWasAccepted event) {
    log.info("handle event: {}", event);

    var order = orderService.findOrder(event.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException(event.getOrderId()));

    orderEventRepository.save(OrderEvent.builder()
        .eventCode(EventCode.TICKET_WAS_ACCEPTED)
        .orderId(order.getId())
        .build());
  }
}
