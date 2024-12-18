package com.restaurantapp.ndnhuy.restaurantservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
  Optional<Ticket> findByOrderId(Long orderId);
}
