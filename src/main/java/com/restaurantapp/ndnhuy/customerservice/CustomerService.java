package com.restaurantapp.ndnhuy.customerservice;

import java.util.Optional;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@Setter
@Service
@Slf4j
public class CustomerService {

    private CustomerRepository customerRepository;

    public Optional<Customer> getCustomer(long customerId) {
        Assert.isTrue(customerId > 0, "invalid customerId");
        return customerRepository.findById(customerId);
    }

    public Customer createCustomer(@Valid CreateCustomerRequest request) {
        log.info("create customer: {}", request);
        return customerRepository.save(
                Customer.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .build()
        );
    }
}
