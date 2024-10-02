package com.restaurantapp.ndnhuy.customerservice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreateCustomerRequest {

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;
}
