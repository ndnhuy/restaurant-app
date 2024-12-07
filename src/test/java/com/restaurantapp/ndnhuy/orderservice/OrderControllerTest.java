package com.restaurantapp.ndnhuy.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.CustomerHelper;
import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import com.restaurantapp.ndnhuy.customerservice.CustomerControllerTest;
import com.restaurantapp.ndnhuy.customerservice.CustomerRepository;
import com.restaurantapp.ndnhuy.customerservice.CustomerService;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

  @Test
  @SneakyThrows
  public void testCreateOrder_shouldSuccess() {
    // given customer
    var customerId = customerHelper.validCustomer()
        .thenGetCustomerId();

    // given menus
    var menuIds = List.of(1L, 2L);
    var resp = this.mockMvc.perform(
            post("/orders")
                .contentType("application/json")
                .content(
                    asJsonString(
                        CreateOrderRequest.builder()
                            .customerId(customerId)
                            .lineItems(
                                menuIds
                                    .stream()
                                    .map(id -> CreateOrderRequest.LineItem.builder()
                                        .menuItemId(id)
                                        .quantity(1)
                                        .build())
                                    .toList())
                            .build()
                    )
                )
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("customerId").isNotEmpty())
        .andExpect(jsonPath("firstName").value("John"))
        .andExpect(jsonPath("lastName").value("Wick"))
        .andReturn();
    var json = new JSONObject(resp.getResponse().getContentAsString());
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
