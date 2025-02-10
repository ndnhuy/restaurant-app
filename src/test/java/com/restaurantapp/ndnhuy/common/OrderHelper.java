package com.restaurantapp.ndnhuy.common;

import com.restaurantapp.ndnhuy.orderservice.CreateOrderRequest;
import com.restaurantapp.ndnhuy.orderservice.GetOrderResponse;
import com.restaurantapp.ndnhuy.orderservice.Order;
import com.restaurantapp.ndnhuy.orderservice.OrderService;
import com.restaurantapp.ndnhuy.restaurantservice.MenuItem;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.restaurantapp.ndnhuy.common.TestHelper.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class OrderHelper implements EntityTestSupport<CreateOrderRequest, Long> {

  @Autowired
  private final MockMvc mockMvc;

  @Autowired
  private CustomerHelper customerHelper;

  @Autowired
  private RestaurantHelper restaurantHelper;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Autowired
  private OrderService orderService;

  public Long givenValidOrderId() {
    return getResourceId(givenValidResource());
  }

  public Order getOrder(Long orderId) {
    return orderService.findOrder(orderId).get();
  }

  @SneakyThrows
  private ResultActions doCreateOrder(CreateOrderRequest request) {
    return this.mockMvc.perform(
            post("/orders")
                .contentType("application/json")
                .content(asJsonString(request))
        )
        .andDo(print());
  }

  @Override
  public ResultActions doCreateResource(CreateOrderRequest request) {
    return doCreateOrder(request);
  }

  @Override
  @SneakyThrows
  public JSONObject getResourceById(Long resourceId, ResultAssert resultAssert) {
    var r = this.mockMvc
        .perform(get("/orders/" + resourceId))
        .andDo(print());
    resultAssert.accept(r);
    return getReponseAsJson(r);
  }

  @SneakyThrows
  public GetOrderResponse fetchOrderById(Long orderId) {
    var responseJson = getResourceById(orderId, rs -> rs.andExpect(status().isOk()));
    return new ObjectMapper().readValue(responseJson.toString(), GetOrderResponse.class);
  }

  @SneakyThrows
  public List<RequestLineItem> getAllMenuItemsOfRestaurant(Long restaurantId) {
    var response = restaurantHelper.getResourceById(restaurantId, rs -> rs.andExpect(status().isOk()));
    var arr = response.getJSONArray("menuItems");

    record TestMenuItem(Long id, int quantity) {
    }

    return IntStream.range(0, arr.length())
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
                ThreadLocalRandom.current().nextInt(0, 10));
          } catch (JSONException e) {
            throw new RuntimeException(e);
          }
        })
        .map(item -> RequestLineItem.builder()
            .menuItemId(item.id)
            .quantity(item.quantity)
            .build())
        .toList();
  }

  @Override
  @SneakyThrows
  public CreateOrderRequest givenValidCreationRequest() {
    // given customer
    var customerId = customerHelper.givenValidCustomerId();

    // given restaurant with menus
    var rid = restaurantHelper.givenValidRestaurantId();
    return CreateOrderRequest.builder()
        .customerId(customerId)
        .restaurantId(rid)
        .lineItems(
            getAllMenuItemsOfRestaurant(rid)
        )
        .build();
  }

  public BigDecimal wantTotalAmountFromRequest(CreateOrderRequest request) {
    var ids = request.getLineItems().stream()
        .map(RequestLineItem::getMenuItemId)
        .toList();
    var priceById = restaurantRepository.findMenuItemsIn(ids).stream()
        .collect(Collectors.toMap(MenuItem::getId, MenuItem::getPrice));
    return request.getLineItems()
        .stream()
        .map(item -> priceById.get(item.getMenuItemId()).multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal::add)
        .get();
  }

  @Override
  @SneakyThrows
  public Long getResourceId(JSONObject jsonObject) {
    return jsonObject.getLong("id");
  }

}
