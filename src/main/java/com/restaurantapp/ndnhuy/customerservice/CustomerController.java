package com.restaurantapp.ndnhuy.customerservice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

  private CustomerService customerService;

  @GetMapping(path = "/{customerId}")
  public ResponseEntity<GetCustomerResponse> getCustomer(
    @PathVariable long customerId
  ) {
    return customerService
      .getCustomer(customerId)
      .map(c ->
        ResponseEntity.ok(
          GetCustomerResponse
            .builder()
            .customerId(c.getId())
            .firtName(c.getFirstName())
            .lastName(c.getLastName())
            .build()
        )
      )
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public CreateCustomerResponse createCustomer(
    @RequestBody CreateCustomerRequest req
  ) {
    var cust = customerService.createCustomer(
      req.getFirstName(),
      req.getLastName()
    );
    return CreateCustomerResponse.builder().customerId(cust.getId()).build();
  }
}
