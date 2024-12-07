package com.restaurantapp.ndnhuy.common;

import com.restaurantapp.ndnhuy.orderservice.CreateOrderRequest;
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

import static com.restaurantapp.ndnhuy.common.TestHelper.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class OrderHelper {

  @Autowired
  private final MockMvc mockMvc;

  private TestHelper testHelper = TestHelper.builder().build();

  private OrderHelper resultActions(ResultActions resultActions) {
    testHelper = testHelper.toBuilder().resultActions(resultActions).build();
    return this;
  }

  @SneakyThrows
  public OrderHelper createOrder(CreateOrderRequest request) {
    return this.resultActions(doCreateOrder(request));
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

  public OrderHelper andExpect(ResultMatcher resultMatcher) {
    testHelper.andExpect(resultMatcher);
    return this;
  }

  public JSONObject thenGetResponseAsJson() {
    return testHelper.thenGetResponseAsJson();
  }

  @SneakyThrows
  public Long thenGetOrderId() {
    return this.thenGetResponseAsJson().getLong("id");
  }

}
