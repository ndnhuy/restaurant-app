package com.restaurantapp.ndnhuy.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateCustomerResponse {

    private Long customerId;

    private String firstName;

    private String lastName;
}
