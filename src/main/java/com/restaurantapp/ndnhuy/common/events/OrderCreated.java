package com.restaurantapp.ndnhuy.common.events;

import com.restaurantapp.ndnhuy.common.RequestLineItem;
import com.restaurantapp.ndnhuy.restaurantservice.TicketLineItem;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Builder
@RequiredArgsConstructor
@Getter
@ToString
public class OrderCreated {

  private final Long orderId;

  private final Long customerId;

  private final Long restaurantId;

  private final List<RequestLineItem> lineItems;

}