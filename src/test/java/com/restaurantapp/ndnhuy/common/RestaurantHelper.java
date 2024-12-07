package com.restaurantapp.ndnhuy.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import com.restaurantapp.ndnhuy.restaurantservice.CreateRestaurantRequest;
import com.restaurantapp.ndnhuy.restaurantservice.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class RestaurantHelper {

  @Autowired
  private final MockMvc mockMvc;

  private ResultActions resultActions;

  private RestaurantHelper cloneInstance() {
    var obj = new RestaurantHelper(mockMvc);
    obj.resultActions = this.resultActions;
    return obj;
  }

  private RestaurantHelper resultActions(ResultActions resultActions) {
    var obj = cloneInstance();
    obj.resultActions = resultActions;
    return obj;
  }

  @SneakyThrows
  public RestaurantHelper createRestaurant(CreateRestaurantRequest request) {
    return cloneInstance().resultActions(doCreateRestaurant(request));
  }

  public RestaurantHelper validRestaurant(String restaurantName, List<Restaurant.MenuItem> menus) {
    return createRestaurant(CreateRestaurantRequest
        .builder()
        .name(restaurantName)
        .menuItems(menus)
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
    try {
      this.resultActions.andExpect(resultMatcher);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return cloneInstance();
  }

  public JSONObject thenGetResponseAsJson() {
    try {
      return new JSONObject(this.resultActions.andReturn().getResponse().getContentAsString());
    } catch (JSONException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @SneakyThrows
  public RestaurantHelper getById(Long id) {
    return cloneInstance()
        .resultActions(this.mockMvc
            .perform(get("/restaurants/" + id))
            .andDo(print())
        );
  }

  @SneakyThrows
  public Long thenGetRestaurantId() {
    return this.thenGetResponseAsJson().getLong("id");
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
