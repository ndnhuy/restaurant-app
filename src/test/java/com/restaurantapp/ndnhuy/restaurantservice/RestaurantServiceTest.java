package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.RequestLineItem;
import com.restaurantapp.ndnhuy.orderservice.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class})
@ActiveProfiles("test")
public class RestaurantServiceTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RestaurantService restaurantService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private TicketRepository ticketRepository;

  @Test
  public void testCreateTicket_shouldBeSuccess() {
    // given a restaurant
    var restaurant = restaurantService.createRestaurant("test", List.of(
        MenuItem.builder()
            .name("menu1")
            .price(BigDecimal.ONE)
            .build(),
        MenuItem.builder()
            .name("menu2")
            .price(BigDecimal.ONE)
            .build()
    ));

    // when create ticket
    var ticketId = restaurantService.createTicket(CreateTicketRequest.builder()
        .orderId(1L)
        .restaurantId(restaurant.getId())
        .customerId(1L)
        .lineItems(restaurant.getMenuItems().stream().map(item -> RequestLineItem.builder()
                .menuItemId(item.getId())
                .quantity(5)
                .build())
            .toList())
        .build()).getId();

    // then
    var ticket = ticketRepository.findById(ticketId);
    assertThat(ticket.get().getRestaurantId()).isEqualTo(restaurant.getId());
    assertThat(ticket.get().getCustomerId()).isEqualTo(1L);
    assertThat(ticket.get().getOrderId()).isEqualTo(1L);
    assertThat(ticket.get().getStatus()).isEqualTo(TicketStatus.CREATED);
    for (int i = 0; i < ticket.get().getLineItems().size(); i++) {
      var lineItem = ticket.get().getLineItems().get(i);
      assertThat(lineItem.getQuantity()).isEqualTo(5);
      assertThat(lineItem.getMenuItem().getId()).isEqualTo(restaurant.getMenuItems().get(i).getId());
    }
  }

  @Test
  void testFindTicketByOrderId() {
    // given a restaurant
    var restaurant = restaurantService.createRestaurant("test", List.of(
        MenuItem.builder()
            .name("menu1")
            .price(BigDecimal.ONE)
            .build(),
        MenuItem.builder()
            .name("menu2")
            .price(BigDecimal.ONE)
            .build()
    ));

    // given a ticket
    var orderId = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
    restaurantService.createTicket(CreateTicketRequest.builder()
        .orderId(orderId)
        .restaurantId(restaurant.getId())
        .customerId(1L)
        .lineItems(restaurant.getMenuItems().stream().map(item -> RequestLineItem.builder()
                .menuItemId(item.getId())
                .quantity(5)
                .build())
            .toList())
        .build()).getId();

    // when
    var ticketDto = restaurantService.findTicketByOrderId(orderId);
    assertThat(ticketDto.getRestaurantId()).isEqualTo(restaurant.getId());
    assertThat(ticketDto.getOrderId()).isEqualTo(orderId);
    assertThat(ticketDto.getStatus()).isEqualTo(TicketStatus.CREATED);
    assertThat(ticketDto.getCustomerId()).isEqualTo(1L);
    assertThat(ticketDto.getLineItems().size()).isEqualTo(restaurant.getMenuItems().size());
    for (var i = 0; i < ticketDto.getLineItems().size(); i++) {
      assertThat(ticketDto.getLineItems().get(i).getMenuItem().getId()).isEqualTo(restaurant.getMenuItems().get(i).getId());
      assertThat(ticketDto.getLineItems().get(i).getQuantity()).isEqualTo(5);
    }
  }
}
