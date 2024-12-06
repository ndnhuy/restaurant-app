package com.restaurantapp.ndnhuy.common;

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
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.List;

import static com.restaurantapp.ndnhuy.common.TestHelper.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class RestaurantHelper {

  @Autowired
  private final MockMvc mockMvc;

  private TestHelper testHelper = TestHelper.builder().build();

  private RestaurantHelper resultActions(ResultActions resultActions) {
    testHelper = testHelper.toBuilder().resultActions(resultActions).build();
    return this;
  }

  @SneakyThrows
  public RestaurantHelper createRestaurant(CreateRestaurantRequest request) {
    return resultActions(doCreateRestaurant(request));
  }

  public RestaurantHelper validRestaurant(String restaurantName, List<MenuItem> menus) {
    return createRestaurant(CreateRestaurantRequest
        .builder()
        .name(restaurantName)
        .menuItems(menus)
        .build());
  }

  public List<MenuItem> givenMenuItems() {
    return List.of(
        MenuItem
            .builder()
            .name("pizza type 1")
            .price(new BigDecimal(5.5))
            .build(),
        MenuItem
            .builder()
            .name("pizza type 2")
            .price(new BigDecimal(10))
            .build());
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

  public RestaurantHelper andExpect(ResultMatcher resultMatcher) {
    testHelper.andExpect(resultMatcher);
    return this;
  }

  public JSONObject thenGetResponseAsJson() {
    return testHelper.thenGetResponseAsJson();
  }

  @SneakyThrows
  public RestaurantHelper getById(Long id) {
    var obj = new RestaurantHelper(mockMvc);
    return obj.resultActions(this.mockMvc
        .perform(get("/restaurants/" + id))
        .andDo(print())
    );
  }

  @SneakyThrows
  public Long thenGetRestaurantId() {
    return this.thenGetResponseAsJson().getLong("id");
  }

}
