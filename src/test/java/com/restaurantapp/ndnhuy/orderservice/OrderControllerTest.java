package com.restaurantapp.ndnhuy.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.CustomerHelper;
import com.restaurantapp.ndnhuy.common.OrderHelper;
import com.restaurantapp.ndnhuy.common.RequestLineItem;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

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
                .andExpect(jsonPath("restaurantId").value(request.getRestaurantId()))
                .andExpect(jsonPath("status").value(OrderStatus.CREATED.toString()))
                .andExpect(jsonPath("amount").value(wantOrderAmount)),
            orderHelper::getReponseAsJson
        )
    );
    Consumer<ResultActions> assertMenuItems = rs -> {
      for (var i = 0; i < request.getLineItems().size(); i++) {
        var wantMenu = request.getLineItems().get(i);
        BiFunction<Integer, String, String> path = (index, name) -> String.format("orderLineItems[%d].%s", index, name);
        try {
          rs.andExpect(jsonPath(path.apply(i, "menuItemId")).value(wantMenu.getMenuItemId()))
              .andExpect(jsonPath(path.apply(i, "quantity")).value(wantMenu.getQuantity()));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
    orderHelper.getResourceById(orderId, rs ->
        assertMenuItems.accept(
            rs.andExpect(jsonPath("id").value(orderId))
                .andExpect(jsonPath("customerId").value(request.getCustomerId()))
                .andExpect(jsonPath("status").value(OrderStatus.CREATED.toString()))
                .andExpect(jsonPath("amount").value(wantOrderAmount))
                .andExpect(status().isOk()))
    );
  }

  @Test
  @SneakyThrows
  public void testCreateOrder_shouldFailWhenMenuItemsNotFound() {
    // given customer
    var customerId = customerHelper.givenValidCustomerId();

    // given restaurant with menus
    var rid = restaurantHelper.givenValidRestaurantId();
    var request = CreateOrderRequest.builder()
        .customerId(customerId)
        .restaurantId(rid)
        .lineItems(
            List.of(
                RequestLineItem.builder()
                    .menuItemId(999L)
                    .quantity(1)
                    .build()
            ))
        .build();

    orderHelper.createResource(
        request,
        rs -> rs.andExpect(status().isBadRequest()).andDo(print()),
        Function.identity()
    );
  }

  @Test
  @SneakyThrows
  public void testCreateOrder_shouldFailWhenMenuItemsNotBelongToRestaurant() {
    // given customer
    var customerId = customerHelper.givenValidCustomerId();

    // given restaurant with menus
    var rid = restaurantHelper.givenValidRestaurantId();
    var rid2 = restaurantHelper.givenValidRestaurantId();
    var request = CreateOrderRequest.builder()
        .customerId(customerId)
        .restaurantId(rid)
        .lineItems(
            orderHelper.getAllMenuItemsOfRestaurant(rid2)
        )
        .build();

    orderHelper.createResource(
        request,
        rs -> rs.andExpect(status().isBadRequest()).andDo(print()),
        Function.identity()
    );
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
