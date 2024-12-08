package com.restaurantapp.ndnhuy.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.CustomerHelper;
import com.restaurantapp.ndnhuy.common.OrderHelper;
import com.restaurantapp.ndnhuy.common.RestaurantHelper;
import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class})
@ActiveProfiles("test")
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private OrderService customerService;

  @Autowired
  private OrderRepository customerRepository;

  @Autowired
  private CustomerHelper customerHelper;

  @Autowired
  private RestaurantHelper restaurantHelper;

  @Autowired
  private OrderHelper orderHelper;

  @Test
  @SneakyThrows
  public void testCreateOrder_shouldSuccess() {
    var request = orderHelper.givenValidCreationRequest();
    var wantOrderAmount = orderHelper.wantTotalAmountFromRequest(request).doubleValue();
    var orderId = orderHelper.getResourceId(
        orderHelper.createResource(
            request,
            rs -> rs.andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("customerId").value(request.getCustomerId()))
                .andExpect(jsonPath("status").value(OrderStatus.CREATED.toString()))
                .andExpect(jsonPath("amount").value(wantOrderAmount)),
            orderHelper::getReponseAsJson
        )
    );
    orderHelper.getResourceById(orderId, rs ->
        rs.andExpect(jsonPath("id").value(orderId))
            .andExpect(jsonPath("customerId").value(request.getCustomerId()))
            .andExpect(jsonPath("status").value(OrderStatus.CREATED.toString()))
            .andExpect(jsonPath("amount").value(wantOrderAmount))
            .andExpect(status().isOk()));
  }

  @Test
  @SneakyThrows
  public void testCreateCustomer_shouldFail_whenMissingFirstName() {
    this.mockMvc.perform(
            post("/customers")
                .contentType("application/json")
                .content(
                    asJsonString(
                        CreateCustomerRequest
                            .builder()
                            .lastName("Wick")
                            .build())))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
