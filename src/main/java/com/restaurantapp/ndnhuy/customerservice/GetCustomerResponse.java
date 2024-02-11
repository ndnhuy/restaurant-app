package com.restaurantapp.ndnhuy.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class GetCustomerResponse {

  private long customerId;
  private String firtName;
  private String lastName;
}
