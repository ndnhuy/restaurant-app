package com.restaurantapp.ndnhuy.common.events;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@RequiredArgsConstructor
@Getter
@ToString
public class TicketWasAccepted implements DomainEvent {

  private final Long ticketId;

  private final Long orderId;

}
