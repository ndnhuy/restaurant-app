package com.restaurantapp.ndnhuy.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreateCustomerRequest {

  private String firstName;
  private String lastName;
}
