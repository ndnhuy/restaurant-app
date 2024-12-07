package com.restaurantapp.ndnhuy.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.CustomerHelper;
import com.restaurantapp.ndnhuy.common.OrderHelper;
import com.restaurantapp.ndnhuy.common.RestaurantHelper;
import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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
    // given customer
    var customerId = customerHelper.validCustomer()
        .thenGetCustomerId();

    // given restaurant with menus
    var rid = restaurantHelper.validRestaurant("KFC", restaurantHelper.givenMenuItems()).thenGetRestaurantId();
    var response = restaurantHelper.getById(rid).thenGetResponseAsJson();
    var arr = response.getJSONArray("menuItems");

    record TestMenuItem(Long id, BigDecimal price, int quantity) {
    }

    var menuItems = IntStream.range(0, arr.length())
        .mapToObj(i -> {
          try {
            return arr.get(i);
          } catch (JSONException e) {
            throw new RuntimeException(e);
          }
        })
        .map(obj -> (JSONObject) obj)
        .map(obj -> {
          try {
            return new TestMenuItem(obj.getLong("id"),
                BigDecimal.valueOf(obj.getDouble("price")),
                ThreadLocalRandom.current().nextInt(0, 10));
          } catch (JSONException e) {
            throw new RuntimeException(e);
          }
        })
        .toList();
    var wantTotal = menuItems.stream()
        .map(item -> item.price.multiply(BigDecimal.valueOf(item.quantity)))
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);


    var resp =
        orderHelper.createOrder(
                CreateOrderRequest.builder()
                    .customerId(customerId)
                    .lineItems(
                        menuItems
                            .stream()
                            .map(item -> CreateOrderRequest.LineItem.builder()
                                .menuItemId(item.id)
                                .quantity(item.quantity)
                                .build())
                            .toList())
                    .build()
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").isNotEmpty())
            .andExpect(jsonPath("customerId").value(customerId))
            .andExpect(jsonPath("status").value(OrderStatus.CREATED.toString()))
            .andExpect(jsonPath("amount").value(wantTotal.doubleValue()))
            .thenGetOrderId();
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
