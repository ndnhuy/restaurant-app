package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.RequestLineItem;
import com.restaurantapp.ndnhuy.common.events.OrderCreatedEvent;
import com.restaurantapp.ndnhuy.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class})
@ActiveProfiles("test")
class OrderCreatedHandlerTest {

  @Autowired
  private OrderCreatedHandler orderCreatedHandler;

  @Autowired
  private TicketRepository ticketRepository;

  @Test
  void testOrderCreatedHandler() {
    var orderId = RandomUtils.randomPositiveLong();
    orderCreatedHandler.on(OrderCreatedEvent.builder()
        .orderId(orderId)
        .restaurantId(RandomUtils.randomPositiveLong())
        .customerId(RandomUtils.randomPositiveLong())
        .lineItems(List.of(RequestLineItem.builder()
            .menuItemId(1L)
            .quantity(10)
            .build()))
        .build());

    var ticket = ticketRepository.findByOrderId(orderId);
    assertThat(ticket).isPresent();
  }
}