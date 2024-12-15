package com.restaurantapp.ndnhuy.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestLineItem {

  private Long menuItemId;
  private int quantity;
}
