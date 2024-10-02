package com.restaurantapp.ndnhuy.customerservice;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@AllArgsConstructor
@Setter
@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public Optional<Customer> getCustomer(long customerId) {
        Assert.isTrue(customerId > 0, "invalid customerId");
        return customerRepository.findById(customerId);
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        return customerRepository.save(
                Customer.builder().firstName(request.getFirstName()).lastName(request.getLastName()).build()
        );
    }
}
