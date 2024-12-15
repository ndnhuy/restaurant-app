package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.common.RequestLineItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateTicketRequest {
  private Long customerId;
  private Long orderId;
  private Long restaurantId;
  private List<RequestLineItem> lineItems;
}
