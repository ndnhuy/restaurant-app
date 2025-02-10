package com.restaurantapp.ndnhuy.deliveryservice;

import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.DeliveryHelper;
import com.restaurantapp.ndnhuy.common.OrderHelper;
import com.restaurantapp.ndnhuy.common.TestFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
  }
}