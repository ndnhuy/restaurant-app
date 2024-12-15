package com.restaurantapp.ndnhuy.common;

import com.restaurantapp.ndnhuy.restaurantservice.AcceptOrderRequest;
import com.restaurantapp.ndnhuy.restaurantservice.CreateRestaurantRequest;
import com.restaurantapp.ndnhuy.restaurantservice.MenuItem;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static com.restaurantapp.ndnhuy.common.TestHelper.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class RestaurantHelper implements EntityTestSupport<CreateRestaurantRequest, Long> {

  @Autowired
  private final MockMvc mockMvc;

  public Long givenValidRestaurantId() {
    return getResourceId(
        createResource(
            givenValidCreationRequest(),
            rs -> rs.andExpect(status().isOk()),
            this::getReponseAsJson
        )
    );
  }

  @SneakyThrows
  private ResultActions doCreateRestaurant(CreateRestaurantRequest request) {
    return this.mockMvc.perform(
            post("/restaurants")
                .contentType("application/json")
                .content(asJsonString(request))
        )
        .andDo(print());
  }

  @Override
  public ResultActions doCreateResource(CreateRestaurantRequest createRestaurantRequest) {
    return doCreateRestaurant(createRestaurantRequest);
  }

  @Override
  @SneakyThrows
  public JSONObject getResourceById(Long resourceId, ResultAssert resultAssert) {
    var r = this.mockMvc
        .perform(get("/restaurants/" + resourceId))
        .andDo(print());
    resultAssert.accept(r);
    return getReponseAsJson(r);
  }

  @Override
  public CreateRestaurantRequest givenValidCreationRequest() {
    return CreateRestaurantRequest
        .builder()
        .name("Pizza Hut")
        .menuItems(List.of(
            MenuItem
                .builder()
                .name("pizza type 1")
                .price(new BigDecimal(5.5))
                .build(),
            MenuItem
                .builder()
                .name("pizza type 2")
                .price(new BigDecimal(10))
                .build()))
        .build();
  }

  @Override
  @SneakyThrows
  public Long getResourceId(JSONObject jsonObject) {
    return jsonObject.getLong("id");
  }

  @SneakyThrows
  public ResultActions acceptOrder(Long orderId) {
    return this.mockMvc.perform(
            post("/restaurants/accept")
                .contentType("application/json")
                .content(asJsonString(AcceptOrderRequest.builder()
                    .orderId(orderId)
                    .build()))
        )
        .andDo(print());
  }
}
