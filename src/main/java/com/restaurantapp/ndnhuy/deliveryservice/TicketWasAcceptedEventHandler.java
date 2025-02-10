package com.restaurantapp.ndnhuy.deliveryservice;

import com.restaurantapp.ndnhuy.common.events.TicketWasAccepted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketWasAcceptedEventHandler {

  private final DeliveryService deliveryService;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @EventListener
  void on(TicketWasAccepted event) {
    log.info("handle event: {}", event);

    var delivery = deliveryService.findDeliveryByOrderId(event.getOrderId());
    var readyBy = LocalDateTime.now().plusHours(1); //TODO should get readyBy from TicketWasAccepted event
    var assignedCourierId = 1L; //TODO should get available courier from CourierService
    deliveryService.scheduleDelivery(delivery, readyBy, assignedCourierId);
  }
}
