package com.restaurantapp.ndnhuy.deliveryservice;

import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.DeliveryHelper;
import com.restaurantapp.ndnhuy.common.OrderHelper;
import com.restaurantapp.ndnhuy.common.TestFactory;
import com.restaurantapp.ndnhuy.common.events.TicketWasAccepted;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class, TestFactory.class})
@ActiveProfiles("test")
class DeliveryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private DeliveryHelper deliveryHelper;

  @Autowired
  private OrderHelper orderHelper;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Test
  @SneakyThrows
  void testCreateDelivery() {
    // given
    var orderId = orderHelper.givenValidOrderId();
    var order = orderHelper.getOrder(orderId);

    // when
    var deliveryId = deliveryHelper.getResourceId(
        deliveryHelper.createResource(
            CreateDeliveryRequest.builder()
                .orderId(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .build(),
            rs -> rs.andExpect(status().isCreated()),
            deliveryHelper::getReponseAsJson
        )
    );

    // then
    deliveryHelper.getResourceById(deliveryId, rs -> rs
        .andExpect(status().isOk())
        .andExpect(jsonPath("deliveryId").value(deliveryId))
        .andExpect(jsonPath("customerId").value(order.getCustomerId()))
        .andExpect(jsonPath("orderId").value(order.getId()))
        .andExpect(jsonPath("restaurantId").value(order.getRestaurantId()))
        .andExpect(jsonPath("status").value(DeliveryStatus.PENDING.name()))
    );

    // when TicketWasAccepted event is published
    applicationEventPublisher.publishEvent(TicketWasAccepted.builder()
        .orderId(order.getId())
        .ticketId(1L)
        .build());

    // then delivery is scheduled
    deliveryHelper.getResourceById(deliveryId, rs -> rs
        .andExpect(status().isOk())
        .andExpect(jsonPath("deliveryId").value(deliveryId))
        .andExpect(jsonPath("customerId").value(order.getCustomerId()))
        .andExpect(jsonPath("orderId").value(order.getId()))
        .andExpect(jsonPath("restaurantId").value(order.getRestaurantId()))
        .andExpect(jsonPath("readyBy").isNotEmpty()) //TODO should be same as readyBy specified in the TicketWasAccepted event
        .andExpect(jsonPath("assignedCourierId").value(1L))
        .andExpect(jsonPath("status").value(DeliveryStatus.SCHEDULED.name()))
    );
  }
}