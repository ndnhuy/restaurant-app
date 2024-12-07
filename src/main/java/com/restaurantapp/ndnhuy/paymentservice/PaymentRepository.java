package com.restaurantapp.ndnhuy.paymentservice;

import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentOrder, Long> {
}
