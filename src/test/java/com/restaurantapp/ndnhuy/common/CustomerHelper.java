package com.restaurantapp.ndnhuy.common;

import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
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
public class CustomerHelper {

  @Autowired
  private final MockMvc mockMvc;

  private TestHelper testHelper = TestHelper.builder().build();

  private CustomerHelper resultActions(ResultActions resultActions) {
    testHelper = testHelper.toBuilder().resultActions(resultActions).build();
    return this;
  }

  @SneakyThrows
  public CustomerHelper createCustomer(CreateCustomerRequest request) {
    return this.resultActions(doCreateCustomer(request));
  }

  public CustomerHelper validCustomer() {
    return createCustomer(CreateCustomerRequest
        .builder()
        .firstName("John")
        .lastName("Wick")
        .build());
  }

  @SneakyThrows
  private ResultActions doCreateCustomer(CreateCustomerRequest request) {
    return mockMvc.perform(
            post("/customers")
                .contentType("application/json")
                .content(
                    asJsonString(request)))
        .andDo(print());
  }

  public CustomerHelper andExpect(ResultMatcher resultMatcher) {
    testHelper.andExpect(resultMatcher);
    return this;
  }

  public JSONObject thenGetResponseAsJson() {
    return testHelper.thenGetResponseAsJson();
  }

  @SneakyThrows
  public Long thenGetCustomerId() {
    return this.thenGetResponseAsJson().getLong("customerId");
  }

}
