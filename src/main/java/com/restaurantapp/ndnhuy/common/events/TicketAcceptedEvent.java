package com.restaurantapp.ndnhuy.common.events;

import com.restaurantapp.ndnhuy.common.RequestLineItem;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Builder
@RequiredArgsConstructor
@Getter
@ToString
public class TicketAcceptedEvent {

  private final Long ticketId;

  private final Long orderId;

}
