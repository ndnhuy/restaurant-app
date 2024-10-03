package com.restaurantapp.ndnhuy.customerservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class CreateCustomerRequest {

  @NotBlank
  @NotNull
  private String firstName;

  @NotBlank
  @NotNull
  private String lastName;
}
