package com.restaurantapp.ndnhuy.paymentservice;

import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.CustomerHelper;
import com.restaurantapp.ndnhuy.common.OrderHelper;
import com.restaurantapp.ndnhuy.common.PaymentHelper;
import com.restaurantapp.ndnhuy.common.RestaurantHelper;
import com.restaurantapp.ndnhuy.orderservice.OrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class})
@ActiveProfiles("test")
public class PaymentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private OrderService customerService;

  @Autowired
  private CustomerHelper customerHelper;

  @Autowired
  private RestaurantHelper restaurantHelper;

  @Autowired
  private OrderHelper orderHelper;

  @Autowired
  private PaymentHelper paymentHelper;

  @Test
  @SneakyThrows
  public void testPayOrder_shouldSuccess() {
    // given order
    var orderId = orderHelper.givenValidOrderId();
    paymentHelper.createPaymentOrder(CreatePaymentOrderRequest.builder()
            .orderId(orderId)
            .build())
        .andExpect(status().isOk());


  }

}
