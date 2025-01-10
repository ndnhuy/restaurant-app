package com.restaurantapp.ndnhuy.orderservice;

import org.springframework.data.repository.CrudRepository;

public interface OrderEventRepository extends CrudRepository<OrderEvent, Long> {
}
